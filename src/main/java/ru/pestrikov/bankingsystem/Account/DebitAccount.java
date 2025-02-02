package ru.pestrikov.bankingsystem.Account;

import ru.pestrikov.bankingsystem.Bank.BankServices.PredictionServiceResult;
import ru.pestrikov.bankingsystem.Client.Client;
import ru.pestrikov.bankingsystem.Transaction.ITransaction;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Сущность дебетового счета.
 * Предоставляет возможность пополнения и снятия, логирование операций,
 * начисление и выплату процентов, механизм ускорения времени.
 */
@Getter
public class DebitAccount implements IAccount, IHaveInterest {
    private final UUID id = UUID.randomUUID();
    private final Client owner;
    private final LocalDate closingDate;
    private final Double interest;
    private Double balance = 0.0;
    private Double currentInterestOnBalance = 0.0;

    private final ArrayList<ITransaction> transactionHistory = new ArrayList<>();

    public DebitAccount(Client owner, LocalDate closingDate, Double Interest) {
        this.owner = owner;
        this.closingDate = closingDate;
        this.interest = Interest;
    }

    @Override
    public AccountOperationStatus deposit(Double amount) {
        balance += amount;
        return AccountOperationStatus.Success;
    }

    @Override
    public AccountOperationStatus withdraw(Double amount) {
        if (balance - amount < 0)
            return AccountOperationStatus.NotEnoughBalance;

        balance -= amount;
        return AccountOperationStatus.Success;
    }

    @Override
    public void logTransaction(ITransaction transaction) {
        transactionHistory.add(transaction);
    }

    @Override
    public PredictionServiceResult predictInterest(Integer days) {
        int counter = 0;
        double currentBalance = balance;
        double currentInterestOnBalance = 0.0;

        for (int i = 0; i < days; i++) {
            currentInterestOnBalance += currentBalance * (interest / 365 / 100);
            counter += 1;

            if (counter == 30) {
                currentBalance += currentInterestOnBalance;
                currentInterestOnBalance = 0.0;
                counter = 0;
            }
        }

        return new PredictionServiceResult(currentBalance, currentInterestOnBalance);
    }

    public void chargeDailyInterest() {
        currentInterestOnBalance += balance * (interest / 365 / 100);
    }

    public void accrueOfInterest() {
        balance += currentInterestOnBalance;
        currentInterestOnBalance = 0.0;
    }
}
