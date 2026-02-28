package ru.mentee.power.crm.storage;

import ru.mentee.power.crm.domain.Lead;

public class LeadStorage {
  private Lead[] leads = new Lead[100];

  public boolean add(Lead lead) {
    for (int index = 0; index < leads.length; index++) {
      if (leads[index] != null && leads[index].getEmail().equals(lead.getEmail())) {
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
}
