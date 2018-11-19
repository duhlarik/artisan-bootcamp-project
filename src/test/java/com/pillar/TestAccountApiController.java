package com.pillar;

import com.pillar.account.Account;
import com.pillar.account.AccountRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class TestAccountApiController {
    private Account account;

    @Before
    public void setup() {
        final AccountRepository repository = mock(AccountRepository.class);
        final AccountApiController controller = new AccountApiController(repository);

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

    @Test
    public void accountHasCardholder() {
        assertNotNull(account.getCardholder());
    }

    @Test
    public void cardholderHasName() {
        assertEquals("Steve Goliath", account.getCardholder().getName());
    }

    @Test
    public void cardholderHasSsn() {
        assertEquals("123-45-6789", account.getCardholder().getSsn());
    }
}
