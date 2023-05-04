package com.bitvault.ui.async;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class AsyncTask<V> extends Service<V> {

    private final Supplier<V> supplier;

    public static <V> AsyncTask<V> toRun(Supplier<V> supplier) {
        return new AsyncTask<>(supplier);

    }

    public AsyncTask(Supplier<V> supplier) {
        this.supplier = supplier;
    }

    public AsyncTask<V> onSuccess(Consumer<V> consumer) {
        setOnSucceeded(event -> consumer.accept(this.getValue()));
        return this;
    }

    public AsyncTask<V> onFailure(Consumer<AsyncTaskException> onException) {
        setOnFailed(event -> onException.accept(new AsyncTaskException(this.getException())));
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
