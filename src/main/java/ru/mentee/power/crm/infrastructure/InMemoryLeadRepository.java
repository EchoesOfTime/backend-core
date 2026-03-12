package ru.mentee.power.crm.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ru.mentee.power.crm.domain.Lead;
import ru.mentee.power.crm.domain.Repository;

public class InMemoryLeadRepository implements Repository<Lead> {
  private  final List<Lead> storage = new ArrayList<>();

  @Override
  public void add(Lead entity) {
    if (!storage.contains(entity) && entity != null) {
      storage.add(entity);
    }
  }

  @Override
  public void remove(UUID id) {
    storage.removeIf(lead -> lead.id().equals(id));
  }

  @Override
  public Optional<Lead> findById(UUID id) {
    return storage.stream().filter(lead -> lead.id().equals(id)).findFirst();
  }

  @Override
  public List<Lead> findAll() {
    return new ArrayList<>(storage);
  }
}