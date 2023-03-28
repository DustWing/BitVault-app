package com.bitvault;

public class BvServiceException extends RuntimeException{
    public BvServiceException() {
        super();
    }

    public BvServiceException(String message) {
        super(message);
    }

    public BvServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public BvServiceException(Throwable cause) {
        super(cause);
    }
}
