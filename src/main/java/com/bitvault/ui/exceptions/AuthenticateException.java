package com.bitvault.ui.exceptions;

public class AuthenticateException extends RuntimeException{

    public static AuthenticateException INSTANCE = new AuthenticateException();

    public AuthenticateException() {
        super();
    }

    public AuthenticateException(String message) {
        super(message);
    }

    public AuthenticateException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticateException(Throwable cause) {
        super(cause);
    }
}
