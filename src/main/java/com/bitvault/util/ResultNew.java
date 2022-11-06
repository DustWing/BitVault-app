package com.bitvault.util;

public sealed interface ResultNew<T> permits Success, Fail {

    T getValue();

    static Success success() {

        return new Success();
    }

}

final class Success<T> implements ResultNew<T> {

    @Override
    public T getValue() {
        return null;
    }
}

final class Fail implements ResultNew<Exception> {

    @Override
    public Exception getValue() {
        return null;
    }
}