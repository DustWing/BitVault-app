package com.bitvault.util;

import java.util.function.Consumer;

public final class Result<T> {


    public static final Result<Boolean> Success = Result.value(Boolean.TRUE);
    public static final Result<Boolean> Fail = Result.value(Boolean.FALSE);


    private final T value;
    private final Exception exception;

    public static <T> Result<T> exception(final Exception exception) {
        return new Result<>(exception);
    }

    public static <T> Result<T> value(final T value) {
        return new Result<>(value);
    }

    private Result(final T value) {

        this.value = value;
        this.exception = null;
    }

    private Result(final Exception exception) {
        this.value = null;
        this.exception = exception;
    }

    public T get() {
        if (exception != null) {
            throw new RuntimeException(exception);
        }
        return value;
    }

    public void apply(
            Consumer<T> resultConsumer,
            Consumer<Exception> exceptionConsumer

    ) {

        if (exception == null) {
            resultConsumer.accept(value);
        } else {
            exceptionConsumer.accept(exception);
        }

    }

}
