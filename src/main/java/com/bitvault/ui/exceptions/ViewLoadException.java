package com.bitvault.ui.exceptions;

public class ViewLoadException extends RuntimeException{
    public ViewLoadException() {
        super();
    }

    public ViewLoadException(String message) {
        super(message);
    }

    public ViewLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ViewLoadException(Throwable cause) {
        super(cause);
    }
}
