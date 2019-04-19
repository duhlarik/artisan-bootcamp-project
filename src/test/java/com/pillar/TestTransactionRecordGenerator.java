package com.pillar;

import com.pillar.account.Account;
import com.pillar.cardholder.Cardholder;
import com.pillar.transaction.TransactionRecord;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

import static com.pillar.transaction.TransactionRecord.DELTA;
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
    private static final Cardholder CARDHOLDER = new Cardholder();
    private static final Account ACCOUNT = new Account();
    private static final boolean APPROVED = true;

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
        ArrayList<TransactionRecord> returnValue = new ArrayList<>();
        Collections.addAll(returnValue, records);
        return returnValue;
    }

    @Test
    public void calculateTransactionBalanceReturns5GivenASingleTransactionRecordOf5() {
        TransactionRecord tran = new TransactionRecord(5.0, NOW, APPROVED, ACCOUNT);

        double expected = TransactionRecordGenerator.calculateTransactionBalance(list(tran));

        assertEquals(5, expected, 0.001);
    }

    @Test
    public void calculateTransactionBalanceReturns0GivenNoTransactionRecords() {

        assertEquals(0, TransactionRecordGenerator.calculateTransactionBalance(list()), 0.001);
    }

    @Test
    public void calculateTransactionBalanceReturns5GivenASingleAuthorizationTransactionRecordOf5() {
        TransactionRecord authTransaction = new TransactionRecord(5.0, NOW, APPROVED, ACCOUNT, false);

        double expected = TransactionRecordGenerator.calculateTransactionBalance(list(authTransaction));

        assertEquals(5.0, expected, 0.001);
    }

    @Test
    public void calculateChargeBalanceReturns0GivenSingleAuthorizationTransactionOf5() {
        TransactionRecord authTransaction = new TransactionRecord(5.0, NOW, APPROVED, ACCOUNT, false);

        double expected = TransactionRecordGenerator.calculateChargeBalance(list(authTransaction));

        assertEquals(0, expected, 0.001);
    }

    @Test
    public void calculateChargeBalanceReturnsChargeAmountGivenChargeAndAuthorizationTransactionRecords() {
        double chargeAmount = 5.0;
        TransactionRecord chargeTransaction = new TransactionRecord(chargeAmount, NOW, APPROVED, ACCOUNT, true);
        TransactionRecord authTransaction = new TransactionRecord(1.0, NOW, APPROVED, ACCOUNT, false);

        double expected = TransactionRecordGenerator.calculateChargeBalance(list(chargeTransaction, authTransaction));

        assertEquals(chargeAmount, expected, 0.001);
    }

    @Test
    public void isValidReturnsTrueGivenBalance5_Amount5_CreditLimit_10() {
        Account account = new Account(ID, 10, CC_NUMBER, ACTIVE, CARDHOLDER);
        TransactionRecordGenerator transactionRecordGenerator = new TransactionRecordGenerator(5, list(new TransactionRecord(5.0, NOW, APPROVED, account)), NOW, IS_CHARGE, account);

        boolean isValid = transactionRecordGenerator.isTransactionValid();

        assertTrue(isValid);
    }

    @Test
    public void isValidReturnsFalseGivenAmount6_Balance5_CreditLimit_10() {
        Account account = new Account(ID, 10, CC_NUMBER, ACTIVE, CARDHOLDER);
        TransactionRecordGenerator transactionRecordGenerator = new TransactionRecordGenerator(6, list(new TransactionRecord(5.0, NOW, APPROVED, account)), NOW, IS_CHARGE, account);

        boolean isValid = transactionRecordGenerator.isTransactionValid();

        assertFalse(isValid);
    }
}
