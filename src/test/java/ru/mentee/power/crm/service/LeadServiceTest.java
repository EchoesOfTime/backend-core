package ru.mentee.power.crm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

  @Test
  void shouldReturnOnlyNewLeadsWhenFindByStatusNew() {
    // Given
    LeadRepository testRepository = new InMemoryLeadRepository();

    testRepository.save(new Lead(
        UUID.randomUUID(), "new1@example.com", "Company A", LeadStatus.NEW));
    testRepository.save(new Lead(
        UUID.randomUUID(), "new2@example.com", "Company B", LeadStatus.NEW));
    testRepository.save(new Lead(
        UUID.randomUUID(), "new3@example.com", "Company C", LeadStatus.NEW));

    testRepository.save(new Lead(
        UUID.randomUUID(), "contacted1@example.com", "Company D", LeadStatus.CONTACTED));
    testRepository.save(new Lead(
        UUID.randomUUID(), "contacted2@example.com", "Company E", LeadStatus.CONTACTED));
    testRepository.save(new Lead(
        UUID.randomUUID(), "contacted3@example.com", "Company F", LeadStatus.CONTACTED));
    testRepository.save(new Lead(
        UUID.randomUUID(), "contacted4@example.com", "Company G", LeadStatus.CONTACTED));
    testRepository.save(new Lead(
        UUID.randomUUID(), "contacted5@example.com", "Company H", LeadStatus.CONTACTED));

    testRepository.save(new Lead(
        UUID.randomUUID(), "qualified1@example.com", "Company I", LeadStatus.QUALIFIED));
    testRepository.save(new Lead(
        UUID.randomUUID(), "qualified2@example.com", "Company J", LeadStatus.QUALIFIED));

    LeadService leadService = new LeadService(testRepository);

    // When
    List<Lead> result = leadService.findByStatus(LeadStatus.NEW);

    // Then
    assertThat(result).hasSize(3);
    assertThat(result).allMatch(lead -> lead.status().equals(LeadStatus.NEW));
  }

  @Test
  void shouldReturnEmptyListWhenNoLeadsWithStatusQUALIFIED() {
    // Given:
    LeadRepository testRepository = new InMemoryLeadRepository();

    testRepository.save(new Lead(
        UUID.randomUUID(), "new1@example.com", "Company A", LeadStatus.NEW));
    testRepository.save(new Lead(
        UUID.randomUUID(), "new2@example.com", "Company B", LeadStatus.NEW));

    testRepository.save(new Lead(
        UUID.randomUUID(), "contacted1@example.com", "Company C", LeadStatus.CONTACTED));
    testRepository.save(new Lead(
        UUID.randomUUID(), "contacted2@example.com", "Company D", LeadStatus.CONTACTED));

    LeadService testService = new LeadService(testRepository);

    // When:
    List<Lead> result = testService.findByStatus(LeadStatus.QUALIFIED);

    // Then:
    assertThat(result).isEmpty();
    assertThat(result).hasSize(0);
  }

  @Test
  void shouldReturnOnlyContactedLeadsWhenFindByStatusContacted() {
    // Given
    LeadRepository testRepository = new InMemoryLeadRepository();

    testRepository.save(new Lead(
        UUID.randomUUID(), "new1@example.com", "Company A", LeadStatus.NEW));
    testRepository.save(new Lead(
        UUID.randomUUID(), "new2@example.com", "Company B", LeadStatus.NEW));
    testRepository.save(new Lead(
        UUID.randomUUID(), "new3@example.com", "Company C", LeadStatus.NEW));

    testRepository.save(new Lead(
        UUID.randomUUID(), "contacted1@example.com", "Company D", LeadStatus.CONTACTED));
    testRepository.save(new Lead(
        UUID.randomUUID(), "contacted2@example.com", "Company E", LeadStatus.CONTACTED));
    testRepository.save(new Lead(
        UUID.randomUUID(), "contacted3@example.com", "Company F", LeadStatus.CONTACTED));
    testRepository.save(new Lead(
        UUID.randomUUID(), "contacted4@example.com", "Company G", LeadStatus.CONTACTED));
    testRepository.save(new Lead(
        UUID.randomUUID(), "contacted5@example.com", "Company H", LeadStatus.CONTACTED));

    testRepository.save(new Lead(
        UUID.randomUUID(), "qualified1@example.com", "Company I", LeadStatus.QUALIFIED));
    testRepository.save(new Lead(
        UUID.randomUUID(), "qualified2@example.com", "Company J", LeadStatus.QUALIFIED));

    LeadService leadService = new LeadService(testRepository);

    // When
    List<Lead> result = leadService.findByStatus(LeadStatus.CONTACTED);

    // Then
    assertThat(result).hasSize(5);
    assertThat(result).allMatch(lead -> lead.status().equals(LeadStatus.CONTACTED));
  }

  @Test
  void shouldReturnOnlyQualifiedLeadsWhenFindByStatusQualified() {
    // Given
    LeadRepository testRepository = new InMemoryLeadRepository();

    testRepository.save(new Lead(
        UUID.randomUUID(), "new1@example.com", "Company A", LeadStatus.NEW));
    testRepository.save(new Lead(
        UUID.randomUUID(), "new2@example.com", "Company B", LeadStatus.NEW));
    testRepository.save(new Lead(
        UUID.randomUUID(), "new3@example.com", "Company C", LeadStatus.NEW));

    testRepository.save(new Lead(
        UUID.randomUUID(), "contacted1@example.com", "Company D", LeadStatus.CONTACTED));
    testRepository.save(new Lead(
        UUID.randomUUID(), "contacted2@example.com", "Company E", LeadStatus.CONTACTED));
    testRepository.save(new Lead(
        UUID.randomUUID(), "contacted3@example.com", "Company F", LeadStatus.CONTACTED));
    testRepository.save(new Lead(
        UUID.randomUUID(), "contacted4@example.com", "Company G", LeadStatus.CONTACTED));
    testRepository.save(new Lead(
        UUID.randomUUID(), "contacted5@example.com", "Company H", LeadStatus.CONTACTED));

    testRepository.save(new Lead(
        UUID.randomUUID(), "qualified1@example.com", "Company I", LeadStatus.QUALIFIED));
    testRepository.save(new Lead(
        UUID.randomUUID(), "qualified2@example.com", "Company J", LeadStatus.QUALIFIED));

    LeadService leadService = new LeadService(testRepository);

    // When
    List<Lead> result = leadService.findByStatus(LeadStatus.QUALIFIED);

    // Then
    assertThat(result).hasSize(2);
    assertThat(result).allMatch(lead -> lead.status().equals(LeadStatus.QUALIFIED));
  }
}