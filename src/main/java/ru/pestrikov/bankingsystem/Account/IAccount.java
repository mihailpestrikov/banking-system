package ru.pestrikov.bankingsystem.Account;

import ru.pestrikov.bankingsystem.Bank.BankServices.PredictionServiceResult;
import ru.pestrikov.bankingsystem.Transaction.ITransaction;

import java.util.List;
import java.util.UUID;

public interface IAccount {
    UUID getId();
    Double getBalance();
    List<ITransaction> getTransactionHistory();
    AccountOperationStatus deposit(Double amount);
    AccountOperationStatus withdraw(Double amount);
    void logTransaction(ITransaction transaction);
    PredictionServiceResult predictInterest(Integer days);
}
