package ru.mentee.power.crm.servlet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.service.LeadService;

@ExtendWith(MockitoExtension.class)
class LeadListServletTest {

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private ServletContext servletContext;

  @Mock
  private ServletConfig servletConfig;

  @Mock
  private LeadService leadService;

  @InjectMocks
  private LeadListServlet servlet;

  private StringWriter stringWriter;
  private PrintWriter printWriter;

  @BeforeEach
  void setUp() throws Exception {
    stringWriter = new StringWriter();
    printWriter = new PrintWriter(stringWriter);

    when(response.getWriter()).thenReturn(printWriter);
    when(servletConfig.getServletContext()).thenReturn(servletContext);
    servlet.init(servletConfig);
  }

  @Test
  void shouldReturnHtmlTableWhenDoGetCalled() throws Exception {
    // Given
    List<Lead> leads = Arrays.asList(
        new Lead(UUID.randomUUID(), "john@example.com", "Company A", LeadStatus.NEW),
        new Lead(UUID.randomUUID(), "jane@example.com", "Company B", LeadStatus.CONTACTED),
        new Lead(UUID.randomUUID(), "bob@example.com", "Company C", LeadStatus.QUALIFIED)
    );

    when(servletContext.getAttribute("leadService")).thenReturn(leadService);
    when(leadService.findAll()).thenReturn(leads);
    LeadListServlet spyServlet = spy(servlet);
    when(spyServlet.getServletContext()).thenReturn(servletContext);

    // When
    spyServlet.doGet(request, response);

    // Then
    verify(leadService, times(1)).findAll();
    verify(response).getWriter();

    printWriter.flush();
    String htmlOutput = stringWriter.toString();

    assertTrue(htmlOutput.contains("<!DOCTYPE html>"));
    assertTrue(htmlOutput.contains("<html>"));
    assertTrue(htmlOutput.contains("<head><title>CRM - Lead List</title></head>"));
    assertTrue(htmlOutput.contains("<h1>Lead List</h1>"));

    assertTrue(htmlOutput.contains("<table border='1'>"));
    assertTrue(htmlOutput.contains("<th>Email</th>"));
    assertTrue(htmlOutput.contains("<th>Company</th>"));
    assertTrue(htmlOutput.contains("<th>Status</th>"));

    assertTrue(htmlOutput.contains("john@example.com"));
    assertTrue(htmlOutput.contains("Company A"));
    assertTrue(htmlOutput.contains("NEW"));

    assertTrue(htmlOutput.contains("jane@example.com"));
    assertTrue(htmlOutput.contains("Company B"));
    assertTrue(htmlOutput.contains("CONTACTED"));

    assertTrue(htmlOutput.contains("bob@example.com"));
    assertTrue(htmlOutput.contains("Company C"));
    assertTrue(htmlOutput.contains("QUALIFIED"));

    assertTrue(htmlOutput.contains("</tbody>"));
    assertTrue(htmlOutput.contains("</table>"));
    assertTrue(htmlOutput.contains("</body>"));
    assertTrue(htmlOutput.contains("</html>"));
  }

  @Test
  void shouldSetContentTypeToHtmlWhenDoGetCalled() throws Exception {
    // Given
    when(servletContext.getAttribute("leadService")).thenReturn(leadService);
    when(leadService.findAll()).thenReturn(Collections.emptyList());

    // When
    servlet.doGet(request, response);

    // Then
    verify(response).setContentType("text/html; charset=UTF-8");
  }

  @Test
  void shouldHandleEmptyLeadList() throws Exception {
    // Given
    when(servletContext.getAttribute("leadService")).thenReturn(leadService);
    when(leadService.findAll()).thenReturn(Collections.emptyList());

    // When
    servlet.doGet(request, response);

    // Then
    verify(leadService).findAll();

    printWriter.flush();
    String htmlOutput = stringWriter.toString();

    assertTrue(htmlOutput.contains("<tbody>"));
    assertTrue(htmlOutput.contains("</tbody>"));
    assertFalse(htmlOutput.contains("<td>"));
  }

  @Test
  void shouldHandleNullLeadFields() throws Exception {
    // Given
    List<Lead> leads = Arrays.asList(
        new Lead(null, null, null, null),
        new Lead(null, "", "", null)
    );

    when(servletContext.getAttribute("leadService")).thenReturn(leadService);
    when(leadService.findAll()).thenReturn(leads);

    // When
    servlet.doGet(request, response);

    // Then
    verify(leadService).findAll();

    printWriter.flush();
    String htmlOutput = stringWriter.toString();

    assertTrue(htmlOutput.contains("<td>null</td>") || htmlOutput.contains("<td></td>"));
  }
}