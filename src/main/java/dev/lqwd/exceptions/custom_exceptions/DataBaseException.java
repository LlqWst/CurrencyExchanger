package dev.lqwd.exceptions.custom_exceptions;

import jakarta.servlet.http.HttpServletResponse;
import dev.lqwd.exceptions.CurrenciesExceptions;

public class DataBaseException extends CurrenciesExceptions {
    public DataBaseException(String message) {
        super(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
    }

    public DataBaseException() {
        super(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

}
