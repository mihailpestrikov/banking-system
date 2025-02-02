package ru.pestrikov.bankingsystem.Bank.BankServices;

import ru.pestrikov.bankingsystem.Client.Client;
import ru.pestrikov.bankingsystem.Account.CreditAccount;
import ru.pestrikov.bankingsystem.Account.DebitAccount;
import ru.pestrikov.bankingsystem.Account.DepositAccount;
import ru.pestrikov.bankingsystem.Account.IAccount;
import ru.pestrikov.bankingsystem.Bank.Bank;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

/**
 * Сервис банка для создания счетов.
 */
public class AccountFactory {
    private final Map<UUID, IAccount> accounts;
    private final Bank bank;

    @Setter
    private Integer debitAccountTermMonths = 48;
    @Setter
    private Integer creditAccountTermMonths = 48;
    @Setter
    private Integer depositAccountTermMonths = 12;

    public AccountFactory(Map<UUID, IAccount> accounts, Bank bank) {
        this.accounts = accounts;
        this.bank = bank;
    }


    public IAccount createDebitAccount(Client client) {
        var closingDate = LocalDate.now().plusMonths(debitAccountTermMonths);
        var account = new DebitAccount(client, closingDate,
                bank.accountsParameters.getDebitAccountInterest());
        accounts.put(account.getId(), account);
        return account;
    }

    public IAccount createCreditAccount(Client client) {
        var closingDate = LocalDate.now().plusMonths(creditAccountTermMonths);
        var account = new CreditAccount(client, closingDate,
                bank.accountsParameters.getCreditAccountLimit(),
                bank.accountsParameters.getCreditAccountCommission());
        accounts.put(account.getId(), account);
        return account;
    }

    public IAccount createDepositAccount(Client client) {
        var closingDate = LocalDate.now().plusMonths(depositAccountTermMonths);
        var account = new DepositAccount(client, closingDate,
                bank.accountsParameters.getDepositAccountInterests());
        accounts.put(account.getId(), account);
        return account;
    }
}
