package ru.mentee.power.crm.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mentee.power.crm.domain.Address;
import ru.mentee.power.crm.domain.Contact;
import ru.mentee.power.crm.model.Lead;

class LeadRepositoryTest {
  private LeadRepository repository;

  @BeforeEach
  void setUp() {
    repository = new LeadRepository();
  }

  @Test
  @DisplayName("Должен сохранить и найти лид по ID")
  void shouldSaveAndFindLeadByIdWhenLeadSaved() {
    Lead lead = new Lead("1", "test@mail.com", "+7123456", "ООО Comp", "NEW");
    repository.save(lead);

    Lead foundLead = repository.findById("1");

    assertThat(foundLead).isNotNull();
    assertThat(foundLead.id()).isEqualTo("1");
    assertThat(foundLead.email()).isEqualTo("test@mail.com");
  }

  @Test
  @DisplayName("Должен вернуть null при поиске несуществующего ID")
  void shouldReturnNullWhenLeadNotFound() {
    Lead foundLead = repository.findById("unknown-id");

    assertThat(foundLead).isNull();
  }

  @Test
  @DisplayName("Должен вернуть список всех сохраненных лидов")
  void shouldReturnAllLeadsWhenMultipleLeadsSaved() {
    Lead lead1 = new Lead("1", "test@mail1.com", "+7123456", "ООО Comp1", "NEW");
    Lead lead2 = new Lead("2", "test@mail2.com", "+7789123", "ООО Comp2", "NEW");
    Lead lead3 = new Lead("3", "test@mail3.com", "+7456789", "ООО Comp3", "NEW");
    repository.save(lead1);
    repository.save(lead2);
    repository.save(lead3);

    List<Lead> allLead = repository.findAll();

    assertThat(allLead).hasSize(3).containsExactlyInAnyOrder(lead1, lead2, lead3);
  }

  @Test
  @DisplayName("Должен удалить лид по ID")
  void shouldDeleteLeadWhenLeadExists() {
    Lead lead = new Lead("1", "test@mail.com", "+7123456", "ООО Comp", "NEW");
    repository.save(lead);

    assertThat(repository.findById("1")).isNotNull();
    assertThat(repository.size()).isEqualTo(1);

    repository.delete("1");

    assertThat(repository.findById("1")).isNull();
    assertThat(repository.size()).isZero();
  }

  @Test
  @DisplayName("Должен перезаписать лид при сохранении с существующим ID")
  void shouldOverwriteLeadWhenSaveWithSameId() {
    Lead lead1 = new Lead("1", "original@mail.com", "+11111111", "ООО Comp", "NEW");
    repository.save(lead1);

    Lead lead2 = new Lead("1", "updated@mail.com", "+22222222", "ООО Comp", "NEW");
    repository.save(lead2);

    Lead foundLead = repository.findById("1");

    assertThat(foundLead).isNotNull();
    assertThat(foundLead.email()).isEqualTo("updated@mail.com");
    assertThat(repository.size()).isEqualTo(1);
  }

  @Test
  void shouldFindFasterWithMapThanWithListFilter() {
    // Given: Создать 1000 лидов
    List<Lead> leadList = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      UUID id = UUID.randomUUID();
      Contact contact = new Contact("email" + i + "@test.com", "+7" + i,
          new Address("City" + i, "Street" + i, "ZIP" + i)
      );
      Lead lead = new Lead(id.toString(), contact.email(), contact.phone(), "Company" + i, "NEW");
      repository.save(lead);
      leadList.add(lead);
    }

    String targetId = "lead-500";  // Средний элемент

    // When: Поиск через Map
    long mapStart = System.nanoTime();
    Lead foundInMap = repository.findById(targetId);
    long mapDuration = System.nanoTime() - mapStart;

    // When: Поиск через List.stream().filter()
    long listStart = System.nanoTime();
    Lead foundInList = leadList.stream()
        .filter(lead -> lead.id().equals(targetId))
        .findFirst()
        .orElse(null);
    long listDuration = System.nanoTime() - listStart;

    // Then: Map должен быть минимум в 10 раз быстрее
    assertThat(foundInMap).isEqualTo(foundInList);
    assertThat(listDuration).isGreaterThan(mapDuration * 10);

    System.out.println("Map поиск: " + mapDuration + " ns");
    System.out.println("List поиск: " + listDuration + " ns");
    System.out.println("Ускорение: " + (listDuration / mapDuration) + "x");
  }

  @Test
  @DisplayName("Должен сохранить оба лида с одинаковыми контактами - демонстрация проблемы")
  void shouldSaveBothLeadsEvenWithSameEmailAndPhoneBecauseRepositoryDoesNotCheckBusinessRules() {
    // Given: два лида с разными UUID но одинаковыми контактами
    Lead originalLead = new Lead("1", "test@mail.com", "+7123456", "ООО Comp", "NEW");
    Lead duplicateLead = new Lead("2", "test@mail.com", "+7123456", "ООО Comp", "NEW");

    // When: сохраняем оба
    repository.save(originalLead);
    repository.save(duplicateLead);

    // Then: Repository сохранил оба (это технически правильно!)
    assertThat(repository.size()).isEqualTo(2);

    // But: Бизнес недоволен — в CRM два контакта на одного человека
    // Решение: Service Layer в Sprint 5 будет проверять бизнес-правила
    // перед вызовом repository.save()
  }
}