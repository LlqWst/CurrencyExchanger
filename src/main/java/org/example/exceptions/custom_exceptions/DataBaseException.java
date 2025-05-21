package org.example.exceptions.custom_exceptions;

import jakarta.servlet.http.HttpServletResponse;
import org.example.exceptions.CurrenciesExceptions;

public class DataBaseException extends CurrenciesExceptions {
    public DataBaseException(String message) {
        super(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
    }

    public DataBaseException() {
        super(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

}
