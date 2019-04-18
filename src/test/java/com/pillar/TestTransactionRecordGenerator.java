package com.pillar;

import com.pillar.account.Account;
import com.pillar.cardholder.Cardholder;
import com.pillar.transaction.TransactionRecord;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

import static com.pillar.transaction.TransactionRecord.*;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestTransactionRecordGenerator {

    private static final Instant NOW = Instant.now();
    private static final double AMOUNT = 2.0;
    private static final double CREDIT_LIMIT = 5.0;
    private static final boolean IS_CHARGE = true;
    private static final ArrayList<TransactionRecord> EMPTY_LIST = new ArrayList<>();
    private static final boolean ACTIVE = true;
    private static final String CC_NUMBER = "123";
    private static final int ID = 1;
    public static final Cardholder CARDHOLDER = new Cardholder();
    private static final Account ACCOUNT = new Account();

    @Test
    public void givenTransactionOf100AndNoExistingTransactionsAndCreditLimitOf1000ReturnTransactionRecordWithAmount100() {
        TransactionRecordGenerator generator = new TransactionRecordGenerator(100, EMPTY_LIST, NOW, IS_CHARGE, ACCOUNT);

        TransactionRecord transactionRecord = generator.generate();

        assertEquals(100, transactionRecord.getAmount(), DELTA);
    }

    @Test
    public void givenTxOf2ReturnTxRecordWithAmount2() {
        double expected = 2;
        TransactionRecordGenerator generator = new TransactionRecordGenerator(expected, EMPTY_LIST, NOW, IS_CHARGE, ACCOUNT);

        TransactionRecord txRecord = generator.generate();

        assertEquals(expected, txRecord.getAmount(), DELTA);
    }

    @Test
    public void givenTransactionOf100AndNoExistingTransactionsAndCreditLimitOf1000ReturnApprovedTransaction() {
        TransactionRecordGenerator generator = new TransactionRecordGenerator(100, EMPTY_LIST, NOW, IS_CHARGE, new Account(ID, 1000, CC_NUMBER, ACTIVE, CARDHOLDER));

        TransactionRecord transactionRecord = generator.generate();

        assertTrue(transactionRecord.isApproved());
    }

    @Test
    public void givenValidChargeTransactionReturnApprovedChargeTransaction() {
        TransactionRecordGenerator generator = new TransactionRecordGenerator(AMOUNT, EMPTY_LIST, NOW, true, ACCOUNT);

        TransactionRecord transactionRecord = generator.generate();

        assertTrue( transactionRecord.isCharge());
    }

    @Test
    public void givenTransactionAtSomeTimeReturnTransactionRecordForThatTime() {
        Instant someTime = NOW.minusSeconds(10);
        TransactionRecordGenerator generator = new TransactionRecordGenerator(AMOUNT, EMPTY_LIST, someTime, IS_CHARGE, ACCOUNT);

        TransactionRecord transactionRecord = generator.generate();

        assertEquals(someTime, transactionRecord.getDateOfTransaction());
    }

    @Test
    public void givenTxOf1000AndCreditLimitOf100ReturnUnapprovedTransaction() {
        TransactionRecordGenerator generator = new TransactionRecordGenerator(1000, EMPTY_LIST, NOW, IS_CHARGE, ACCOUNT);

        TransactionRecord transactionRecord = generator.generate();

        assertFalse(transactionRecord.isApproved());
    }

    @Test
    public void givenTxOf2AndTransactionsSumming2AndCreditLimitOf0ReturnUnapprovedTransaction() {
        Account account = new Account(ID, 0, CC_NUMBER, ACTIVE, CARDHOLDER);
        TransactionRecordGenerator generator = new TransactionRecordGenerator(2, list(new TransactionRecord(2.0, NOW, false, account)), NOW, IS_CHARGE, account);

        TransactionRecord txRecord = generator.generate();

        assertFalse(txRecord.isApproved());
    }

    @Test
    public void givenTxOnAccountReturnTxRecordForThatSameAccount() {
        Integer accountId = 15;
        Account account = new Account(accountId, (int) CREDIT_LIMIT, CC_NUMBER, ACTIVE, CARDHOLDER);

        TransactionRecordGenerator generator = new TransactionRecordGenerator(AMOUNT, EMPTY_LIST, NOW, IS_CHARGE, account);
        TransactionRecord transactionRecord = generator.generate();

        assertEquals(accountId, transactionRecord.getAccount().getId());
    }

    private ArrayList<TransactionRecord> list(TransactionRecord...records){
        ArrayList<TransactionRecord> returnValue = EMPTY_LIST;
        Collections.addAll(returnValue, records);
        return returnValue;
    }
}
