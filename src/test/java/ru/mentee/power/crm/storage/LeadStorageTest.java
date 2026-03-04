package ru.mentee.power.crm.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Lead;

class LeadStorageTest {

  @Test
  void shouldAddLeadWhenLeadIsUnique() {
    LeadStorage storage = new LeadStorage();
    Lead uniqueLead = new Lead(UUID.randomUUID(), "ivan@mail.ru", "+7123", "TechCorp", "NEW");
    boolean added = storage.add(uniqueLead);
    assertThat(added).isTrue();
    assertThat(storage.size()).isEqualTo(1);
    assertThat(storage.findAll()).containsExactly(uniqueLead);
  }

  @Test
  void shouldRejectDuplicateWhenEmailAlreadyExists() {
    LeadStorage storage = new LeadStorage();
    Lead existingLead = new Lead(UUID.randomUUID(), "ivan@mail.ru", "+7123", "TechCorp", "NEW");
    Lead duplicateLead = new Lead(UUID.randomUUID(), "ivan@mail.ru", "+7456", "Other", "NEW");
    storage.add(existingLead);
    boolean added = storage.add(duplicateLead);
    assertThat(added).isFalse();
    assertThat(storage.size()).isEqualTo(1);
    assertThat(storage.findAll()).containsExactly(existingLead);
  }

  @Test
  void shouldThrowExceptionWhenStorageIsFull() {
    LeadStorage storage = new LeadStorage();
    for (int index = 0; index < 100; index++) {
      UUID uuid = UUID.randomUUID();
      storage.add(new Lead(uuid, "lead" + index + "@mail.ru", "+7000", "Company", "NEW"));
    }
    UUID hundredFirstUuid = UUID.randomUUID();
    Lead hundredFirstLead = new Lead(hundredFirstUuid, "lead101@mail.ru", "+7001",
        "Company", "NEW");
    assertThatThrownBy(() -> storage.add(hundredFirstLead))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Storage is full");
  }

  @Test
  void shouldReturnOnlyAddedLeadsWhenFindAllCalled() {
    LeadStorage storage = new LeadStorage();
    Lead firstLead = new Lead(UUID.randomUUID(), "ivan@mail.ru", "+7123", "TechCorp", "NEW");
    Lead secondLead = new Lead(UUID.randomUUID(), "maria@startup.io", "+7456", "StartupLab", "NEW");
    storage.add(firstLead);
    storage.add(secondLead);
    Lead[] result = storage.findAll();
    assertThat(result).hasSize(2);
    assertThat(result).containsExactly(firstLead, secondLead);
  }
}
