package ru.pestrikov.bankingsystem.Bank.BankAccountsParameters;

import ru.pestrikov.bankingsystem.Exceptions.InvalidRatioBetweenBordersAndInterests;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


@Getter
@Setter
public class DepositAccountInterestsDto {
    public ArrayList<Double> borders;
    public ArrayList<Double> interests;

    public DepositAccountInterestsDto(ArrayList<Double> borders, ArrayList<Double> interests) throws InvalidRatioBetweenBordersAndInterests {
        this.borders = borders;
        this.interests = interests;
        verification();
    }

    private void verification() throws InvalidRatioBetweenBordersAndInterests {
        if (interests.size() != borders.size() + 1) {
            throw new InvalidRatioBetweenBordersAndInterests("Should be n borders and n+1 interests");
        }
    }
}
