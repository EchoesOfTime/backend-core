package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ContactTest {

  @Test
  void shouldCreateContactWhenValidData() {
    Address address = new Address("San Francisco", "123 Main St", "94105");
    Contact contact = new Contact("john@example.com", "+7 954 685 23 65", address);

    // Then
    assertThat(contact.address()).isEqualTo(address);
    assertThat(contact.address().city()).isEqualTo("San Francisco");
  }

  @Test
  void shouldDelegateToAddressWhenAccessingCity() {
    Address address = new Address("San Francisco", "123 Main St", "94105");
    Contact contact = new Contact("john@example.com", "+7 954 685 23 65", address);

    // Then
    assertThat(contact.address().city()).isEqualTo(address.city());
    assertThat(contact.address().street()).isEqualTo(address.street());
    assertThat(contact.address().zip()).isEqualTo(address.zip());
  }

  @Test
  void shouldThrowExceptionWhenAddressIsNull() {
    assertThatThrownBy(() -> new Contact("john@example.com", "+7 954 685 23 65", null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Address cannot be null");
  }
}