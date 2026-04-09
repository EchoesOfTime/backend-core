package ru.mentee.power.crm.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;

class InMemoryLeadRepositoryTest {

  private InMemoryLeadRepository repository;

  @BeforeEach
  void setUp() {
    repository = new InMemoryLeadRepository();
  }

  @Test
  void shouldAddLeadAndFindItById() {
    UUID leadId = UUID.randomUUID();
    Lead lead = new Lead(leadId, "john@example.com", "Test Company", LeadStatus.NEW);

    repository.save(lead);

    assertThat(repository.findAll()).hasSize(1).containsExactly(lead);
    assertThat(repository.findById(leadId)).isPresent().contains(lead);
  }

  @Test
  void shouldReturnEmptyOptionalWhenFindingNonExistentLead() {
    for (int i = 0; i < 10; i++) {
      Lead lead = new Lead(UUID.randomUUID(), "user"
          + i + "@example.com", "Company " + i, LeadStatus.NEW);
      repository.save(lead);
    }

    UUID nonExistentId = UUID.randomUUID();
    var result = repository.findById(nonExistentId);

    assertThat(result).isEmpty();
  }

  @Test
  void shouldOverwriteLeadWhenSavingWithSameId() {
    UUID leadId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    Lead lead1 = new Lead(leadId, "company1@example.com", "Company1", LeadStatus.NEW);
    repository.save(lead1);

    Lead lead2 = new Lead(leadId, "company2@example.com", "Company2", LeadStatus.QUALIFIED);
    repository.save(lead2);

    assertThat(repository.findAll()).hasSize(1).containsExactly(lead2);
    assertThat(repository.findByEmail("company1@example.com")).isPresent().contains(lead2);
    assertThat(repository.findByEmail("company2@example.com")).isPresent().contains(lead2);
  }

  @Test
  void shouldRemoveExistingLead() {
    UUID firstLeadId = UUID.randomUUID();
    UUID secondLeadId = UUID.randomUUID();
    UUID thirdLeadId = UUID.randomUUID();
    UUID fourthLeadId = UUID.randomUUID();
    UUID fifthLeadId = UUID.randomUUID();

    Lead lead1 = new Lead(firstLeadId, "company1@example.com", "Company1", LeadStatus.NEW);
    Lead lead2 = new Lead(secondLeadId, "company2@example.com", "Company2", LeadStatus.NEW);
    Lead lead3 = new Lead(thirdLeadId, "company3@example.com", "Company3", LeadStatus.NEW);
    Lead lead4 = new Lead(fourthLeadId, "company4@example.com", "Company4", LeadStatus.NEW);
    Lead lead5 = new Lead(fifthLeadId, "company5@example.com", "Company5", LeadStatus.NEW);

    repository.save(lead1);
    repository.save(lead2);
    repository.save(lead3);
    repository.save(lead4);
    repository.save(lead5);

    repository.delete(thirdLeadId);  // удаляем 3 лид

    assertThat(repository.findAll()).hasSize(4)
        .containsExactlyInAnyOrder(lead1, lead2, lead4, lead5).doesNotContain(lead3);
    assertThat(repository.findById(thirdLeadId)).isEmpty();
    assertThat(repository.findById(firstLeadId)).isPresent().contains(lead1);
    assertThat(repository.findById(secondLeadId)).isPresent().contains(lead2);
    assertThat(repository.findById(fourthLeadId)).isPresent().contains(lead4);
    assertThat(repository.findById(fifthLeadId)).isPresent().contains(lead5);
  }

  @Test
  void shouldReturnDefensiveCopyThatDoesNotAffectInternalStorage() {
    Lead lead1 = new Lead(UUID.randomUUID(), "company1@example.com", "Company1", LeadStatus.NEW);
    Lead lead2 = new Lead(UUID.randomUUID(), "company2@example.com", "Company2", LeadStatus.NEW);

    repository.save(lead1);
    repository.save(lead2);

    List<Lead> returnedList = repository.findAll();
    returnedList.remove(0);
    returnedList.clear();

    assertThat(repository.findAll()).hasSize(2).containsExactlyInAnyOrder(lead1, lead2);
    assertThat(repository.findById(lead1.id())).isPresent();
    assertThat(repository.findById(lead2.id())).isPresent();
  }
}