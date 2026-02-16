package repository.inmemory;

import domain.BankAccount;
import repository.AccountRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAccountRepository implements AccountRepository {
    private final Map<String, BankAccount> storage = new ConcurrentHashMap<>();

    @Override
    public BankAccount save(BankAccount account) {
        storage.put(account.getId(), account);
        return account;
    }

    @Override
    public Optional<BankAccount> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<BankAccount> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(String id) {
        storage.remove(id);
    }

    @Override
    public boolean exists(String id) {
        return storage.containsKey(id);
    }
}