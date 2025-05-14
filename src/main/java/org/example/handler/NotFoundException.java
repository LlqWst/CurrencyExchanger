package org.example.handler;

import jakarta.servlet.http.HttpServletResponse;

public class NotFoundException extends CurrenciesExceptions {
    public NotFoundException(String message) {
        super(HttpServletResponse.SC_NOT_FOUND, message);
    }
}
