package ru.mentee.power.crm;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import ru.mentee.power.crm.infrastructure.InMemoryLeadRepository;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;
import ru.mentee.power.crm.service.LeadService;
import ru.mentee.power.crm.servlet.LeadListServlet;

import java.io.File;

public class Main {

  public static void main(String[] args) throws Exception {

    LeadRepository leadRepository = new InMemoryLeadRepository();

    LeadService leadService = new LeadService(leadRepository);

    leadService.addLead("john.doe@example.com", "Tech Corp", LeadStatus.NEW);
    leadService.addLead("jane.smith@example.com", "Business Ltd", LeadStatus.CONTACTED);
    leadService.addLead("bob.wilson@example.com", "Startup Inc", LeadStatus.QUALIFIED);
    leadService.addLead("alice.brown@example.com", "Enterprise Co", LeadStatus.NEW);
    leadService.addLead("charlie.davis@example.com", "Ventures LLC", LeadStatus.LOST);

    Tomcat tomcat = new Tomcat();
    tomcat.setPort(8080);
    tomcat.getConnector();

    Context context = tomcat.addContext("", new File(".").getAbsolutePath());
    context.getServletContext().setAttribute("leadService", leadService);
    tomcat.addServlet(context, "LeadListServlet", new LeadListServlet());
    context.addServletMappingDecoded("/leads", "LeadListServlet");
    tomcat.start();

    System.out.println("Tomcat started on port " + 8080);
    System.out.println("Open http://localhost:8080/leads in browser");
    System.out.println("To stop press Ctrl+C");

    tomcat.getServer().await();
  }
}