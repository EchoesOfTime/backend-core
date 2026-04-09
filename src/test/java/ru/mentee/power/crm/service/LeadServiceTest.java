package ru.mentee.power.crm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.infrastructure.InMemoryLeadRepository;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.repository.LeadRepository;

class LeadServiceTest {
  private LeadService service;
  private LeadRepository repository;

  @BeforeEach
  void setUp() {
    repository = new InMemoryLeadRepository();
    service = new LeadService(repository);
  }

  @Test
  void shouldCreateLeadWhenEmailIsUnique() {
    String email = "duplicate@example.com";
    String company = "Test Company";
    LeadStatus status = LeadStatus.NEW;

    Lead result = service.addLead(email, company, status);

    assertThat(result).isNotNull();
    assertThat(result.email()).isEqualTo(email);
    assertThat(result.company()).isEqualTo(company);
    assertThat(result.status()).isEqualTo(status);
    assertThat(result.id()).isNotNull();
  }

  @Test
  void shouldThrowExceptionWhenEmailAlreadyExists() {
    String email = "duplicate@example.com";
    service.addLead(email, "First Company", LeadStatus.NEW);
    assertThatThrownBy(() -> service.addLead(email, "Second Company", LeadStatus.NEW))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Lead with email already exists");
  }

  @Test
  void shouldFindAllLeads() {
    service.addLead("one@example.com", "Company 1", LeadStatus.NEW);
    service.addLead("two@example.com", "Company 2", LeadStatus.CONTACTED);

    List<Lead> result = service.findAll();

    assertThat(result).hasSize(2);
  }

  @Test
  void shouldFindLeadById() {
    Lead created = service.addLead("find@example.com", "Company", LeadStatus.NEW);

    Optional<Lead> result = service.findById(created.id());

    assertThat(result).isPresent();
    assertThat(result.get().email()).isEqualTo("find@example.com");
  }

  @Test
  void shouldFindLeadByEmail() {
    service.addLead("search@example.com", "Company", LeadStatus.NEW);

    Optional<Lead> result = service.findByEmail("search@example.com");

    assertThat(result).isPresent();
    assertThat(result.get().company()).isEqualTo("Company");
  }

  @Test
  void shouldReturnEmptyWhenLeadNotFound() {
    Optional<Lead> result = service.findByEmail("nonexistent@example.com");

    assertThat(result).isEmpty();
  }

  @Test
  void shouldCreateLeadWithAllPossibleStatuses() {
    // Создаем лиды со всеми статусами
    Lead leadNew = service.addLead("new@example.com", "Company", LeadStatus.NEW);
    Lead leadContacted = service.addLead("contacted@example.com", "Company", LeadStatus.CONTACTED);
    Lead leadQualified = service.addLead("qualified@example.com", "Company", LeadStatus.QUALIFIED);
    Lead leadProposal = service.addLead("proposal@example.com",
        "Company", LeadStatus.PROPOSAL_SENT);
    Lead leadNegotiation = service.addLead("negotiation@example.com",
        "Company", LeadStatus.NEGOTIATION);
    Lead leadWon = service.addLead("won@example.com", "Company", LeadStatus.WON);
    Lead leadLost = service.addLead("lost@example.com", "Company", LeadStatus.LOST);

    // Проверяем каждый статус
    assertThat(leadNew.status()).isEqualTo(LeadStatus.NEW);
    assertThat(leadContacted.status()).isEqualTo(LeadStatus.CONTACTED);
    assertThat(leadQualified.status()).isEqualTo(LeadStatus.QUALIFIED);
    assertThat(leadProposal.status()).isEqualTo(LeadStatus.PROPOSAL_SENT);
    assertThat(leadNegotiation.status()).isEqualTo(LeadStatus.NEGOTIATION);
    assertThat(leadWon.status()).isEqualTo(LeadStatus.WON);
    assertThat(leadLost.status()).isEqualTo(LeadStatus.LOST);

    // Проверяем общее количество
    assertThat(service.findAll()).hasSize(7);
  }
}