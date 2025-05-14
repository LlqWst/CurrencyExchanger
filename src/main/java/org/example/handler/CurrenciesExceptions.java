package org.example.handler;

public class CurrenciesExceptions extends RuntimeException {

    private final int statusCode;

    public CurrenciesExceptions(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

}

