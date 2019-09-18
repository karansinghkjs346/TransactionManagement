package com.revolut.library;

import com.revolut.config.TransactionManagementConfig;
import com.revolut.library.protocol.CustomerInformation;
import com.revolut.library.protocol.TransactionLog;
import com.revolut.rest.protocol.AccTransferPayload;
import com.revolut.rest.protocol.UserDefinationPayload;
import com.revolut.library.protocol.TransactionLog.TransactionEntry;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.*;

@Getter
@Setter
public class TransactionRuntime {
    public static int accountPostFix = 1;
    public static Map<String, CustomerInformation> accounts;
    public static List<TransactionLog> bankLogs;

    /**
     * @param transactionManagementConfig
     */
    public TransactionRuntime(TransactionManagementConfig transactionManagementConfig) {
        TransactionManagementConfig.Provider.initialize(transactionManagementConfig);
        accounts = Collections.synchronizedMap(new HashMap<>());
        bankLogs = Collections.synchronizedList(new LinkedList<>());
//        addAccounts(); //For Testing the API

    }

    /**
     * @param userDefinationPayload
     * @return
     */
    public CustomerInformation registerUser(UserDefinationPayload userDefinationPayload) {
        CustomerInformation customerInformation = new CustomerInformation();
        accountPostFix++;
        String accounNo = userDefinationPayload.getFirstName() + "AC" + accountPostFix;
        customerInformation.setAccountNumber(accounNo);
        customerInformation.setFirstName(userDefinationPayload.getFirstName());
        customerInformation.setLastName(userDefinationPayload.getLastName());
        customerInformation.setBalance(userDefinationPayload.getAmount());
        customerInformation.setPhoneNumber(userDefinationPayload.getPhoneNumber());
        accounts.put(accounNo, customerInformation);
        return customerInformation;
    }

    /**
     * @param accountNumber
     * @return
     */
    public String getAccountDetails(String accountNumber) {
        if (accounts.containsKey(accountNumber))
            return accounts.get(accountNumber).toString();
        else
            return "No Information Available for Such account";
    }


    /**
     * @param accTransferPayload
     * @return
     */
    public String accountTransfer(AccTransferPayload accTransferPayload) {
        if (!(checkAccountNo(accTransferPayload.getToAccountNo()) && checkAccountNo(accTransferPayload.getFromAccountNo())))
            return "No Account of this kind " + accTransferPayload.getToAccountNo() + "or" + accTransferPayload.getFromAccountNo();

        CustomerInformation toCust = accounts.get(accTransferPayload.getToAccountNo());
        CustomerInformation fromCust = accounts.get(accTransferPayload.getFromAccountNo());

        BigDecimal amountToTransfer = accTransferPayload.getAmount();
        BigDecimal fromCustAmount = fromCust.getBalance();
        BigDecimal toCustAmount = toCust.getBalance();
        if (fromCustAmount.compareTo(amountToTransfer) == -1)
            return "Amount not Sufficient";

        BigDecimal fromCustAmout = fromCust.getBalance();
        toCust.setBalance(toCustAmount.add(amountToTransfer));
        updateTransactionLog(toCust.getTransactionLog(), toCust.getAccountNumber(), fromCust.getAccountNumber(), TransactionEntry.DEBIT, amountToTransfer, toCust.getBalance());
        fromCust.setBalance(fromCustAmout.subtract(amountToTransfer));
        updateTransactionLog(fromCust.getTransactionLog(), fromCust.getAccountNumber(), toCust.getAccountNumber(), TransactionEntry.CREDIT, amountToTransfer, fromCust.getBalance());

        return "Success";
    }

    /**
     * @param accountNumber
     * @return
     */
    public boolean checkAccountNo(String accountNumber) {
        if (accounts.containsKey(accountNumber))
            return true;
        else
            return false;
    }

    /**
     * @param accountNumber
     * @param amount
     * @return
     */
    public String selfDeposit(String accountNumber, BigDecimal amount) {
        if (!checkAccountNo(accountNumber))
            return "No Account of this kind " + accountNumber;
        CustomerInformation toAccount = accounts.get(accountNumber);
        BigDecimal toAmount = toAccount.getBalance();
        toAccount.setBalance(toAmount.add(amount));
        updateTransactionLog(toAccount.getTransactionLog(), accountNumber, null, TransactionEntry.DEBIT, amount, toAccount.getBalance());
        return "Success";
    }

    /**
     * @param accountNumber
     * @param amount
     * @return
     */
    public String selfWidthdraw(String accountNumber, BigDecimal amount) {
        if (!checkAccountNo(accountNumber))
            return "No Account of this kind " + accountNumber;
        CustomerInformation fromAccount = accounts.get(accountNumber);
        BigDecimal fromAmount = fromAccount.getBalance();
        if (fromAmount.compareTo(amount) == -1)
            return "Amount not Sufficient";
        fromAccount.setBalance(fromAmount.subtract(amount));
        updateTransactionLog(fromAccount.getTransactionLog(), accountNumber, null, TransactionEntry.CREDIT, amount, fromAccount.getBalance());

        return "Success";
    }

    /**
     * @param list
     * @param primaryAccountNumber
     * @param secondaryAccountNunber
     * @param type
     * @param amount
     * @param currentBalance
     */
    public static void updateTransactionLog(List<TransactionLog> list, String primaryAccountNumber, String secondaryAccountNunber, TransactionLog.TransactionEntry type, BigDecimal amount, BigDecimal currentBalance) {
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setSecondaryAccount(secondaryAccountNunber);
        transactionLog.setPrimaryAccount(primaryAccountNumber);
        transactionLog.setAmount(amount);
        transactionLog.setEpoch(System.currentTimeMillis());
        transactionLog.setEntryType(type);
        transactionLog.setCurrentBalance(currentBalance);
        list.add(transactionLog);
        bankLogs.add(transactionLog);
    }

    /**
     * @return
     */
    //Currently not controlling the rollbacks but can be done
    public String rollBack() {
        int sizeOfList = bankLogs.size();
        TransactionLog lastLog = bankLogs.get(sizeOfList - 1);
        BigDecimal amount = lastLog.getAmount();
        CustomerInformation primaryAcc = accounts.get(lastLog.getPrimaryAccount());
        CustomerInformation secondaryAcc = null;
        if (lastLog.getSecondaryAccount() != null)
            secondaryAcc = accounts.get(lastLog.getSecondaryAccount());
        switch (lastLog.getEntryType()) {
            case DEBIT: {
                BigDecimal currBalance = primaryAcc.getBalance();
                primaryAcc.setBalance(currBalance.subtract(amount));
                if (secondaryAcc != null) {
                    updateTransactionLog(primaryAcc.getTransactionLog(), primaryAcc.getAccountNumber(), secondaryAcc.getAccountNumber(), TransactionEntry.CREDIT, amount, primaryAcc.getBalance());
                    BigDecimal secCurrBalance = secondaryAcc.getBalance();
                    secondaryAcc.setBalance(secCurrBalance.add(amount));
                    updateTransactionLog(secondaryAcc.getTransactionLog(), secondaryAcc.getAccountNumber(), primaryAcc.getAccountNumber(), TransactionEntry.DEBIT, amount, secondaryAcc.getBalance());
                } else {
                    updateTransactionLog(primaryAcc.getTransactionLog(), primaryAcc.getAccountNumber(), null, TransactionEntry.CREDIT, amount, primaryAcc.getBalance());

                }
                break;
            }
            case CREDIT: {
                BigDecimal currBalance = primaryAcc.getBalance();
                primaryAcc.setBalance(currBalance.add(amount));
                //Change : update the log
                if (secondaryAcc != null) {
                    updateTransactionLog(primaryAcc.getTransactionLog(), primaryAcc.getAccountNumber(), secondaryAcc.getAccountNumber(), TransactionEntry.DEBIT, amount, primaryAcc.getBalance());
                    BigDecimal secCurrBalance = secondaryAcc.getBalance();
                    secondaryAcc.setBalance(secCurrBalance.subtract(amount));
                    updateTransactionLog(secondaryAcc.getTransactionLog(), secondaryAcc.getAccountNumber(), primaryAcc.getAccountNumber(), TransactionEntry.CREDIT, amount, secondaryAcc.getBalance());

                } else {
                    updateTransactionLog(primaryAcc.getTransactionLog(), primaryAcc.getAccountNumber(), null, TransactionEntry.DEBIT, amount, primaryAcc.getBalance());

                }
                break;
            }
            default:
                return "INVALID TYPE";
        }
        return "Transaction Rolled Back Status : SUCCESS  Transaction Rolled Back was : " + lastLog.toString();
    }

//    /**
//     *
//     */
//    public static void addAccounts() {
//        for (int i = 0; i < 5; i++) {
//            CustomerInformation customerInformation = new CustomerInformation();
//            customerInformation.setBalance(BigDecimal.valueOf(20 + i));
//            customerInformation.setFirstName("karan" + i);
//            customerInformation.setLastName("Singh" + i);
//            customerInformation.setAccountNumber("KaranAC" + i);
//            customerInformation.setPhoneNumber(12345 + i);
//            accounts.put("KaranAC" + i, customerInformation);
//
//        }
//    }


}
