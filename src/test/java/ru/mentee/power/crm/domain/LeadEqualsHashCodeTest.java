package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class LeadEqualsHashCodeTest {

  @Test
  void shouldBeReflexiveWhenEqualsCalledOnSameObject() {
    Address address = new Address("San Francisco", "123 Main St", "94105");
    Contact contact = new Contact("john@example.com", "+7 954 685 23 65", address);
    Lead lead = new Lead(UUID.randomUUID(), contact, "Test", "NEW");

    assertThat(lead).isEqualTo(lead);
  }

  @Test
  void shouldBeSymmetricWhenEqualsCalledOnTwoObjects() {
    Address address = new Address("San Francisco", "123 Main St", "94105");
    Contact contact = new Contact("john@example.com", "+7 954 685 23 65", address);
    UUID uuid = UUID.randomUUID();
    Lead lead = new Lead(uuid, contact, "Test", "NEW");
    Lead lead1 = new Lead(uuid, contact, "Test", "NEW");

    assertThat(lead).isEqualTo(lead1);
    assertThat(lead1).isEqualTo(lead);
  }

  @Test
  void shouldBeTransitiveWhenEqualsChainOfThreeObjects() {
    Address address = new Address("San Francisco", "123 Main St", "94105");
    Contact contact = new Contact("john@example.com", "+7 954 685 23 65", address);
    UUID uuid = UUID.randomUUID();
    Lead lead = new Lead(uuid, contact, "Test", "NEW");
    Lead lead1 = new Lead(uuid, contact, "Test", "NEW");
    Lead lead2 = new Lead(uuid, contact, "Test", "NEW");

    assertThat(lead).isEqualTo(lead1);
    assertThat(lead1).isEqualTo(lead2);
    assertThat(lead2).isEqualTo(lead);
  }

  @Test
  void shouldReturnFalseWhenEqualsComparedWithNull() {
    Address address = new Address("San Francisco", "123 Main St", "94105");
    Contact contact = new Contact("john@example.com", "+7 954 685 23 65", address);
    Lead lead = new Lead(UUID.randomUUID(), contact, "Test", "NEW");
    assertThat(lead).isNotEqualTo(null);
  }

  @Test
  void shouldHaveSameHashCodeWhenObjectsAreEqual() {
    Address address = new Address("San Francisco", "123 Main St", "94105");
    Contact contact = new Contact("john@example.com", "+7 954 685 23 65", address);
    UUID uuid = UUID.randomUUID();
    Lead lead = new Lead(uuid, contact, "Test", "NEW");
    Lead lead1 = new Lead(uuid, contact, "Test", "NEW");
    assertThat(lead).isEqualTo(lead1);
    assertThat(lead.hashCode()).isEqualTo(lead1.hashCode());
  }

  @Test
  void shouldWorkInHashMapWhenLeadUsedAsKey() {
    Address address = new Address("San Francisco", "123 Main St", "94105");
    Contact contact = new Contact("john@example.com", "+7 954 685 23 65", address);
    UUID uuid = UUID.randomUUID();
    Lead keyLead = new Lead(uuid, contact, "Test", "NEW");
    Lead lookupLead = new Lead(uuid, contact, "Test", "NEW");
    Map<Lead, String> map = new HashMap<>();
    map.put(keyLead, "CONTACTED");
    String string = map.get(lookupLead);
    assertThat(string).isEqualTo("CONTACTED");
  }

  @Test
  void shouldNotBeEqualWhenIdsAreDifferent() {
    Address firstAddress = new Address("San Francisco", "123 Main St", "94105");
    Address differentAddress = new Address("Los Angeles", "124 Main St", "94106");

    Contact firstContact = new Contact("john@example.com", "+7 954 685 23 65", firstAddress);
    Contact differentContact = new Contact("johnr@example.com", "+7 963 685 03 95",
        differentAddress);

    Lead firstLead = new Lead(UUID.randomUUID(), firstContact, "Test", "NEW");
    Lead differentLead = new Lead(UUID.randomUUID(), differentContact, "Tech", "NEW");
    assertThat(firstLead).isNotEqualTo(differentLead);
  }
}