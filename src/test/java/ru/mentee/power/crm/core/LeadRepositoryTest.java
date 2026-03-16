package ru.mentee.power.crm.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.domain.Lead;

class LeadRepositoryTest {
  Address address = new Address("San Francisco", "123 Main St", "94105");
  Contact contact = new Contact("john@example.com", "+7 954 685 23 65", address);
  Lead lead = new Lead(UUID.randomUUID(), contact, "Test", "NEW");

  @Test
  @DisplayName("Should automatically deduplicate leads by id")
  void shouldDeduplicateLeadsById() {
    LeadRepository leadRepository = new LeadRepository();
    boolean firstAdd = leadRepository.add(lead);
    boolean secondAdd = leadRepository.add(lead);

    assertThat(leadRepository.size()).isEqualTo(1);
    assertThat(firstAdd).isTrue();
    assertThat(secondAdd).isFalse();
  }

  @Test
  @DisplayName("Should allow different leads with different ids")
  void shouldAllowDifferentLeads() {
    LeadRepository leadRepository = new LeadRepository();

    Lead lead1 = new Lead(UUID.randomUUID(), contact, "Test1", "NEW");
    Lead lead2 = new Lead(UUID.randomUUID(), contact, "Test2", "QUALIFIED");

    boolean add1 = leadRepository.add(lead1);
    boolean add2 = leadRepository.add(lead2);

    assertThat(leadRepository.size()).isEqualTo(2);
    assertThat(add1).isTrue();
    assertThat(add2).isTrue();
  }

  @Test
  @DisplayName("Should find existing lead through contains")
  void shouldFindExistingLead() {
    LeadRepository leadRepository = new LeadRepository();
    leadRepository.add(lead);

    assertThat(leadRepository.contains(lead)).isTrue();
  }

  @Test
  @DisplayName("Should return unmodifiable set from findAll")
  void shouldReturnUnmodifiableSet() {
    LeadRepository leadRepository = new LeadRepository();
    leadRepository.add(lead);

    var result = leadRepository.findAll();

    assertThatThrownBy(() -> result.clear()).isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  @DisplayName("Should perform contains() faster than ArrayList")
  void shouldPerformFasterThanArrayList() {

    int numberOfLeads = 10000;
    int numberOfContainsChecks = 1000;

    List<Lead> arrayList = new ArrayList<>();
    Set<Lead> hashSet = new HashSet<>();

    Contact testContact = new Contact("test@example.com", "+7 123 456 78 90",
        new Address("City", "Street", "12345"));

    for (int i = 0; i < numberOfLeads; i++) {
      Lead lead = new Lead(UUID.randomUUID(), testContact, "Company" + i, "NEW");
      arrayList.add(lead);
      hashSet.add(lead);
    }

    Lead searchLead = new Lead(UUID.randomUUID(), testContact, "SearchCompany", "NEW");

    long arrayListStart = System.nanoTime();
    for (int i = 0; i < numberOfContainsChecks; i++) {
      arrayList.contains(searchLead);
    }
    long arrayListDuration = System.nanoTime() - arrayListStart;

    long hashSetStart = System.nanoTime();
    for (int i = 0; i < numberOfContainsChecks; i++) {
      hashSet.contains(searchLead);
    }
    long hashSetDuration = System.nanoTime() - hashSetStart;

    System.out.println("ArrayList contains() time: " + arrayListDuration / 1_000_000.0 + " ms");
    System.out.println("HashSet contains() time: " + hashSetDuration / 1_000_000.0 + " ms");
    System.out.println("Performance ratio (ArrayList/HashSet): "
        + (double) arrayListDuration / hashSetDuration);

    assertThat((double) arrayListDuration / hashSetDuration).isGreaterThan(100);

    assertThat(arrayListDuration / 1_000_000.0).isGreaterThan(100);
  }
}
