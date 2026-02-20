package com.fdsa.altairis.exception;

public class RepositoryException extends RuntimeException{

    private final String code;

    public RepositoryException(String code, String message){
        super(message);
        this.code = code;
    }

    public RepositoryException(String code, String message, Throwable cause){
        super(message,cause);
        this.code = code;
    }
}
