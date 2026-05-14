# Проект "Backend-Core"

[![Java CI with Checkstyle and Coverage](https://github.com/EchoesOfTime/backend-core/actions/workflows/ci.yml/badge.svg)](https://github.com/EchoesOfTime/backend-core/actions/workflows/ci.yml)

# Сравнение: new внутри vs DI через конструктор

## BAD: new InMemoryLeadRepository() внутри класса

```java
public class LeadService {
    // Тесная связанность!
    private final LeadRepository repository = new InMemoryLeadRepository();
}
```
### Проблемы:
- Невозможно подставить mock в тестах
- Невозможно заменить на PostgreSQL без изменения кода
- Скрытая зависимость — не видно, что нужно для работы

## GOOD: DI через конструктор
```java
public class LeadService {
    private final LeadRepository repository;

    public LeadService(LeadRepository repository) {
        this.repository = repository;
    }
}
```

### Преимущества:
- В тестах передаём mock(LeadRepository.class)
- В production передаём InMemoryLeadRepository
- В будущем передаём JpaLeadRepository (Sprint 7)
- Зависимость явная — видно в конструкторе

Метрики сравнения:

| Критерий             | BCORE-12 (println)    | BCORE-13 (JTE)                              |
|----------------------|-----------------------|---------------------------------------------|
| Строк Java кода      | ~52                   | ~29                                         |
| Смешивание Java/HTML | Да (нечитаемо)        | Нет (разделены)                             |
| XSS защита           | Ручная (легко забыть) | Автоматическая                              |
| Type-safety          | Нет                   | Да (compile error если параметр не передан) |
