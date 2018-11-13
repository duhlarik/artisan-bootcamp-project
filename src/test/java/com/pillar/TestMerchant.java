package com.pillar;

import com.pillar.merchant.Merchant;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestMerchant {

    @Test
    public void twoMerchantsAreEquivalentIfTheirIdsAreTheSame() {
        Merchant merchant1 = new Merchant(1, "Test Merchant");
        Merchant merchant2 = new Merchant(1, "Test Merchant2");

        assertEquals(merchant1, merchant2);
    }

    @Test
    public void twoMerchantsAreNotEqualIfTheirIdsAreDifferent() {
        Merchant merchant1 = new Merchant(1, "Test Merchant");
        Merchant merchant2 = new Merchant(2, "Test Merchant2");

        assertNotEquals(merchant1, merchant2);
    }

    @Test
    public void aMerchantIsNotEqualToNull() {
        Merchant merchant = new Merchant(1, "Test Merchant");

        assertNotEquals(merchant, null);
    }

    @Test
    public void aMerchantIsEqualToItself() {
        Merchant merchant = new Merchant(1, "Test Merchant");

        assertEquals(merchant, merchant);
    }

    @Test
    public void aMerchantIsNotEqualToAnotherObjectOfADifferentClass() {
        Merchant merchant = new Merchant(1, "Test Merchant");

        assertNotEquals(merchant, new ArrayList());
    }
}
