package com.revolut.library.protocol;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class TransactionLog {
    String primaryAccount; // Primar Account on which transaction is done
    BigDecimal amount;
    String SecondaryAccount; //from Account or in case of Self Withdrawal / Deposit it will be null
    TransactionEntry entryType; // Type of Transaction DEBIT/CREDIT
    long epoch;
    BigDecimal currentBalance;
    LogType logType; //To Control number of Rollbacks For now not controlling it.

    public enum LogType{
        TRANSACTION,
        ROLLBACK
    }
   public enum TransactionEntry{
        DEBIT,
        CREDIT
    }
}
