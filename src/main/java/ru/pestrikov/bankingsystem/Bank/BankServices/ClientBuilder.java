package ru.pestrikov.bankingsystem.Bank.BankServices;

import ru.pestrikov.bankingsystem.Client.Client;
import ru.pestrikov.bankingsystem.Bank.Bank;

import java.util.Map;
import java.util.UUID;


/**
 * Сервис банка для создания клиентов.
 * Реализует паттерн Builder.
 */
public class ClientBuilder {
    private final Bank bank;
    private final Map<UUID, Client> clients;
    private String firstName;
    private String lastName;
    private String address;
    private String passport;

    public ClientBuilder(Bank bank, Map<UUID, Client> clients) {
        this.bank = bank;
        this.clients = clients;
    }

    public ClientBuilder createClient(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        return this;
    }

    public ClientBuilder withAddress(String address) {
        this.address = address;
        return this;
    }
    public ClientBuilder withPassport(String passport) {
        this.passport = passport;
        return this;
    }

    public Client execute() {
        var client = new Client(bank, firstName, lastName);

        if (address != null) {
            client.setAddress(address);
        }

        if (passport != null) {
            client.setPassport(passport);
        }

        clients.put(client.getId(), client);

        return client;
    }
}
