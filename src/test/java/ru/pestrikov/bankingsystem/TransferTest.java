package ru.pestrikov.bankingsystem;

import ru.pestrikov.bankingsystem.Account.AccountType;
import ru.pestrikov.bankingsystem.Bank.Bank;
import ru.pestrikov.bankingsystem.CentralBank.CentralBank;
import ru.pestrikov.bankingsystem.Exceptions.InvalidRatioBetweenBordersAndInterests;
import ru.pestrikov.bankingsystem.Exceptions.NotEnoughRightsToUseThisAccount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TransferTest {
    @Test
    public void depositTest() throws InvalidRatioBetweenBordersAndInterests, NotEnoughRightsToUseThisAccount {
        Bank bank1 = CentralBank.getInstance().createBank(Setup.getBankParameters());
        var client1 = bank1.clientBuilder.createClient("FirstName", "LastName").withAddress("qwe").withPassport("qwe").execute();
        var accountId1 = client1.createAccount(AccountType.DebitAccount);

        Bank bank2 = CentralBank.getInstance().createBank(Setup.getBankParameters());
        var client2 = bank2.clientBuilder.createClient("FirstName", "LastName").withAddress("qwe").withPassport("qwe").execute();
        var accountId2 = client2.createAccount(AccountType.DebitAccount);

        client1.topUp(accountId1, 5000.0);
        client1.transfer(accountId1, bank2.getId(), accountId2, 3000.0);
        Double actualAccount1 = client1.getAccount(accountId1).getBalance();
        Double actualAccount2 = client2.getAccount(accountId2).getBalance();
        Double expectedAccount1 = 2000.0;
        Double expectedAccount2 = 3000.0;

        Assertions.assertEquals(expectedAccount1, actualAccount1);
        Assertions.assertEquals(expectedAccount2, actualAccount2);
    }
}
