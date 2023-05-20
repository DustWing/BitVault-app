package com.bitvault.util;

import java.util.Optional;
import java.util.function.Function;

public final class Result<T> {

    public static final Result<Boolean> Success = Result.ok(Boolean.TRUE);

    private final T value;
    private final Exception exception;
    private final boolean hasError;

    public static <T> Result<T> error(final Exception exception) {
        return new Result<>(null, exception);
    }

    public static <T> Result<T> ok(final T value) {
        return new Result<>(value, null);
    }

    private Result(final T value, final Exception exception) {

        this.value = value;
        this.exception = exception;
        this.hasError = exception != null;
    }

    public T get() {
        if (hasError) {
            throw new IllegalStateException(exception);
        }
        return value;
    }

    public Optional<T> getOpt() {
        if (hasError) {
            return Optional.empty();
        }

        return Optional.ofNullable(value);
    }

    public boolean hasError() {
        return hasError;
    }

    public boolean isSuccess() {
        return !hasError;
    }

    public Exception getError() {
        return this.exception;
    }

    public T apply(
            Function<T, T> onSuccess,
            Function<Exception, T> onError
    ) {

        if (exception == null) {
            return onSuccess.apply(value);
        } else {
            return onError.apply(exception);
        }

    }


}
