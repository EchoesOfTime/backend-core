package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ContactTest {

  @Test
  void shouldCreateContactWhenValidData() {
    Contact contact = new Contact("John", "Doe", "john@example.com");
    String firstName = contact.firstName();
    String lastName = contact.lastName();
    String email = contact.email();
    assertThat(firstName).isEqualTo("John");
    assertThat(lastName).isEqualTo("Doe");
    assertThat(email).isEqualTo("john@example.com");
  }

  @Test
  void shouldBeEqualWhenSameData() {
    Contact firstContact = new Contact("John", "Doe", "john@example.com");
    Contact secondContact = new Contact("John", "Doe", "john@example.com");
    assertThat(firstContact).isEqualTo(secondContact);
    assertThat(firstContact.hashCode()).isEqualTo(secondContact.hashCode());
  }

  @Test
  void shouldNotBeEqualWhenDifferentData() {
    Contact firstContact = new Contact("John", "Doe", "john@example.com");
    Contact differentContact = new Contact("Jack", "Cook", "jackc@example.com");
    assertThat(firstContact).isNotEqualTo(differentContact);
  }
}