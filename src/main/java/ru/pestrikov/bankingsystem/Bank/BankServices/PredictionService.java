package ru.pestrikov.bankingsystem.Bank.BankServices;

import ru.pestrikov.bankingsystem.Account.IAccount;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * Сервис банка для ускорения времени - получения баланса счета через какое то количество дней.
 * Возвращает пару - балас счета и накопившийся процент на остаток, который будет выплачен в конце месяца.
 */
@AllArgsConstructor
public class PredictionService {
    private final Map<UUID, IAccount> accounts;
    public PredictionServiceResult predictBalanceInNDays(IAccount account, Integer days) {
        return account.predictInterest(days);
    }
}
