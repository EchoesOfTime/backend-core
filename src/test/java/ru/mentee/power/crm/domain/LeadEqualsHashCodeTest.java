package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class LeadEqualsHashCodeTest {

  @Test
  void shouldBeReflexiveWhenEqualsCalledOnSameObject() {
    Lead lead = new Lead("1", "ivan@mail.ru", "+7 489 851 12 34", "TestCorp", "NEW");
    assertThat(lead).isEqualTo(lead);
  }

  @Test
  void shouldBeSymmetricWhenEqualsCalledOnTwoObjects() {
    Lead lead = new Lead("1", "ivan@mail.ru", "+7 489 851 12 34", "TestCorp", "NEW");
    Lead lead1 = new Lead("1", "ivan@mail.ru", "+7 489 851 12 34", "TestCorp", "NEW");
    assertThat(lead).isEqualTo(lead1);
    assertThat(lead1).isEqualTo(lead);
  }

  @Test
  void shouldBeTransitiveWhenEqualsChainOfThreeObjects() {
    Lead lead = new Lead("1", "ivan@mail.ru", "+7 489 851 12 34", "TestCorp", "NEW");
    Lead lead1 = new Lead("1", "ivan@mail.ru", "+7 489 851 12 34", "TestCorp", "NEW");
    Lead lead2 = new Lead("1", "ivan@mail.ru", "+7 489 851 12 34", "TestCorp", "NEW");
    assertThat(lead).isEqualTo(lead1);
    assertThat(lead1).isEqualTo(lead2);
    assertThat(lead2).isEqualTo(lead);
  }

  @Test
  void shouldBeConsistentWhenEqualsCalledMultipleTimes() {
    Lead lead = new Lead("1", "ivan@mail.ru", "+7 489 851 12 34", "TestCorp", "NEW");
    Lead lead1 = new Lead("1", "ivan@mail.ru", "+7 489 851 12 34", "TestCorp", "NEW");
    assertThat(lead).isEqualTo(lead1);
    assertThat(lead).isEqualTo(lead1);
    assertThat(lead).isEqualTo(lead1);
  }

  @Test
  void shouldReturnFalseWhenEqualsComparedWithNull() {
    Lead lead = new Lead("1", "ivan@mail.ru", "+7 489 851 12 34", "TestCorp", "NEW");
    assertThat(lead).isNotEqualTo(null);
  }

  @Test
  void shouldHaveSameHashCodeWhenObjectsAreEqual() {
    Lead lead = new Lead("1", "ivan@mail.ru", "+7 489 851 12 34", "TestCorp", "NEW");
    Lead lead1 = new Lead("1", "ivan@mail.ru", "+7 489 851 12 34", "TestCorp", "NEW");
    assertThat(lead).isEqualTo(lead1);
    assertThat(lead.hashCode()).isEqualTo(lead1.hashCode());
  }

  @Test
  void shouldWorkInHashMapWhenLeadUsedAsKey() {
    Lead keyLead = new Lead("1", "ivan@mail.ru", "+7 489 851 12 34", "TestCorp", "NEW");
    Lead lookupLead = new Lead("1", "ivan@mail.ru",
        "+7 489 851 12 34", "TestCorp", "NEW");
    Map<Lead, String> map = new HashMap<>();
    map.put(keyLead, "CONTACTED");
    String string = map.get(lookupLead);
    assertThat(string).isEqualTo("CONTACTED");
  }

  @Test
  void shouldNotBeEqualWhenIdsAreDifferent() {
    Lead firstLead = new Lead("1", "ivan@mail.ru", "+7 489 851 12 34", "TestCorp", "NEW");
    Lead differentLead = new Lead("2", "ivan@mail.ru",
        "+7 489 851 12 34", "TestCorp", "NEW");
    assertThat(firstLead).isNotEqualTo(differentLead);
  }
}