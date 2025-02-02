package ru.pestrikov.bankingsystem;

import ru.pestrikov.bankingsystem.Account.AccountType;
import ru.pestrikov.bankingsystem.Bank.Bank;
import ru.pestrikov.bankingsystem.CentralBank.CentralBank;
import ru.pestrikov.bankingsystem.Exceptions.InvalidRatioBetweenBordersAndInterests;
import ru.pestrikov.bankingsystem.Exceptions.UnverifiedAccountLimit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RestrictedAccountTest {
    @Test
    public void depositTest() throws InvalidRatioBetweenBordersAndInterests {
        Bank bank = CentralBank.getInstance().createBank(Setup.getBankParameters());
        var client = bank.clientBuilder.createClient("FirstName", "LastName").execute();
        var accountId = client.createAccount(AccountType.DebitAccount);

        client.topUp(accountId, 50000.0);

        Assertions.assertThrows(UnverifiedAccountLimit.class, () -> client.withdraw(accountId, 160000.0));
    }

}
