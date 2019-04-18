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

    @Test
    public void givenTransactionOf100AndNoExistingTransactionsAndCreditLimitOf1000ReturnTransactionRecordWithAmount100() {
        TransactionRecord transactionRecord = new TransactionRecordGenerator(100, EMPTY_LIST, 1000, NOW, true, new Account()).generate();

        double transactionRecordAmount = transactionRecord.getAmount();
        assertEquals(100, transactionRecordAmount, DELTA);
    }

    @Test
    public void givenTxOf2ReturnTxRecordWithAmount2() {
        double expected = 2;
        TransactionRecordGenerator generator = new TransactionRecordGenerator(expected, EMPTY_LIST, 100, NOW, true, new Account());

        TransactionRecord txRecord = generator.generate();

        assertEquals(expected, txRecord.getAmount(), DELTA);
    }

    @Test
    public void givenTransactionOf100AndNoExistingTransactionsAndCreditLimitOf1000ReturnApprovedTransaction() {
        TransactionRecordGenerator generator = new TransactionRecordGenerator(100, EMPTY_LIST, 1000, NOW, true, new Account());

        TransactionRecord transactionRecord = generator.generate();

        assertTrue(transactionRecord.isApproved());
    }

    @Test
    public void givenValidChargeTransactionReturnApprovedChargeTransaction() {
        TransactionRecordGenerator generator = new TransactionRecordGenerator(100, EMPTY_LIST, 1000, NOW, true, new Account());

        TransactionRecord transactionRecord = generator.generate();

        assertTrue( transactionRecord.isCharge());
    }

    @Test
    public void givenTransactionAtCurrentTimeAndNoExistingTransactionsAndCreditLimitOf1000ReturnTransactionRecordWithCurrentTime() {
        Instant now = NOW.minusSeconds(10);
        TransactionRecord transactionRecord = new TransactionRecordGenerator(100, EMPTY_LIST, 1000, now, true, new Account()).generate();

        assertEquals(now, transactionRecord.getDateOfTransaction());
    }

    @Test
    public void givenTxOf1000AndCreditLimitOf100ReturnUnapprovedTransaction() {
        TransactionRecord transactionRecord = new TransactionRecordGenerator(1000, EMPTY_LIST, 100, NOW, true, new Account()).generate();

        assertFalse(transactionRecord.isApproved());
    }

    @Test
    public void givenTxOf2AndChargesSumming2AndCreditLimitOf3ReturnUnapprovedTransaction() {
        TransactionRecordGenerator generator = new TransactionRecordGenerator(2, list(new TransactionRecord(2.0, NOW, false, new Account())), 3, NOW, true, new Account());

        TransactionRecord txRecord = generator.generate();

        assertFalse(txRecord.isApproved());
    }

    private ArrayList<TransactionRecord> list(TransactionRecord...records){
        ArrayList<TransactionRecord> returnValue = EMPTY_LIST;
        Collections.addAll(returnValue, records);
        return returnValue;
    }

    @Test
    public void givenTxOnAccountReturnTxRecordForThatSameAccount() {
        Integer accountId = 15;
        Account account = new Account(accountId, (int) CREDIT_LIMIT, CC_NUMBER, ACTIVE, new Cardholder());

        TransactionRecordGenerator generator = new TransactionRecordGenerator(AMOUNT, EMPTY_LIST, CREDIT_LIMIT, NOW, IS_CHARGE, account);
        TransactionRecord transactionRecord = generator.generate();

        assertEquals(accountId, transactionRecord.getAccount().getId());
    }
}
