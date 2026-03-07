package ru.mentee.power.crm.domain;

import java.util.Objects;
import java.util.UUID;

public record Lead(UUID id, Contact contact, String company, String status) {

  public Lead {
    if (id == null) {
      throw new IllegalArgumentException("id cannot be null");
    }
    if (contact == null) {
      throw new IllegalArgumentException("Contact cannot be null");
    }
    if (company == null || company.isBlank()) {
      throw new IllegalArgumentException("Company cannot be blank");
    }
    if (status == null || status.isBlank()) {
      throw new IllegalArgumentException("Status cannot be blank");
    }
    if (!status.equals("NEW") && !status.equals("QUALIFIED") && !status.equals("CONVERTED")) {
      throw new IllegalArgumentException("Status must be one of: NEW, QUALIFIED, CONVERTED.   Got: "
          + status);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Lead lead = (Lead) o;
    return Objects.equals(id, lead.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}