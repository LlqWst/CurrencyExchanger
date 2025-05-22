package dev.lqwd.exceptions.custom_exceptions;

import jakarta.servlet.http.HttpServletResponse;
import dev.lqwd.exceptions.CurrenciesExceptions;

public class NotFoundException extends CurrenciesExceptions {
    public NotFoundException(String message) {
        super(HttpServletResponse.SC_NOT_FOUND, message);
    }

    public NotFoundException() {
        super(HttpServletResponse.SC_NOT_FOUND);
    }
}
