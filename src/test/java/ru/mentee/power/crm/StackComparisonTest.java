package ru.mentee.power.crm;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.*;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.mentee.power.crm.infrastructure.InMemoryLeadRepository;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;
import ru.mentee.power.crm.servlet.LeadListServlet;
import ru.mentee.power.crm.service.LeadService;
import ru.mentee.power.crm.spring.Application;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Интеграционный тест сравнения Servlet и Spring Boot стеков.
 * Запускает оба сервера, выполняет HTTP запросы, сравнивает результаты.
 */
class StackComparisonTest {
  private static final int SERVLET_PORT = 8080;
  private static final int SPRING_PORT = 8081;

  private static Tomcat servletTomcat;
  private static ConfigurableApplicationContext springContext;

  private static long servletStartupMs;
  private static long springStartupMs;

  private HttpClient httpClient;

  @BeforeAll
  static void startServers() throws Exception {

    System.setProperty("spring.sql.init.mode", "never");

    // ===== Servlet стек =====
    LeadRepository repository = new InMemoryLeadRepository();
    LeadService leadService = new LeadService(repository);

    leadService.addLead("john.doe@example.com", "Tech Corp", LeadStatus.NEW);
    leadService.addLead("jane.smith@example.com", "Business Ltd", LeadStatus.CONTACTED);
    leadService.addLead("bob.wilson@example.com", "Startup Inc", LeadStatus.QUALIFIED);
    leadService.addLead("alice.brown@example.com", "Enterprise Co", LeadStatus.NEW);
    leadService.addLead("charlie.davis@example.com", "Ventures LLC", LeadStatus.LOST);
    leadService.addLead("<script>alert('XSS')</script>", "XSS Test Company", LeadStatus.NEW);

    servletTomcat = new Tomcat();
    servletTomcat.setPort(SERVLET_PORT);
    servletTomcat.getConnector();

    Context context = servletTomcat.addContext("", new File(".").getAbsolutePath());

    context.getServletContext().setAttribute("leadService", leadService);

    Tomcat.addServlet(context,"LeadListServlet", new LeadListServlet());
    context.addServletMappingDecoded("/leads","LeadListServlet");

    long servletStart = System.nanoTime();
    servletTomcat.start();
    servletStartupMs = (System.nanoTime() - servletStart) / 1_000_000;

    Thread.sleep(500);

    // ===== Spring Boot стек =====
    long springStart = System.nanoTime();
    springContext = SpringApplication.run(Application.class, "--server.port=" + SPRING_PORT);
    springStartupMs = (System.nanoTime() - springStart) / 1_000_000;

    Thread.sleep(500);
  }

  @AfterAll
  static void stopServers() throws Exception {

    // Останавливаем Embedded Tomcat
    if (servletTomcat != null) {servletTomcat.stop();servletTomcat.destroy();
    }

    // Останавливаем Spring Boot
    if (springContext != null) {springContext.close();
    }
  }

  @BeforeEach
  void setUp() {
    httpClient = HttpClient.newHttpClient();
  }

  @Test
  @DisplayName("Оба стека должны возвращать лидов в HTML таблице")
  void shouldReturnLeadsFromBothStacks() throws Exception {

    HttpRequest servletRequest = HttpRequest.newBuilder().uri(URI.create("http://localhost:" + SERVLET_PORT + "/leads")).GET().build();
    HttpRequest springRequest = HttpRequest.newBuilder().uri(URI.create("http://localhost:" + SPRING_PORT + "/leads")).GET().build();

    HttpResponse<String> servletResponse = httpClient.send(servletRequest, HttpResponse.BodyHandlers.ofString());
    HttpResponse<String> springResponse = httpClient.send(springRequest, HttpResponse.BodyHandlers.ofString());

    assertThat(servletResponse.statusCode()).isEqualTo(200);
    assertThat(springResponse.statusCode()).isEqualTo(200);

    assertThat(servletResponse.body()).contains("<table");
    assertThat(springResponse.body()).contains("<table");

    int servletRows = countTableRows(servletResponse.body());
    int springRows = countTableRows(springResponse.body());

    assertThat(servletRows).as("Количество лидов должно совпадать").isEqualTo(springRows);

    System.out.printf("Servlet: %d лидов, Spring: %d лидов%n", servletRows, springRows);
  }

  /**
   * Подсчитывает количество строк <tr> в HTML (количество лидов в таблице).
   */
  private int countTableRows(String html) {
    return html.split("<tr").length - 1;
  }

  @Test
  @DisplayName("Измерение времени старта обоих стеков")
  void shouldMeasureStartupTime() throws Exception{
    long servletStartupMs = measureServletStartup();
    long springStartupMs = measureSpringBootStartup();

    System.out.println("=== Сравнение времени старта ===");
    System.out.printf("Servlet стек: %d ms%n", servletStartupMs);
    System.out.printf("Spring Boot: %d ms%n", springStartupMs);
    System.out.printf("Разница: Spring %s на %d ms%n", springStartupMs > servletStartupMs ? "медленнее" : "быстрее", Math.abs(springStartupMs - servletStartupMs));

    assertThat(servletStartupMs).isLessThan(10_000);
    assertThat(springStartupMs).isLessThan(15_000);
  }

  private long measureServletStartup() {
    return servletStartupMs;
  }

  private long measureSpringBootStartup() {
    return springStartupMs;
  }
}
