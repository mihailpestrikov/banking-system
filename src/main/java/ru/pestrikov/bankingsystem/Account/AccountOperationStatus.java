package ru.pestrikov.bankingsystem.Account;

public enum AccountOperationStatus {
    Success,
    SuccessWithCommission,
    WithdrawalIsNotPossibleByDate,
    NotEnoughBalance
}
