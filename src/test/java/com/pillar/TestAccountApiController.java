package com.pillar;

import com.pillar.account.Account;
import com.pillar.account.AccountRepository;
import com.pillar.cardholder.Cardholder;
import com.pillar.cardholder.CardholderRepository;
import com.pillar.customer.Customer;
import com.pillar.customer.CustomerRepository;
import com.pillar.transaction.TransactionRecordRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestAccountApiController {
    public static final double DELTA = 0.001;
    private final Cardholder cardholder = new Cardholder(1, "123-45-6789", "Steve Goliath");
    private final Customer customer = new Customer(1, "Target");

    private Account account;
    private AccountApiController controller;

    @Before
    public void setup() {
        final CardholderRepository cardholderRepository = mock(CardholderRepository.class);
        when(cardholderRepository.findBySsn(cardholder.getSsn())).thenReturn(Collections.emptyList());
        when(cardholderRepository.save(any())).thenReturn(cardholder);

        final CustomerRepository customerRepository = mock(CustomerRepository.class);
        when(customerRepository.findByName(customer.getName())).thenReturn(Collections.emptyList());
        when(customerRepository.save(any())).thenReturn(customer);

        final AccountRepository repository = mock(AccountRepository.class);
        when(repository.existsByCustomerIdAndCardholderId(anyInt(), anyInt())).thenReturn(false);
        when(repository.save(any())).thenReturn(new Account(cardholder, customer));

        controller = new AccountApiController(repository, cardholderRepository, customerRepository);
        createAccount();
    }

    private void createAccount() {
        final Map<String, String> params = new HashMap<>();
        params.put(AccountApiController.CARDHOLDER_NAME, cardholder.getName());
        params.put(AccountApiController.CARDHOLDER_SSN, cardholder.getSsn());
        params.put(AccountApiController.BUSINESS_NAME, customer.getName());

        account = controller.create(params).getBody();
    }

    @Test
    public void createdAccountHas10000CreditLimit() {
        assertEquals(10000, account.getCreditLimit());
    }

    @Test
    public void createdAccountHasCardNumber() {
        final String cardNumber = account.getCreditCardNumber();
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
        assertEquals(cardholder.getName(), account.getCardholder().getName());
    }

    @Test
    public void accountHas0TransactionBalanceBeforeAnyTransactionsAreMade() {
        assertEquals(0, account.getTransactionBalance(), DELTA);
    }

    @Test
    public void cardholderHasSsn() {
        assertEquals(cardholder.getSsn(), account.getCardholder().getSsn());
    }
}
