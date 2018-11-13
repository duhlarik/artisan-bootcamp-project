package com.pillar;

import com.pillar.merchant.Merchant;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestMerchant {

    private Merchant merchant1;

    @Before
    public void setUp() {
        merchant1 = new Merchant(1, "Test Merchant");
    }

    @Test
    public void twoMerchantsAreEquivalentIfTheirIdsAreTheSame() {
        Merchant merchant2 = new Merchant(1, "Test Merchant2");

        assertEquals(merchant1, merchant2);
    }

    @Test
    public void twoMerchantsAreNotEqualIfTheirIdsAreDifferent() {
        Merchant merchant2 = new Merchant(2, "Test Merchant2");

        assertNotEquals(merchant1, merchant2);
    }

    @Test
    public void aMerchantIsNotEqualToNull() {
        assertNotEquals(merchant1, null);
    }

    @Test
    public void aMerchantIsEqualToItself() {
        assertEquals(merchant1, merchant1);
    }

    @Test
    public void aMerchantIsNotEqualToAnotherObjectOfADifferentClass() {
        assertNotEquals(merchant1, new ArrayList());
    }
}
