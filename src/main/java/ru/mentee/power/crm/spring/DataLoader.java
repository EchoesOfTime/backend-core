package ru.mentee.power.crm.spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.service.LeadService;

@Configuration
public class DataLoader {

  @Bean
  CommandLineRunner loadData(LeadService leadService) {
    return args -> {
      leadService.addLead("john.doe@example.com", "Tech Corp", LeadStatus.NEW);
      leadService.addLead("jane.smith@example.com", "Business Ltd", LeadStatus.CONTACTED);
      leadService.addLead("bob.wilson@example.com", "Startup Inc", LeadStatus.QUALIFIED);
      leadService.addLead("alice.brown@example.com", "Enterprise Co", LeadStatus.NEW);
      leadService.addLead("charlie.davis@example.com", "Ventures LLC", LeadStatus.LOST);

      leadService.addLead("<script>alert('XSS')</script>", "XSS Test Company", LeadStatus.NEW);
    };
  }
}