package ru.pestrikov.bankingsystem.Bank;


import ru.pestrikov.bankingsystem.Bank.BankAccountsParameters.BankAccountsParameters;
import ru.pestrikov.bankingsystem.Bank.BankServices.*;
import ru.pestrikov.bankingsystem.Client.Client;
import ru.pestrikov.bankingsystem.Account.IAccount;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Сущность банка. Хранит пользователей и счеты.
 * Имеет сервисы создания счетов, регистрации пользователей, проведения транзакций,
 * списывания комиссий, ускорения времени, отправки уведомлений.
 */
public class Bank {
    @Getter
    private final UUID id = UUID.randomUUID();

    public final BankAccountsParameters accountsParameters;
    private final Map<UUID, IAccount> accounts = new HashMap<>();
    private final Map<UUID, Client> clients = new HashMap<>();

    public final BankNotificationService notificationService = new BankNotificationService();
    public final PredictionService predictionService = new PredictionService(accounts);
    public final CommissionAndInterestAccrueService commissionAndInterestAccrueService = new CommissionAndInterestAccrueService(accounts);
    public final TransactionService transactionService = new TransactionService(accounts);

    public final AccountFactory accountFactory = new AccountFactory(accounts, this);
    public final ClientBuilder clientBuilder = new ClientBuilder(this, clients);

    public Bank(BankAccountsParameters accountsParameters) {
        this.accountsParameters = accountsParameters;
        this.accountsParameters.setLinkToNotificationService(notificationService);
    }

    /* Сомнительный подход с инициализаций сервисов через =new(). Правильнее было бы сделать интерфейсы сервисов и передавать их через конструктор,
    чтобы можно было подставлять моки при тестировании.
    Не могу инициализировать сервисы через конструктор, потому что нужно передать зависимости на родительский класс или коллекцию.
    Решение, наверное, - сделать в сервисах сеттеры зависимостей (как для accountsParameters) и в конструкторе родительского класса сразу вызывать их.
    Не стал так делать, потому что для данной лабораторной работы over engineering */
}
