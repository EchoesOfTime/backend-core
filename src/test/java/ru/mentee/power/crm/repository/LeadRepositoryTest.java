package ru.mentee.power.crm.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.infrastructure.InMemoryLeadRepository;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;

class LeadRepositoryTest {
  private LeadRepository repository;

  @BeforeEach
  void setUp() {
    repository = new InMemoryLeadRepository();
  }

  @Test
  @DisplayName("Должен сохранить и найти лид по ID")
  void shouldSaveAndFindLeadByIdWhenLeadSaved() {
    UUID id = UUID.randomUUID();
    Lead lead = new Lead(id, "test@mail.com", "ООО Comp", LeadStatus.NEW);
    repository.save(lead);

    var foundLead = repository.findById(id);

    assertThat(foundLead).isPresent();
    assertThat(foundLead.get().id()).isEqualTo(id);
    assertThat(foundLead.get().email()).isEqualTo("test@mail.com");
  }

  @Test
  @DisplayName("Должен вернуть пустой Optional при поиске несуществующего ID")
  void shouldReturnNullWhenLeadNotFound() {
    var foundLead = repository.findById(UUID.randomUUID());

    assertThat(foundLead).isEmpty();
  }

  @Test
  @DisplayName("Должен вернуть список всех сохраненных лидов")
  void shouldReturnAllLeadsWhenMultipleLeadsSaved() {
    Lead lead1 = new Lead(UUID.randomUUID(), "test@mail1.com", "ООО Comp1", LeadStatus.NEW);
    Lead lead2 = new Lead(UUID.randomUUID(), "test@mail2.com", "ООО Comp2", LeadStatus.NEW);
    Lead lead3 = new Lead(UUID.randomUUID(), "test@mail3.com", "ООО Comp3", LeadStatus.NEW);
    repository.save(lead1);
    repository.save(lead2);
    repository.save(lead3);

    List<Lead> allLead = repository.findAll();

    assertThat(allLead).hasSize(3).containsExactlyInAnyOrder(lead1, lead2, lead3);
  }

  @Test
  @DisplayName("Должен удалить лид по ID")
  void shouldDeleteLeadWhenLeadExists() {
    UUID id = UUID.randomUUID();
    Lead lead = new Lead(id, "test@mail.com", "ООО Comp", LeadStatus.NEW);
    repository.save(lead);

    assertThat(repository.findById(id)).isPresent();
    assertThat(repository.findAll()).hasSize(1);

    repository.delete(id);

    assertThat(repository.findById(id)).isEmpty();
    assertThat(repository.findAll()).isEmpty();
  }

  @Test
  @DisplayName("Должен перезаписать лид при сохранении с существующим ID")
  void shouldOverwriteLeadWhenSaveWithSameId() {
    UUID id = UUID.randomUUID();
    Lead lead1 = new Lead(id, "original@mail.com", "ООО Comp", LeadStatus.NEW);
    repository.save(lead1);

    Lead lead2 = new Lead(id, "updated@mail.com", "ООО Comp Updated", LeadStatus.QUALIFIED);
    repository.save(lead2);

    var foundLead = repository.findById(id);
    assertThat(foundLead).isPresent();
    assertThat(foundLead.get().email()).isEqualTo("updated@mail.com");
    assertThat(foundLead.get().company()).isEqualTo("ООО Comp Updated");
    assertThat(foundLead.get().status()).isEqualTo(LeadStatus.QUALIFIED);
    assertThat(repository.findAll()).hasSize(1);
  }
}