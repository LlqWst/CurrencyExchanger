package org.example.handler;

import jakarta.servlet.http.HttpServletResponse;

public class BadRequestException extends CurrenciesExceptions {
    public BadRequestException(String message) {
        super(HttpServletResponse.SC_BAD_REQUEST, message);
    }
}
