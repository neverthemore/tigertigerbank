package repository;

import domain.Operation;
import java.util.List;
import java.util.Optional;

public interface OperationRepository {
    Operation save(Operation operation);
    Optional<Operation> findById(String id);
    List<Operation> findAll();
    void delete(String id);
    boolean exists(String id);
}