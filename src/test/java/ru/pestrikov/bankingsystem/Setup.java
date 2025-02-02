package ru.pestrikov.bankingsystem;

import ru.pestrikov.bankingsystem.Bank.BankAccountsParameters.BankAccountsParameters;
import ru.pestrikov.bankingsystem.Bank.BankAccountsParameters.DepositAccountInterestsDto;
import ru.pestrikov.bankingsystem.Exceptions.InvalidRatioBetweenBordersAndInterests;

import java.util.ArrayList;
import java.util.Arrays;

public class Setup {
    public static BankAccountsParameters getBankParameters() throws InvalidRatioBetweenBordersAndInterests {

        return new BankAccountsParameters(
                3.65,
                100.0,
                30000.0,
                15000.0,
                new DepositAccountInterestsDto(
                        new ArrayList<>(Arrays.asList(50000.0, 100000.0)),
                        new ArrayList<>(Arrays.asList(3.0, 3.5, 4.0)))
        );
    }
}
