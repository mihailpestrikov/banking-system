package ru.pestrikov.bankingsystem.Account;

import ru.pestrikov.bankingsystem.Bank.BankServices.PredictionServiceResult;
import ru.pestrikov.bankingsystem.Transaction.ITransaction;
import lombok.Getter;
import ru.pestrikov.bankingsystem.Client.Client;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Сущность кредитного счета.
 * Предоставляет возможность пополнения и снятия, логирование операций,
 * начисление и выплату процентов, механизм ускорения времени.
 */
@Getter
public class CreditAccount implements IAccount {
    private final UUID id = UUID.randomUUID();
    private final Client owner;
    private final LocalDate closingDate;
    private final Double creditLimit;
    private final Double creditCommission;
    private final Double interest = 0.0;
    private Double balance = 0.0;

    private final ArrayList<ITransaction> transactionHistory = new ArrayList<>();

    public CreditAccount(Client owner, LocalDate closingDate , Double creditLimit, Double creditCommission) {
        this.owner = owner;
        this.closingDate = closingDate;
        this.creditLimit = creditLimit;
        this.creditCommission = creditCommission;
    }

    @Override
    public AccountOperationStatus deposit(Double amount) {
        balance += amount;
        return AccountOperationStatus.Success;
    }

    @Override
    public AccountOperationStatus withdraw(Double amount) {
        if (balance + creditLimit - amount < 0)
            return AccountOperationStatus.NotEnoughBalance;

        if (balance - amount < 0) {
            balance -= (amount + creditCommission);
            return AccountOperationStatus.SuccessWithCommission;
        }

        balance -= amount;
        return AccountOperationStatus.Success;
    }

    @Override
    public void logTransaction(ITransaction transaction) {
        transactionHistory.add(transaction);
    }

    @Override
    public PredictionServiceResult predictInterest(Integer days) {
        return new PredictionServiceResult(balance, 0.0);
    }
}
