package com.pillar;

import com.pillar.merchant.Customer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestCustomer {

    private Customer customer1;

    @Before
    public void setUp() {
        customer1 = new Customer(1, "Test Customer");
    }

    @Test
    public void twoCustomersAreEquivalentIfTheirIdsAreTheSame() {
        Customer customer2 = new Customer(1, "Test Customer2");

        assertEquals(customer1, customer2);
    }

    @Test
    public void twoCustomersAreNotEqualIfTheirIdsAreDifferent() {
        Customer customer2 = new Customer(2, "Test Customer2");

        assertNotEquals(customer1, customer2);
    }

    @Test
    public void aCustomerIsNotEqualToNull() {
        assertNotEquals(customer1, null);
    }

    @Test
    public void aCustomerIsEqualToItself() {
        assertEquals(customer1, customer1);
    }

    @Test
    public void aCustomerIsNotEqualToAnotherObjectOfADifferentClass() {
        assertNotEquals(customer1, new ArrayList());
    }
}
