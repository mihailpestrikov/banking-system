package ru.pestrikov.bankingsystem.Account;

public interface IHaveInterest {
    void chargeDailyInterest();
    void accrueOfInterest();
}
