package ru.pestrikov.bankingsystem;

import ru.pestrikov.bankingsystem.Account.AccountType;
import ru.pestrikov.bankingsystem.Bank.Bank;
import ru.pestrikov.bankingsystem.CentralBank.CentralBank;
import ru.pestrikov.bankingsystem.Exceptions.InvalidRatioBetweenBordersAndInterests;
import ru.pestrikov.bankingsystem.Exceptions.NotEnoughRightsToUseThisAccount;
import ru.pestrikov.bankingsystem.Exceptions.UnverifiedAccountLimit;
import ru.pestrikov.bankingsystem.Transaction.TransactionStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DebitAccountTest {
    @Test
    public void depositTest() throws InvalidRatioBetweenBordersAndInterests {
        Bank bank = CentralBank.getInstance().createBank(Setup.getBankParameters());
        var client = bank.clientBuilder.createClient("FirstName", "LastName").withAddress("qwe").withPassport("qwe").execute();
        var accountId = client.createAccount(AccountType.DebitAccount);

        client.topUp(accountId, 5000.0);
        Double expected = 5000.0;
        Double actual = client.getAccount(accountId).getBalance();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void depositAndWithdrawTest() throws InvalidRatioBetweenBordersAndInterests, NotEnoughRightsToUseThisAccount, UnverifiedAccountLimit {
        Bank bank = CentralBank.getInstance().createBank(Setup.getBankParameters());
        var client = bank.clientBuilder.createClient("FirstName", "LastName").withAddress("qwe").withPassport("qwe").execute();
        var accountId = client.createAccount(AccountType.DebitAccount);

        client.topUp(accountId, 5000.0);
        client.withdraw(accountId, 1250.0);
        client.withdraw(accountId, 750.0);
        Double expected = 3000.0;
        Double actual = client.getAccount(accountId).getBalance();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void withdrawMoreThanBalance() throws InvalidRatioBetweenBordersAndInterests, NotEnoughRightsToUseThisAccount, UnverifiedAccountLimit {
        Bank bank = CentralBank.getInstance().createBank(Setup.getBankParameters());
        var client = bank.clientBuilder.createClient("FirstName", "LastName").withAddress("qwe").withPassport("qwe").execute();
        var accountId = client.createAccount(AccountType.DebitAccount);

        client.withdraw(accountId, 35000.0);
        var actual = client.getAccount(accountId).getTransactionHistory().getLast().getStatus();
        TransactionStatus expected = TransactionStatus.Cancelled;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void predictInterest() throws InvalidRatioBetweenBordersAndInterests {
        Bank bank = CentralBank.getInstance().createBank(Setup.getBankParameters());
        var client = bank.clientBuilder.createClient("FirstName", "LastName").withAddress("qwe").withPassport("qwe").execute();
        var accountId = client.createAccount(AccountType.DebitAccount);

        client.topUp(accountId, 100000.0);
        var actual = client.getAccount(accountId).predictInterest(365);
        long expectedBalance = 103660;
        long expectedInterestOnBalance = 52;

        Assertions.assertEquals(expectedBalance, Math.round(actual.balance()));
        Assertions.assertEquals(expectedInterestOnBalance, Math.round(actual.currentInterestOnBalance()));
    }
}
