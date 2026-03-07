package ru.mentee.power.crm.storage;

import java.util.UUID;

import ru.mentee.power.crm.domain.Lead;

public class LeadStorage {
  private Lead[] leads = new Lead[100];

  public boolean add(Lead lead) {
    for (int index = 0; index < leads.length; index++) {
      if (leads[index] != null && leads[index].id().equals(lead.id())) {
        return false;
      }
    }
    for (int index = 0; index < leads.length; index++) {
      if (leads[index] == null) {
        leads[index] = lead;
        return true;
      }
    }
    throw new IllegalStateException("Storage is full, cannot add more leads");
  }

  public Lead[] findAll() {
    int count = 0;
    for (int i = 0; i < leads.length; i++) {
      if (leads[i] != null) {
        count++;
      }
    }
    System.out.println("Лидов в хранилище: " + count);

    Lead[] result = new Lead[count];
    int resultIndex = 0;
    for (int index = 0; index < leads.length; index++) {
      if (leads[index] != null) {
        result[resultIndex++] = leads[index];
      }
    }
    return result;
  }

  public int size() {
    int count = 0;
    for (int i = 0; i < leads.length; i++) {
      if (leads[i] != null) {
        count++;
      }
    }
    return count;
  }

  public Lead findById(UUID id) {
    if (id == null) {
      throw new NullPointerException("Should not be null");
    }

    for (int i = 0; i < leads.length; i++) {
      if (leads[i] != null && leads[i].id().equals(id)) {
        return leads[i];
      }
    }
    return null;
  }
}
