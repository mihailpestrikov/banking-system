package ru.pestrikov.bankingsystem.Bank.BankServices;

import ru.pestrikov.bankingsystem.Account.IAccount;
import ru.pestrikov.bankingsystem.Account.IHaveInterest;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * Сервис банка работы с комиссиями.
 * Предоставляет методы для добавления процентов, начисления процентов на счет в конце месяца и списания комиссии.
 * Реализует паттерн Observer. Центральный банк отправляет цведомления о том, что нужно начислить проценты или списать комиссию.
 */
@AllArgsConstructor
public class CommissionAndInterestAccrueService {
    private final Map<UUID, IAccount> accounts;

    private void chargeCommissions() {
        // no commission
    }
    private void accrueOfInterest() {
        for (var entry : accounts.entrySet()) {
            var account = entry.getValue();
            if (account instanceof IHaveInterest a) {
                a.accrueOfInterest();
            }
        }
    }

    private void chargeInterest() {
        for (var entry : accounts.entrySet()) {
            var account = entry.getValue();
            if (account instanceof IHaveInterest a) {
                a.chargeDailyInterest();
            }
        }
    }

    public void receiveChargeCommissionNotification() {
        chargeCommissions();
    }

    public void receiveAccrueOfInterestNotification() {
        accrueOfInterest();
    }
}
