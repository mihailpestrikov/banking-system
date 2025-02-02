package ru.pestrikov.bankingsystem.Account;

import ru.pestrikov.bankingsystem.Bank.BankAccountsParameters.DepositAccountInterestsDto;
import ru.pestrikov.bankingsystem.Bank.BankServices.PredictionServiceResult;
import ru.pestrikov.bankingsystem.Client.Client;
import ru.pestrikov.bankingsystem.Transaction.ITransaction;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Сущность депозитного счета.
 * Предоставляет возможность пополнения и снятия, логирование операций,
 * начисление и выплату процентов, механизм ускорения времени.
 */
@Getter
public class DepositAccount implements IAccount, IHaveInterest {
    private final UUID id = UUID.randomUUID();
    private final Client owner;
    private final LocalDate closingDate;
    private final DepositAccountInterestsDto depositAccountInterests;
    private Double currentInterest;
    private Double balance = 0.0;
    private Double currentInterestOnBalance = 0.0;

    private final ArrayList<ITransaction> transactionHistory = new ArrayList<>();

    public DepositAccount(Client owner, LocalDate closingDate, DepositAccountInterestsDto depositAccountInterests) {
        this.owner = owner;
        this.closingDate = closingDate;
        this.depositAccountInterests = depositAccountInterests;
    }

    @Override
    public AccountOperationStatus deposit(Double amount) {
        balance += amount;
        setCurrentInterest();
        return AccountOperationStatus.Success;
    }

    @Override
    public AccountOperationStatus withdraw(Double amount) {
        if (balance - amount < 0)
            return AccountOperationStatus.NotEnoughBalance;
        if (LocalDate.now().isBefore(closingDate))
            return AccountOperationStatus.WithdrawalIsNotPossibleByDate;

        balance -= amount;
        setCurrentInterest();
        return AccountOperationStatus.Success;
    }

    @Override
    public void logTransaction(ITransaction transaction) {
        transactionHistory.add(transaction);
    }

    @Override
    public PredictionServiceResult predictInterest(Integer days) {
        double currentInterestOnBalance = balance * (currentInterest / 365 / 100) * days;

        return new PredictionServiceResult(balance, currentInterestOnBalance);
    }

    public void chargeDailyInterest() {
        currentInterestOnBalance += balance * (currentInterest / 365 / 100);
    }

    public void accrueOfInterest() {
        balance += currentInterestOnBalance;
        currentInterestOnBalance = 0.0;
    }

    private void setCurrentInterest() {
        if (balance < depositAccountInterests.getBorders().getFirst()) {
            currentInterest = depositAccountInterests.getInterests().getFirst();
            return;
        }

        for (int i = 1; i < depositAccountInterests.getBorders().size(); i++) {
            if (depositAccountInterests.getBorders().get(i - 1) <= balance && balance <= depositAccountInterests.getBorders().get(i)) {
                currentInterest = depositAccountInterests.getInterests().get(i);
                return;
            }
        }

        if (balance > depositAccountInterests.getBorders().getLast()) {
            currentInterest = depositAccountInterests.getInterests().getLast();
        }
    }
}
