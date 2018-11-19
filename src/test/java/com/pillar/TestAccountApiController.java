package com.pillar;

import com.pillar.account.Account;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestAccountApiController {
    private Account account;

    @Before
    public void setup() {
        final AccountApiController controller = new AccountApiController();

        final Map<String, String> params = new HashMap<>();
        params.put("cardHolderName", "Steve Goliath");
        params.put("ssn", "123-45-6789");
        params.put("businessName", "Target");

        account = controller.create(params).getBody();
    }

    @Test
    public void createdAccountHas10000CreditLimit() {
        assertEquals(10000, account.getCreditLimit());
    }

    @Test
    public void createdAccountHasCardNumber() {
        final String cardNumber = account.getCardNumber();
        assertNotNull(cardNumber);
        assertEquals(cardNumber.length(), 36);
    }

    @Test
    public void createdAccountIsActive(){
        assertTrue(account.isActive());
    }
}
