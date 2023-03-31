package com.bitvault.ui.async;

public class AsyncTaskException extends RuntimeException{
    public AsyncTaskException() {
        super();
    }

    public AsyncTaskException(String message) {
        super(message);
    }

    public AsyncTaskException(String message, Throwable cause) {
        super(message, cause);
    }

    public AsyncTaskException(Throwable cause) {
        super(cause);
    }
}
