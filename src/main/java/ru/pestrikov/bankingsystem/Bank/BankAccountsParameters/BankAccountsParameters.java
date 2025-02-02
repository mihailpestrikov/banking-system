package ru.pestrikov.bankingsystem.Bank.BankAccountsParameters;

import ru.pestrikov.bankingsystem.Bank.BankServices.BankNotificationService;
import lombok.Getter;
import lombok.Setter;


/**
 * Data Transfer Object банка для передачи всех параметров счета в одном объекте.
 * Написаны кастомные сеттеры для отправки уведомлений при изменении определенных параметров.
 */
@Getter
public class BankAccountsParameters {
    private Double debitAccountInterest;
    private Double creditAccountCommission;
    private Double creditAccountLimit;
    @Setter
    private Double maxWithdrawOfUnverifiedAccount;
    private DepositAccountInterestsDto depositAccountInterests;
    private BankNotificationService notificationService;

    public BankAccountsParameters(Double debitAccountInterest, Double creditAccountCommission, Double creditAccountLimit, Double maxWithdrawOfSuspiciousAccount, DepositAccountInterestsDto depositAccountInterests) {
        this.debitAccountInterest = debitAccountInterest;
        this.creditAccountCommission = creditAccountCommission;
        this.creditAccountLimit = creditAccountLimit;
        this.maxWithdrawOfUnverifiedAccount = maxWithdrawOfSuspiciousAccount;
        this.depositAccountInterests = depositAccountInterests;
    }

    public void setLinkToNotificationService(BankNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void setDebitAccountInterest(Double debitAccountInterest) {
        this.debitAccountInterest = debitAccountInterest;
        notificationService.notifyAboutDebitAccountInterestChanges(debitAccountInterest);
    }

    public void setCreditAccountCommission(Double creditAccountCommission) {
        this.creditAccountCommission = creditAccountCommission;
        notificationService.notifyAboutCreditAccountCommissionChanges(creditAccountCommission);
    }

    public void setCreditAccountLimit(Double creditAccountLimit) {
        this.creditAccountLimit = creditAccountLimit;
        notificationService.notifyCreditAccountLimitChanges(creditAccountLimit);
    }

    public void setDepositAccountInterests(DepositAccountInterestsDto depositAccountInterests) {
        this.depositAccountInterests = depositAccountInterests;
        notificationService.notifyDepositAccountInterestsChanges(depositAccountInterests);
    }
}
