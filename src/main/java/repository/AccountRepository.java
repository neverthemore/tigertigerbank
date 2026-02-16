package repository;

import domain.BankAccount;
import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    BankAccount save(BankAccount account);
    Optional<BankAccount> findById(String id);
    List<BankAccount> findAll();
    void delete(String id);
    boolean exists(String id);
}