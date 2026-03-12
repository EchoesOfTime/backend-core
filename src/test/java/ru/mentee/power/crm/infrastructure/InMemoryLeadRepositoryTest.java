package ru.mentee.power.crm.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

class InMemoryLeadRepositoryTest {

  private InMemoryLeadRepository repository;
  private Address address;
  private Contact contact;

  @BeforeEach
  void setUp() {
    repository = new InMemoryLeadRepository();
    address = new Address("San Francisco", "123 Main St", "94105");
    contact = new Contact("john@example.com", "+7 954 685 23 65", address);
  }

  @Test
  void shouldAddLeadAndFindItById() {
    UUID leadId = UUID.randomUUID();
    Lead lead = new Lead(leadId, contact, "Test Company", "NEW");

    repository.add(lead);

    assertThat(repository.findAll()).hasSize(1).containsExactly(lead);
    assertThat(repository.findById(leadId)).isPresent().contains(lead);
  }

  @Test
  void shouldReturnEmptyOptionalWhenFindingNonExistentLead() {
    for (int i = 0; i < 10; i++) {
      Lead lead = new Lead(UUID.randomUUID(), contact, "Company " + i, "NEW");
      repository.add(lead);
    }

    UUID nonExistentId = UUID.randomUUID();
    var result = repository.findById(nonExistentId);

    assertThat(result).isEmpty();
  }

  @Test
  void shouldNotAddDuplicateLeadWithSameUuid() {
    UUID leadId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    Lead lead1 = new Lead(leadId, contact, "Company1", "NEW");
    repository.add(lead1);

    Lead lead2 = new Lead(leadId, contact, "Company2", "QUALIFIED");
    repository.add(lead2);

    assertThat(repository.findAll()).hasSize(1).containsExactly(lead1);
  }

  @Test
  void shouldRemoveExistingLead() {
    UUID firstLeadId = UUID.randomUUID();
    UUID secondLeadId = UUID.randomUUID();
    UUID thirdLeadId = UUID.randomUUID();
    UUID fourthLeadId = UUID.randomUUID();
    UUID fifthLeadId = UUID.randomUUID();

    Lead lead1 = new Lead(firstLeadId, contact, "Company1", "NEW");
    Lead lead2 = new Lead(secondLeadId, contact, "Company2", "NEW");
    Lead lead3 = new Lead(thirdLeadId, contact, "Company3", "NEW");
    Lead lead4 = new Lead(fourthLeadId, contact, "Company4", "NEW");
    Lead lead5 = new Lead(fifthLeadId, contact, "Company5", "NEW");

    repository.add(lead1);
    repository.add(lead2);
    repository.add(lead3);
    repository.add(lead4);
    repository.add(lead5);

    repository.remove(thirdLeadId);  // удаляем 3 лид

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
    Lead lead1 = new Lead(UUID.randomUUID(), contact, "Company1", "NEW");
    Lead lead2 = new Lead(UUID.randomUUID(), contact, "Company2", "NEW");

    repository.add(lead1);
    repository.add(lead2);

    List<Lead> returnedList = repository.findAll();
    returnedList.remove(0);
    returnedList.clear();

    assertThat(repository.findAll()).hasSize(2).containsExactlyInAnyOrder(lead1, lead2);
    assertThat(repository.findById(lead1.id())).isPresent();
    assertThat(repository.findById(lead2.id())).isPresent();
  }
}