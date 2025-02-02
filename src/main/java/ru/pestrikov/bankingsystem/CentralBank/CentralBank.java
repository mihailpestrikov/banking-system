package ru.pestrikov.bankingsystem.CentralBank;


import ru.pestrikov.bankingsystem.Transaction.ITransaction;
import ru.pestrikov.bankingsystem.Bank.Bank;
import ru.pestrikov.bankingsystem.Bank.BankAccountsParameters.BankAccountsParameters;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Сущность центрального банка. Хранит список существующих банков.
 * Создает банки. Отправляет уведомления банкам о том, что нужно выплатить проценты или списать комиссию.
 * Обрабатывает транзакции.
 * Реализует паттерн Singleton.
 */
public class CentralBank {
    private static  final CentralBank INSTANCE = new CentralBank();
    private final Map<UUID, Bank> banks = new HashMap<>();
    private CentralBankNotifier notifier;

    private CentralBank() {}

    public static CentralBank getInstance() {
        return INSTANCE;
    }

    public Bank getBankById(UUID bankId) {
        return banks.get(bankId);
    }

    public Bank createBank(BankAccountsParameters parameters) {
        var newBank = new Bank(parameters);
        banks.put(newBank.getId(), newBank);
        return newBank;
    }

    public void notifyBanks(BankNotificationsType notificationsType) {
        notifier.sendNotifications(banks, notificationsType);
    }

    public void processTransaction(ITransaction transaction) {
        transaction.execute();
    }


}
