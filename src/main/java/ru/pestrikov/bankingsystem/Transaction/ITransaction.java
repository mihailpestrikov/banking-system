package ru.pestrikov.bankingsystem.Transaction;

import ru.pestrikov.bankingsystem.Exceptions.UnresolvedTransactionCannotBeCancelled;


public interface ITransaction {
    void execute();
    void cancel() throws UnresolvedTransactionCannotBeCancelled;
    TransactionStatus getStatus();
}
