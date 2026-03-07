package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class LeadTest {

  @Test
  void shouldCreateLeadWhenValidData() {
    Address address = new Address("San Francisco", "123 Main St", "94105");
    Contact contact = new Contact("john@example.com", "+7 954 685 23 65", address);
    Lead lead = new Lead(UUID.randomUUID(), contact, "Test", "NEW");

    // Then
    assertThat(lead.contact()).isEqualTo(contact);
  }

  @Test
  void shouldAccessEmailThroughDelegationWhenLeadCreated() {
    Address address = new Address("San Francisco", "123 Main St", "94105");
    Contact contact = new Contact("john@example.com", "+7 954 685 23 65", address);
    Lead lead = new Lead(UUID.randomUUID(), contact, "Test", "NEW");

    // Then
    assertThat(lead.contact().email()).isEqualTo("john@example.com");
    assertThat(lead.contact().address().city()).isEqualTo("San Francisco");

  }

  @Test
  void shouldBeEqualWhenSameIdButDifferentContact() {
    Address address = new Address("San Francisco", "123 Main St", "94105");
    Contact contact1 = new Contact("john@example.com", "+7 954 685 23 65", address);
    Contact contact2 = new Contact("1john@example.com", "+7 954 685 23 64", address);
    UUID id = UUID.randomUUID();
    Lead lead1 = new Lead(id, contact1, "Test", "NEW");
    Lead lead2 = new Lead(id, contact2, "Test", "NEW");

    // Then
    assertThat(lead1).isEqualTo(lead2);
  }

  @Test
  void shouldThrowExceptionWhenContactIsNull() {
    assertThatThrownBy(() -> new Lead(UUID.randomUUID(), null, "Test", "NEW"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Contact cannot be null");
  }

  @Test
  void shouldThrowExceptionWhenInvalidStatus() {
    Address address = new Address("San Francisco", "123 Main St", "94105");
    Contact contact = new Contact("john@example.com", "+7 954 685 23 65", address);

    // Then
    assertThatThrownBy(() -> new Lead(UUID.randomUUID(), contact, "Test", "INVALID"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Status must be one of: NEW, QUALIFIED, CONVERTED");
  }

  @Test
  void shouldDemonstrateThreeLevelCompositionWhenAccessingCity() {
    Address address = new Address("San Francisco", "123 Main St", "94105");
    Contact contact = new Contact("john@example.com", "+7 954 685 23 65", address);
    Lead lead = new Lead(UUID.randomUUID(), contact, "Test", "NEW");

    // When
    String city = lead.contact().address().city();

    // Then
    assertThat(city).isEqualTo("San Francisco");
  }
}