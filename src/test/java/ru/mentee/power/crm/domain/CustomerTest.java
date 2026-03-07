package ru.mentee.power.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class CustomerTest {

  @Test
  void shouldReuseContactWhenCreatingCustomer() {
    Address contactaddress = new Address("San Francisco", "123 Main St", "94105");
    Address billingaddress = new Address("Los Angelesss", "124 Main St", "94106");

    Contact contact = new Contact("john@example.com", "+7 954 685 23 65", contactaddress);
    Customer customer = new Customer(UUID.randomUUID(), contact, billingaddress, "BRONZE");

    assertThat(customer.contact().address()).isNotEqualTo(customer.billingAddress());
  }

  @Test
  void shouldDemonstrateContactReuseAcrossLeadAndCustomer() {
    Address contactaddress = new Address("San Francisco", "123 Main St", "94105");
    Address billingaddress = new Address("Los Angelesss", "124 Main St", "94106");

    Contact contact = new Contact("john@example.com", "+7 954 685 23 65", contactaddress);
    Customer customer = new Customer(UUID.randomUUID(), contact, billingaddress, "BRONZE");
    Lead lead = new Lead(UUID.randomUUID(), contact, "Test", "NEW");

    assertThat(customer.contact()).isEqualTo(lead.contact());
  }

  @Test
  void shouldCreateCustomerWithBronzeTier() {
    Address contactAddress = new Address("San Francisco", "123 Main St", "94105");
    Address billingAddress = new Address("Los Angeles", "124 Main St", "94106");
    Contact contact = new Contact("john@example.com", "+7 954 685 23 65", contactAddress);

    Customer customer = new Customer(UUID.randomUUID(), contact, billingAddress, "BRONZE");

    assertThat(customer.loyaltyTier()).isEqualTo("BRONZE");
  }

  @Test
  void shouldCreateCustomerWithSilverTier() {
    Address contactAddress = new Address("San Francisco", "123 Main St", "94105");
    Address billingAddress = new Address("Los Angeles", "124 Main St", "94106");
    Contact contact = new Contact("john@example.com", "+7 954 685 23 65", contactAddress);

    Customer customer = new Customer(UUID.randomUUID(), contact, billingAddress, "SILVER");

    assertThat(customer.loyaltyTier()).isEqualTo("SILVER");
  }

  @Test
  void shouldCreateCustomerWithGoldTier() {
    Address contactAddress = new Address("San Francisco", "123 Main St", "94105");
    Address billingAddress = new Address("Los Angeles", "124 Main St", "94106");
    Contact contact = new Contact("john@example.com", "+7 954 685 23 65", contactAddress);

    Customer customer = new Customer(UUID.randomUUID(), contact, billingAddress, "GOLD");

    assertThat(customer.loyaltyTier()).isEqualTo("GOLD");
  }

  @Test
  void shouldThrowExceptionWhenIdIsNull() {
    Address contactAddress = new Address("San Francisco", "123 Main St", "94105");
    Address billingAddress = new Address("Los Angeles", "124 Main St", "94106");
    Contact contact = new Contact("john@example.com", "+7 954 685 23 65", contactAddress);

    assertThatThrownBy(() -> new Customer(null, contact, billingAddress, "BRONZE"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("id cannot be null");
  }

  @Test
  void shouldThrowExceptionWhenContactIsNull() {
    Address billingAddress = new Address("Los Angeles", "124 Main St", "94106");

    assertThatThrownBy(() -> new Customer(UUID.randomUUID(), null, billingAddress, "BRONZE"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Contact cannot be null");
  }

  @Test
  void shouldThrowExceptionWhenBillingAddressIsNull() {
    Address contactAddress = new Address("San Francisco", "123 Main St", "94105");
    Contact contact = new Contact("john@example.com", "+7 954 685 23 65", contactAddress);

    assertThatThrownBy(() -> new Customer(UUID.randomUUID(), contact, null, "BRONZE"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Billing address cannot be null");
  }
}