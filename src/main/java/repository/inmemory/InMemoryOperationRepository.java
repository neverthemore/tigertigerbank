package repository.inmemory;

import domain.Operation;
import repository.OperationRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOperationRepository implements OperationRepository {
    private final Map<String, Operation> storage = new ConcurrentHashMap<>();

    @Override
    public Operation save(Operation operation) {
        storage.put(operation.getId(), operation);
        return operation;
    }

    @Override
    public Optional<Operation> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Operation> findAll() {
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