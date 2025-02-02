package ru.pestrikov.bankingsystem.CentralBank;

import ru.pestrikov.bankingsystem.Bank.Bank;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * Сервис центрального банка для отправки уведомлений.
 */
@AllArgsConstructor
public class CentralBankNotifier {
    public void sendNotifications(Map<UUID, Bank> banks, BankNotificationsType notificationsType) {
        for (var entry : banks.entrySet()) {
            var bank = entry.getValue();

            switch (notificationsType) {
                case AccrueInterest -> bank.commissionAndInterestAccrueService.receiveAccrueOfInterestNotification();
                case ChargeCommission -> bank.commissionAndInterestAccrueService.receiveChargeCommissionNotification();
            }
        }
    }
}
