package ru.mentee.power.crm.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

class LeadStorageTest {

  @Test
  void shouldAddLeadWhenLeadIsUnique() {
    LeadStorage storage = new LeadStorage();
    Address address = new Address("San Francisco", "123 Main St", "94105");
    Contact contact = new Contact("john@example.com", "+7 954 685 23 65", address);
    Lead uniqueLead = new Lead(UUID.randomUUID(), contact, "Test", "NEW");
    boolean added = storage.add(uniqueLead);
    assertThat(added).isTrue();
    assertThat(storage.size()).isEqualTo(1);
    assertThat(storage.findAll()).containsExactly(uniqueLead);
  }

  @Test
  void shouldRejectDuplicateWhenEmailAlreadyExists() {
    LeadStorage storage = new LeadStorage();
    Address address = new Address("San Francisco", "123 Main St", "94105");
    Contact contact = new Contact("john@example.com", "+7 954 685 23 65", address);
    UUID uuid = UUID.randomUUID();
    Lead existingLead = new Lead(uuid, contact, "Test", "NEW");
    Lead duplicateLead = new Lead(uuid, contact, "Test", "NEW");
    storage.add(existingLead);
    boolean added = storage.add(duplicateLead);
    assertThat(added).isFalse();
    assertThat(storage.size()).isEqualTo(1);
    assertThat(storage.findAll()).containsExactly(existingLead);
  }

  @Test
  void shouldThrowExceptionWhenStorageIsFull() {
    LeadStorage storage = new LeadStorage();
    Address address = new Address("San Francisco", "123 Main St", "94105");

    // Заполняем хранилище до предела (100 лидов)
    for (int index = 0; index < 100; index++) {
      Contact contact = new Contact("lead" + index + "@mail.ru", "+7000", address);
      Lead lead = new Lead(UUID.randomUUID(), contact, "Company", "NEW");
      storage.add(lead);
    }
    // Добавляем 101 лид
    Contact hundredFirstContact = new Contact("lead101@mail.ru", "+7001", address);
    UUID hundredFirstUuid = UUID.randomUUID();
    Lead hundredFirstLead = new Lead(hundredFirstUuid, hundredFirstContact, "Company", "NEW");
    assertThatThrownBy(() -> storage.add(hundredFirstLead))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Storage is full");
  }

  @Test
  void shouldReturnOnlyAddedLeadsWhenFindAllCalled() {
    Address firstAddress = new Address("San Francisco", "123 Main St", "94105");
    Address differentAddress = new Address("Los Angeles", "124 Main St", "95706");
    Contact firstContact = new Contact("john@example.com", "+7 954 685 23 65", firstAddress);
    Contact differentContact = new Contact("johnr@example.com", "+7 963 685 03 95",
        differentAddress);
    LeadStorage storage = new LeadStorage();
    Lead firstLead = new Lead(UUID.randomUUID(), firstContact, "Test", "NEW");
    Lead secondLead = new Lead(UUID.randomUUID(), differentContact, "Tech", "NEW");
    storage.add(firstLead);
    storage.add(secondLead);
    Lead[] result = storage.findAll();
    assertThat(result).hasSize(2);
    assertThat(result).containsExactly(firstLead, secondLead);
  }
}
