package di;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DIContainer {
    private final Map<Class<?>, Supplier<?>> suppliers = new HashMap<>();
    private final Map<Class<?>, Object> singletons = new HashMap<>();

    public <T> void register(Class<T> type, Supplier<T> supplier) {
        suppliers.put(type, supplier);
    }

    public <T> void registerSingleton(Class<T> type, T instance) {
        singletons.put(type, instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> type) {
        if (singletons.containsKey(type)) {
            return (T) singletons.get(type);
        }
        Supplier<?> supplier = suppliers.get(type);
        if (supplier == null) {
            throw new IllegalArgumentException("No registered supplier for " + type.getName());
        }
        return (T) supplier.get();
    }
}