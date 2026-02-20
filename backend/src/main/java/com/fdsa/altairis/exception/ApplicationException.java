package com.fdsa.altairis.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final String code;

    public ApplicationException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ApplicationException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
