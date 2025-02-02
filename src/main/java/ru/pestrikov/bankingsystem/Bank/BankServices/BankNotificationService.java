package ru.pestrikov.bankingsystem.Bank.BankServices;

import ru.pestrikov.bankingsystem.Bank.BankAccountsParameters.DepositAccountInterestsDto;
import ru.pestrikov.bankingsystem.Client.Client;
import ru.pestrikov.bankingsystem.Account.AccountType;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Сервис банка для отправки уведомлений.
 * Хранит списки подписчиков на уведомления о определенном типе счета.
 * При изменении параметров счета отправляет подписчикам уведомления об изменениях.
 * Реализует паттер Observer.
 */
@AllArgsConstructor
public class BankNotificationService {
    private final Map<UUID, Client> debitAccountsSubscribers = new HashMap<>();
    private final Map<UUID, Client> depositAccountsSubscribers = new HashMap<>();
    private final Map<UUID, Client> creditAccountsSubscribers = new HashMap<>();

    public void addClientToSubscriptionList(Client client, AccountType accountType) {
        switch (accountType) {
            case CreditAccount -> creditAccountsSubscribers.put(client.getId(), client);
            case DebitAccount -> debitAccountsSubscribers.put(client.getId(), client);
            case DepositAccount -> depositAccountsSubscribers.put(client.getId(), client);
        }
    }

    public void notifyAboutDebitAccountInterestChanges(Double newDebitAccountInterest) {
        var notificationString = "New debit account interest is " + newDebitAccountInterest.toString() + " %";

        for (var entrySet : debitAccountsSubscribers.entrySet()) {
            var client = entrySet.getValue();
            client.notificationService.doNotify(notificationString);
        }
    }

    public void notifyAboutCreditAccountCommissionChanges(Double newCreditAccountCommission) {
        var notificationString = "New credit account commission is " + newCreditAccountCommission.toString() + " %";

        for (var entrySet : creditAccountsSubscribers.entrySet()) {
            var client = entrySet.getValue();
            client.notificationService.doNotify(notificationString);
        }
    }

    public void notifyCreditAccountLimitChanges(Double newCreditAccountLimit) {
        var notificationString = "New credit account commission is " + newCreditAccountLimit.toString() + " %";

        for (var entrySet : creditAccountsSubscribers.entrySet()) {
            var client = entrySet.getValue();
            client.notificationService.doNotify(notificationString);
        }
    }

    public void notifyDepositAccountInterestsChanges(DepositAccountInterestsDto newDepositAccountInterests) {
        StringBuilder sb = new StringBuilder();

        sb.append("New deposit account interests are:\n");
        sb.append("Deposit sum\t\tInterests\n");

        var borders = newDepositAccountInterests.getBorders();
        var interests = newDepositAccountInterests.getInterests();

        sb.append("< ").append(borders.getFirst()).append("\t\t").append(interests.getFirst()).append("\n");
        for (int i = 1; i < borders.size(); i++) {
            sb.append(borders.get(i - 1)).append(" - ").append(borders.get(i)).append("\t\t").append(interests.get(i)).append("\n");
        }
        sb.append("> ").append(borders.getLast()).append("\t\t").append(interests.getLast()).append("\n");

        var notificationString = sb.toString();

        for (var entrySet : depositAccountsSubscribers.entrySet()) {
            var client = entrySet.getValue();
            client.notificationService.doNotify(notificationString);
        }
    }
}
