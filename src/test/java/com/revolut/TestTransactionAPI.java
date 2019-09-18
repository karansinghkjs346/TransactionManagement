package com.revolut;

import com.revolut.library.protocol.CustomerInformation;
import com.revolut.rest.protocol.AccTransferPayload;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class TestTransactionAPI extends UnitTestBase {

    @Test
    public void testRegisterUser() {

        CustomerInformation output = transactionRuntime.registerUser(getSampleUserDefinationPayload());
        assertEquals(getSampleUserDefinationPayload().getFirstName(), output.getFirstName());
        assertEquals(getSampleUserDefinationPayload().getAmount(), output.getBalance());
        assertEquals(getSampleUserDefinationPayload().getLastName(), output.getLastName());
        assertEquals(getSampleUserDefinationPayload().getPhoneNumber(), output.getPhoneNumber());
        String accNo = output.getAccountNumber();
        assertEquals(getSampleUserDefinationPayload().getFirstName() + "AC", accNo.substring(0, accNo.length() - 1));
    }

    @Test
    public void testGetAccountDetails() {
        CustomerInformation output = transactionRuntime.registerUser(getSampleUserDefinationPayload());
        assertEquals(transactionRuntime.getAccountDetails(output.getAccountNumber()), output.toString());
    }

    @Test
    public void testCheckAccountNumber() {
        CustomerInformation output = transactionRuntime.registerUser(getSampleUserDefinationPayload());
        assertEquals(transactionRuntime.checkAccountNo(output.getAccountNumber()), true);
    }

    @Test
    public void testSelfDeposit() {
        CustomerInformation output = transactionRuntime.registerUser(getSampleUserDefinationPayload());
        BigDecimal amountBefore = output.getBalance();
        BigDecimal amountDeposit = BigDecimal.valueOf(10);
        transactionRuntime.selfDeposit(output.getAccountNumber(), amountDeposit);
        assertEquals(output.getBalance(), amountBefore.add(amountDeposit));
    }

    @Test
    public void testSelfWithdraw() {
        CustomerInformation output = transactionRuntime.registerUser(getSampleUserDefinationPayload());
        BigDecimal amountBefore = output.getBalance();
        BigDecimal amountDeposit = BigDecimal.valueOf(10);
        transactionRuntime.selfWidthdraw(output.getAccountNumber(), amountDeposit);
        assertEquals(output.getBalance(), amountBefore.subtract(amountDeposit));
    }

    @Test
    public void testAccountTransfer() {
        CustomerInformation primary = transactionRuntime.registerUser(getSampleUserDefinationPayload());
        CustomerInformation secondary = transactionRuntime.registerUser(getSampleUserDefinationPayloadSecond());
        AccTransferPayload accTransferPayload = new AccTransferPayload();
        accTransferPayload.setToAccountNo(primary.getAccountNumber());
        BigDecimal primaryAccBalance = primary.getBalance();
        accTransferPayload.setFromAccountNo(secondary.getAccountNumber());
        BigDecimal secondaryAccBalance = secondary.getBalance();
        BigDecimal amountTransfer = BigDecimal.valueOf(10);
        accTransferPayload.setAmount(amountTransfer);
        transactionRuntime.accountTransfer(accTransferPayload);
        assertEquals(primary.getBalance(), primaryAccBalance.add(amountTransfer));
        assertEquals(secondary.getBalance(), secondaryAccBalance.subtract(amountTransfer));

    }

    @Test
    public void testRollBack() {
        CustomerInformation primary = transactionRuntime.registerUser(getSampleUserDefinationPayload());
        CustomerInformation secondary = transactionRuntime.registerUser(getSampleUserDefinationPayloadSecond());
        AccTransferPayload accTransferPayload = new AccTransferPayload();
        accTransferPayload.setToAccountNo(primary.getAccountNumber());
        BigDecimal primaryAccBalance = primary.getBalance();
        accTransferPayload.setFromAccountNo(secondary.getAccountNumber());
        BigDecimal secondaryAccBalance = secondary.getBalance();
        BigDecimal amountTransfer = BigDecimal.valueOf(10);
        accTransferPayload.setAmount(amountTransfer);
        transactionRuntime.accountTransfer(accTransferPayload);
        transactionRuntime.rollBack();
        assertEquals(primary.getBalance(), primaryAccBalance);
        assertEquals(secondary.getBalance(), secondaryAccBalance);

    }
}
