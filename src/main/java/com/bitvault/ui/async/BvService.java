package com.bitvault.ui.async;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BvService<V> extends Service<V> {

    private final Supplier<V> supplier;

    public static <V> BvService<V> create(Supplier<V> supplier) {
        final BvService<V> service = new BvService<>(supplier);
        return service;

    }

    public BvService(Supplier<V> supplier) {
        this.supplier = supplier;
    }

    public BvService<V> onSuccess(Consumer<V> consumer) {
        setOnSucceeded(event -> consumer.accept(this.getValue()));
        return this;
    }

    public BvService<V> onFailure(Consumer<BvServiceException> onException) {
        setOnFailed(event -> onException.accept(new BvServiceException(this.getException())));
        return this;
    }


    @Override
    protected Task<V> createTask() {
        final Task<V> task = new Task<>() {
            @Override
            protected V call() {
                // Perform long-running task here
                return supplier.get();
            }
        };
        return task;
    }
}
