package ru.pestrikov.bankingsystem.Exceptions;

public class UnresolvedTransactionCannotBeCancelled extends Exception {
    public UnresolvedTransactionCannotBeCancelled(String message) { super(message); }
}
