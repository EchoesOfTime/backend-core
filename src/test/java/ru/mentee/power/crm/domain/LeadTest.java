package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.storage.LeadStorage;

class LeadTest {

  @Test
  void shouldPreventStringConfusionWhenUsingUUID() {
    UUID uuid = UUID.randomUUID();
    Lead lead = new Lead(uuid, "test@example.com", "+7 489 851 12 34", "TestCorp", "NEW");
    LeadStorage storage = new LeadStorage();
    storage.add(lead);
    Lead foundLead = storage.findById(uuid);
    assertThat(foundLead).isEqualTo(lead);
  }

  @Test
  void shouldCreateLeadWhenValidData() {
    UUID uuid = UUID.randomUUID();
    Lead lead = new Lead(uuid, "test@example.com", "+7 489 851 12 34", "TestCorp", "NEW");
    assertThat(lead.getId()).isEqualTo(uuid);
  }

  @Test
  void shouldGenerateUniqueIdsWhenMultipleLeads() {
    UUID uuid1 = UUID.randomUUID();
    UUID uuid2 = UUID.randomUUID();
    Lead lead1 = new Lead(uuid1, "test@example.com", "+7 489 851 12 34", "TestCorp", "NEW");
    Lead lead2 = new Lead(uuid2, "test2@example.com", "+7 489 851 85 62", "TestCorp", "NEW");
    assertThat(lead1).isNotEqualTo(lead2);
  }

  @Test
  void shouldReturnIdWhenGetIdCalled() {
    UUID uuid = UUID.randomUUID();
    Lead lead = new Lead(uuid, "test@example.com", "+7 489 851 12 34", "TestCorp", "NEW");
    UUID id = lead.getId();
    assertThat(id).isEqualTo(uuid);
  }

  @Test
  void shouldReturnEmailWhenGetEmailCalled() {
    Lead lead = new Lead(UUID.randomUUID(), "test@example.com", "+7 489 851 12 34",
        "TestCorp", "NEW");
    String email = lead.getEmail();
    assertThat(email).isEqualTo("test@example.com");
  }

  @Test
  void shouldReturnPhoneWhenGetPhoneCalled() {
    Lead lead = new Lead(UUID.randomUUID(), "test@example.com", "+7 489 851 12 34",
        "TestCorp", "NEW");
    String phone = lead.getPhone();
    assertThat(phone).isEqualTo("+7 489 851 12 34");
  }

  @Test
  void shouldReturnCompanyWhenGetCompanyCalled() {
    Lead lead = new Lead(UUID.randomUUID(), "test@example.com", "+7 489 851 12 34",
        "TestCorp", "NEW");
    String company = lead.getCompany();
    assertThat(company).isEqualTo("TestCorp");

  }

  @Test
  void shouldReturnStatusWhenGetStatusCalled() {
    Lead lead = new Lead(UUID.randomUUID(), "test@example.com", "+7 489 851 12 34",
        "TestCorp", "NEW");
    String status = lead.getStatus();
    assertThat(status).isEqualTo("NEW");
  }

  @Test
  void shouldReturnToStringWhenGetToStringCalled() {
    UUID uuid = UUID.randomUUID();
    Lead lead = new Lead(uuid, "test@example.com", "+7 489 851 12 34", "TestCorp", "NEW");
    String result  = lead.toString();
    String expected = "Lead{id='" + uuid + "', email='test@example.com', "
        + "phone='+7 489 851 12 34', company='TestCorp', status='NEW'}";
    assertThat(result).isEqualTo(expected);
  }
}