package factory;

import domain.BankAccount;
import java.util.UUID;

public class DefaultBankAccountFactory implements BankAccountFactory {
    @Override
    public BankAccount create(String name, double balance) {
        return new BankAccount(UUID.randomUUID().toString(), name, balance);
    }
}