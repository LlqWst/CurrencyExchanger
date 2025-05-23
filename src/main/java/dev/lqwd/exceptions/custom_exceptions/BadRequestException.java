package dev.lqwd.exceptions.custom_exceptions;

import jakarta.servlet.http.HttpServletResponse;
import dev.lqwd.exceptions.CurrenciesExceptions;

public class BadRequestException extends CurrenciesExceptions {
    public BadRequestException(String message) {
        super(HttpServletResponse.SC_BAD_REQUEST, message);
    }

    public BadRequestException() {
        super(HttpServletResponse.SC_BAD_REQUEST);
    }

}
