package ru.pestrikov.bankingsystem.Transaction;

import ru.pestrikov.bankingsystem.Exceptions.UnresolvedTransactionCannotBeCancelled;
import ru.pestrikov.bankingsystem.Account.AccountOperationStatus;
import ru.pestrikov.bankingsystem.Bank.Bank;
import ru.pestrikov.bankingsystem.CentralBank.CentralBank;
import lombok.Getter;

import java.util.UUID;

/**
 * Транзакция снятия.
 * Взаимодействует с банком, сервисом обработки транзакций.
 * Реализует паттерн команда. Метод execute вызывается в центральном банке при обработке транзакции.
 * Также есть функционал отмены транзакции.
 */
@Getter
public class WithdrawalTransaction implements ITransaction {
    private final UUID id = UUID.randomUUID();
    private TransactionStatus status = TransactionStatus.InProcess;
    private final UUID accountId;
    private final UUID bankId;
    private final double amount;

    public WithdrawalTransaction(UUID bankId, UUID accountId, double amount) {
        this.accountId = accountId;
        this.bankId = bankId;
        this.amount = amount;
    }

    @Override
    public void execute() {
        Bank bank = CentralBank.getInstance().getBankById(bankId);

        AccountOperationStatus result = bank.transactionService.processWithdrawal(accountId, amount);

        switch (result) {
            case Success -> status = TransactionStatus.Completed;
            case SuccessWithCommission -> status = TransactionStatus.CompletedWithCommission;
            default -> status = TransactionStatus.Cancelled;
        }

        bank.transactionService.logTransaction(accountId, this);
    }

    @Override
    public void cancel() throws UnresolvedTransactionCannotBeCancelled {
        Bank bank = CentralBank.getInstance().getBankById(bankId);

        if (status == TransactionStatus.InProcess) {
            throw new UnresolvedTransactionCannotBeCancelled(String.format("Transaction status %s", status));
        }
        else if (status == TransactionStatus.CompletedWithCommission) {
            bank.transactionService.processTopUp(accountId, amount + bank.accountsParameters.getCreditAccountCommission());
        }
        else {
            bank.transactionService.processTopUp(accountId, amount);
        }

        status = TransactionStatus.Cancelled;
        bank.transactionService.logTransaction(accountId, this);
    }
}
