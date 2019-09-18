package com.revolut.library.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Getter
@Setter
@AllArgsConstructor

public class CustomerInformation {

//    private final ReentrantLock lock = new ReentrantLock(); //For acquiring  the lock

    public CustomerInformation()
    {
        List list=new LinkedList<>();
        transactionLog= Collections.synchronizedList(new LinkedList<>());
    }
    @Override
    public String toString() {
        return " Customer Information { " +
                "accountNumber= '" + accountNumber + '\'' +
                ", firstName= '" + firstName + '\'' +
                ", lastName= '" + lastName + '\'' +
                ", phoneNumber= " + phoneNumber +
                ", amount= " + balance +
                 '}';
    }

    String accountNumber;
    String firstName;
    String lastName;
    int phoneNumber;
    BigDecimal balance;
    List<TransactionLog> transactionLog;

//    public void lock() {
//        this.lock.lock();
//    }
//
//    public void unlock() {
//        this.lock.unlock();
//    }

}
