package factory;

import domain.BankAccount;

public interface BankAccountFactory {
    BankAccount create(String name, double balance);
}