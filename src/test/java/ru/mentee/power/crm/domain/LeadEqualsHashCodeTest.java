package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class LeadEqualsHashCodeTest {

  @Test
  void shouldBeReflexiveWhenEqualsCalledOnSameObject() {
    Lead lead = new Lead(UUID.randomUUID(), "ivan@mail.ru", "+7 489 851 12 34",
        "TestCorp", "NEW");
    assertThat(lead).isEqualTo(lead);
  }

  @Test
  void shouldBeSymmetricWhenEqualsCalledOnTwoObjects() {
    UUID uuid = UUID.randomUUID();
    Lead lead = new Lead(uuid, "ivan@mail.ru", "+7 489 851 12 34", "TestCorp", "NEW");
    Lead lead1 = new Lead(uuid, "ivan@mail.ru", "+7 489 851 12 34", "TestCorp", "NEW");
    assertThat(lead).isEqualTo(lead1);
    assertThat(lead1).isEqualTo(lead);
  }

  @Test
  void shouldBeTransitiveWhenEqualsChainOfThreeObjects() {
    UUID uuid = UUID.randomUUID();
    Lead lead = new Lead(uuid, "ivan@mail.ru", "+7 489 851 12 34", "TestCorp", "NEW");
    Lead lead1 = new Lead(uuid, "ivan@mail.ru", "+7 489 851 12 34", "TestCorp", "NEW");
    Lead lead2 = new Lead(uuid, "ivan@mail.ru", "+7 489 851 12 34", "TestCorp", "NEW");
    assertThat(lead).isEqualTo(lead1);
    assertThat(lead1).isEqualTo(lead2);
    assertThat(lead2).isEqualTo(lead);
  }

  @Test
  void shouldBeConsistentWhenEqualsCalledMultipleTimes() {
    UUID uuid = UUID.randomUUID();
    Lead lead = new Lead(uuid, "ivan@mail.ru", "+7 489 851 12 34", "TestCorp", "NEW");
    Lead lead1 = new Lead(uuid, "ivan@mail.ru", "+7 489 851 12 34", "TestCorp", "NEW");
    assertThat(lead).isEqualTo(lead1);
    assertThat(lead).isEqualTo(lead1);
    assertThat(lead).isEqualTo(lead1);
  }

  @Test
  void shouldReturnFalseWhenEqualsComparedWithNull() {
    Lead lead = new Lead(UUID.randomUUID(), "ivan@mail.ru", "+7 489 851 12 34",
        "TestCorp", "NEW");
    assertThat(lead).isNotEqualTo(null);
  }

  @Test
  void shouldHaveSameHashCodeWhenObjectsAreEqual() {
    UUID uuid = UUID.randomUUID();
    Lead lead = new Lead(uuid, "ivan@mail.ru", "+7 489 851 12 34", "TestCorp", "NEW");
    Lead lead1 = new Lead(uuid, "ivan@mail.ru", "+7 489 851 12 34", "TestCorp", "NEW");
    assertThat(lead).isEqualTo(lead1);
    assertThat(lead.hashCode()).isEqualTo(lead1.hashCode());
  }

  @Test
  void shouldWorkInHashMapWhenLeadUsedAsKey() {
    UUID uuid = UUID.randomUUID();
    Lead keyLead = new Lead(uuid, "ivan@mail.ru", "+7 489 851 12 34", "TestCorp", "NEW");
    Lead lookupLead = new Lead(uuid, "ivan@mail.ru",
        "+7 489 851 12 34", "TestCorp", "NEW");
    Map<Lead, String> map = new HashMap<>();
    map.put(keyLead, "CONTACTED");
    String string = map.get(lookupLead);
    assertThat(string).isEqualTo("CONTACTED");
  }

  @Test
  void shouldNotBeEqualWhenIdsAreDifferent() {
    Lead firstLead = new Lead(UUID.randomUUID(), "ivan@mail.ru", "+7 489 851 12 34",
        "TestCorp", "NEW");
    Lead differentLead = new Lead(UUID.randomUUID(), "ivan@mail.ru",
        "+7 489 851 12 34", "TestCorp", "NEW");
    assertThat(firstLead).isNotEqualTo(differentLead);
  }
}