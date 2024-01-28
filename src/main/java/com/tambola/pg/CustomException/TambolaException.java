package com.tambola.pg.CustomException;



public class TambolaException extends RuntimeException {

    public TambolaException(String message) {
        super(message);
    }

    public TambolaException(String message, Throwable cause) {
        super(message, cause);
    }
}

