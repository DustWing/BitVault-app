package com.bitvault.services.exceptions;

public class CategoryException extends RuntimeException{

    public static CategoryException notFound(){
       return new CategoryException("no.category.found");
    }

    public static CategoryException cannotDelete(){
        return new CategoryException("category.must.be.empty.to.delete");
    }

    public CategoryException() {
        super();
    }

    public CategoryException(String message) {
        super(message);
    }

    public CategoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public CategoryException(Throwable cause) {
        super(cause);
    }

    protected CategoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
