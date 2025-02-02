package ru.pestrikov.bankingsystem.Transaction;

import ru.pestrikov.bankingsystem.Exceptions.UnresolvedTransactionCannotBeCancelled;
import ru.pestrikov.bankingsystem.Account.AccountOperationStatus;
import ru.pestrikov.bankingsystem.Bank.Bank;
import ru.pestrikov.bankingsystem.CentralBank.CentralBank;
import lombok.Getter;

import java.util.UUID;

import static ru.pestrikov.bankingsystem.Account.AccountOperationStatus.Success;


/**
 * Транзакция перевода.
 * Взаимодействует с банком, сервисом обработки транзакций.
 * Реализует паттерн команда. Метод execute вызывается в центральном банке при обработке транзакции.
 * Также есть функционал отмены транзакции.
 */
@Getter
public class TransferTransaction implements ITransaction {
    private final UUID id = UUID.randomUUID();
    private TransactionStatus status = TransactionStatus.InProcess;
    private final UUID accountSenderId;
    private final UUID bankSenderId;
    private final UUID accountReceiverId;
    private final UUID bankReceiverId;
    private final Double amount;

    public TransferTransaction(UUID bankSenderId, UUID accountSenderId, UUID bankReceiverId, UUID accountReceiverId , double amount) {
        this.accountSenderId = accountSenderId;
        this.bankSenderId = bankSenderId;
        this.accountReceiverId = accountReceiverId;
        this.bankReceiverId = bankReceiverId;
        this.amount = amount;
    }

    @Override
    public void execute() {
        Bank bankSender = CentralBank.getInstance().getBankById(bankSenderId);

        AccountOperationStatus result = bankSender.transactionService.processWithdrawal(accountSenderId, amount);

        switch (result) {
            case Success -> status = TransactionStatus.Completed;
            case SuccessWithCommission -> status = TransactionStatus.CompletedWithCommission;
            default -> status = TransactionStatus.Cancelled;
        }

        bankSender.transactionService.logTransaction(accountSenderId,this);

        status = TransactionStatus.Completed;
        Bank bankReceiver = CentralBank.getInstance().getBankById(bankReceiverId);

        if (bankReceiver.transactionService.processTopUp(accountReceiverId, amount) != Success)
            status = TransactionStatus.Cancelled;
        bankReceiver.transactionService.logTransaction(accountReceiverId, this);
    }

    @Override
    public void cancel() throws UnresolvedTransactionCannotBeCancelled {
        if (status == TransactionStatus.InProcess) {
            throw new UnresolvedTransactionCannotBeCancelled(String.format("Transaction status %s", status));
        }

        Bank bankSender = CentralBank.getInstance().getBankById(bankSenderId);
        Bank bankReceiver = CentralBank.getInstance().getBankById(bankReceiverId);

        status = TransactionStatus.Cancelled;

        bankSender.transactionService.processTopUp(accountSenderId, amount);
        bankSender.transactionService.logTransaction(accountSenderId, this);

        bankReceiver.transactionService.processWithdrawal(accountReceiverId, amount);
        bankReceiver.transactionService.logTransaction(accountReceiverId, this);
    }
}
