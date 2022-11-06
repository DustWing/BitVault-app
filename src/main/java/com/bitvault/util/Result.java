package com.bitvault.util;

import java.util.function.Consumer;
import java.util.function.Function;

public final class Result<T> {


    public static final Result<Boolean> Success = Result.value(Boolean.TRUE);
    public static final Result<Boolean> Fail = Result.value(Boolean.FALSE);


    private final T value;
    private final Exception exception;

    private final boolean isFail;

    public static <T> Result<T> exception(final Exception exception) {
        return new Result<>(exception);
    }

    public static <T> Result<T> value(final T value) {
        return new Result<>(value);
    }

    private Result(final T value) {

        this.value = value;
        this.exception = null;
        this.isFail = false;
    }

    private Result(final Exception exception) {
        this.value = null;
        this.exception = exception;
        this.isFail = true;
    }

    public T getOrThrow() {
        if (exception != null) {
            throw new RuntimeException(exception);
        }
        return value;
    }

    public boolean isFail() {
        return isFail;
    }

    public boolean isSuccess() {
        return !isFail;
    }

    public Exception getException() {
        return this.exception;
    }

    public T apply(
            Function<T, T> onSuccess,
            Function<Exception, T> onFail

    ) {

        if (exception == null) {
            return onSuccess.apply(value);
        } else {
            return onFail.apply(exception);
        }

    }


}
