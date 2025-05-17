package org.example.handler.custom_exceptions;

import jakarta.servlet.http.HttpServletResponse;
import org.example.handler.CurrenciesExceptions;

public class NotFoundException extends CurrenciesExceptions {
    public NotFoundException(String message) {
        super(HttpServletResponse.SC_NOT_FOUND, message);
    }

    public NotFoundException() {
        super(HttpServletResponse.SC_NOT_FOUND);
    }
}
