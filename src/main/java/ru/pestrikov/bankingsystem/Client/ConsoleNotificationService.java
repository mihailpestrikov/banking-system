package ru.pestrikov.bankingsystem.Client;


/**
 * Сервис, отправляющий уведомления пользователю через консоль
 */
public class ConsoleNotificationService implements IClientNotificationService {
    @Override
    public void doNotify(String notificationString) {
        System.out.println(notificationString);
    }
}
