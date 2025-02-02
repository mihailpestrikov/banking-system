package ru.pestrikov.bankingsystem.Client;

import ru.pestrikov.bankingsystem.Exceptions.UnverifiedAccountLimit;
import ru.pestrikov.bankingsystem.Account.AccountType;
import ru.pestrikov.bankingsystem.Account.IAccount;
import ru.pestrikov.bankingsystem.CentralBank.CentralBank;
import ru.pestrikov.bankingsystem.Exceptions.NotEnoughRightsToUseThisAccount;
import ru.pestrikov.bankingsystem.Transaction.TopUpTransaction;
import ru.pestrikov.bankingsystem.Transaction.TransferTransaction;
import ru.pestrikov.bankingsystem.Transaction.WithdrawalTransaction;
import ru.pestrikov.bankingsystem.Bank.Bank;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Сущность пользователя банка
 * Функционал: взаимодействие со счетом (пополнение, перевод, снятие),
 * Создание нового счета, подписка на уведомления о счете.
 */
public class Client {
    @Getter
    private final UUID id = UUID.randomUUID();
    private final Bank bank;
    private final String firstName;
    private final String lastName;
    private String address;
    private String passport;
    @Getter
    private boolean verified = false;
    private final Map<UUID, IAccount> clientAccounts = new HashMap<>();

    @Setter
    public IClientNotificationService notificationService = new ConsoleNotificationService();

    public Client(Bank bank, String firstName, String lastName) {
        this.bank = bank;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void transfer(UUID senderAccountId, UUID receiverBankId, UUID receiverAccountId, Double amount) throws NotEnoughRightsToUseThisAccount {
        var transaction = new TransferTransaction(bank.getId(), senderAccountId, receiverBankId, receiverAccountId, amount);

        if (!verified) {
            if (sumIsGreaterThanLimit(amount)) {
                notificationService.doNotify("Unverified users are not allowed to transfer more than" +
                        bank.accountsParameters.getMaxWithdrawOfUnverifiedAccount().toString());
                return;
            }
        }

        if (!clientAccounts.containsKey(senderAccountId)) {
            throw new NotEnoughRightsToUseThisAccount(senderAccountId.toString());
        }

        CentralBank.getInstance().processTransaction(transaction);
    }

    public void withdraw(UUID accountId, Double amount) throws NotEnoughRightsToUseThisAccount, UnverifiedAccountLimit {
        var transaction = new WithdrawalTransaction(bank.getId(), accountId, amount);

        if (!verified) {
            if (sumIsGreaterThanLimit(amount)) {
                throw new UnverifiedAccountLimit("Unverified users are not allowed to withdraw more than " +
                        bank.accountsParameters.getMaxWithdrawOfUnverifiedAccount().toString());
            }
        }

        if (!clientAccounts.containsKey(accountId)) {
            throw new NotEnoughRightsToUseThisAccount(accountId.toString());
        }

        CentralBank.getInstance().processTransaction(transaction);
    }

    public void topUp(UUID accountId, Double amount) {
        var transaction = new TopUpTransaction(accountId, bank.getId(), amount);

        CentralBank.getInstance().processTransaction(transaction);
    }

    public UUID createAccount(AccountType accountType) {
        IAccount account;
        switch (accountType) {
            case DebitAccount -> account = bank.accountFactory.createDebitAccount(this);
            case CreditAccount -> account = bank.accountFactory.createCreditAccount(this);
            case DepositAccount -> account = bank.accountFactory.createDepositAccount(this);
            default -> account = null;
        }

        clientAccounts.put(account.getId(), account);
        return account.getId();
    }

    public Client setAddress(String address_string) {
        address = address_string;
        checkVerification();
        return this;
    }

    public Client setPassport(String passport_string) {
        passport = passport_string;
        checkVerification();
        return this;
    }

    public void subscribeToNotifications(AccountType accountType) {
        bank.notificationService.addClientToSubscriptionList(this, accountType);
    }

    public IAccount getAccount(UUID accountId) {
        return clientAccounts.get(accountId);
    }

    private void checkVerification() {
        if (address != null && passport != null) {
            verified = true;
        }
    }

    private boolean sumIsGreaterThanLimit(Double amount) {
        return amount > bank.accountsParameters.getMaxWithdrawOfUnverifiedAccount();
    }
}
