package ru.mentee.power.crm.domain;

import java.util.UUID;

public record Customer(UUID id, Contact contact, Address billingAddress, String loyaltyTier) {

  public Customer {
    if (id == null) {
      throw new IllegalArgumentException("id cannot be null");
    }
    if (contact == null) {
      throw new IllegalArgumentException("Contact cannot be null");
    }
    if (billingAddress == null) {
      throw new IllegalArgumentException("Billing address cannot be null");
    }
    if (loyaltyTier == null || loyaltyTier.isBlank()) {
      throw new IllegalArgumentException("Loyalty tier cannot be blank");
    }
    if (!loyaltyTier.equals("BRONZE") && !loyaltyTier.equals("SILVER")
        && !loyaltyTier.equals("GOLD")) {
      throw new IllegalArgumentException("Loyalty tier must be one of: BRONZE, SILVER, GOLD. Got: "
          + loyaltyTier);
    }
  }
}