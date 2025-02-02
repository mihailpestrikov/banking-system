package ru.pestrikov.bankingsystem.Transaction;

import ru.pestrikov.bankingsystem.Exceptions.UnresolvedTransactionCannotBeCancelled;
import ru.pestrikov.bankingsystem.Account.AccountOperationStatus;
import ru.pestrikov.bankingsystem.Bank.Bank;
import ru.pestrikov.bankingsystem.CentralBank.CentralBank;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

/**
 * Транзакция пополнения.
 * Взаимодействует с банком, сервисом обработки транзакций.
 * Реализует паттерн команда. Метод execute вызывается в центральном банке при обработке транзакции.
 * Также есть функционал отмены транзакции.
 */
@Getter
public class TopUpTransaction implements ITransaction {
    private final UUID id = UUID.randomUUID();
    private TransactionStatus status = TransactionStatus.InProcess;
    private final UUID accountId;
    private final UUID bankId;
    private final double amount;

    public TopUpTransaction(UUID accountId, UUID bankId, double amount) {
        this.accountId = accountId;
        this.bankId = bankId;
        this.amount = amount;
    }
    @Override
    public void execute() {
        Bank bank = CentralBank.getInstance().getBankById(bankId);

        AccountOperationStatus result = bank.transactionService.processTopUp(accountId, amount);

        if (Objects.requireNonNull(result) == AccountOperationStatus.Success) {
            status = TransactionStatus.Completed;
        } else {
            status = TransactionStatus.Cancelled;
        }

        bank.transactionService.logTransaction(accountId,this);
    }

    @Override
    public void cancel() throws UnresolvedTransactionCannotBeCancelled {
        if (status == TransactionStatus.InProcess) {
            throw new UnresolvedTransactionCannotBeCancelled(String.format("Transaction status %s", status));
        }

        Bank bank = CentralBank.getInstance().getBankById(bankId);

        bank.transactionService.processWithdrawal(accountId, amount);

        status = TransactionStatus.Cancelled;

        bank.transactionService.logTransaction(accountId,this);
    }
}
