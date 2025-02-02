package ru.pestrikov.bankingsystem.Bank.BankServices;

import ru.pestrikov.bankingsystem.Transaction.ITransaction;
import ru.pestrikov.bankingsystem.Account.AccountOperationStatus;
import ru.pestrikov.bankingsystem.Account.IAccount;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * Сервис банка для работы с транзакциями.
 * Обрабатывает пополнения, снятия и переводы.
 */
@AllArgsConstructor
public class TransactionService {
    private final Map<UUID, IAccount> accounts;

    public AccountOperationStatus processWithdrawal(UUID accountId, Double amount) {
        return accounts.get(accountId).withdraw(amount);
    }

    public AccountOperationStatus processTopUp(UUID accountId, Double amount) {
        return accounts.get(accountId).deposit(amount);
    }

    public void logTransaction(UUID accountId, ITransaction transaction) {
        accounts.get(accountId).logTransaction(transaction);
    }
}
