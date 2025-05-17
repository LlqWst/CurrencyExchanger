package org.example.handler.custom_exceptions;
import jakarta.servlet.http.HttpServletResponse;
import org.example.handler.CurrenciesExceptions;

public class ExistInDbException extends CurrenciesExceptions {

  public ExistInDbException(String message) {
    super(HttpServletResponse.SC_CONFLICT, message);
  }

  public ExistInDbException() {
    super(HttpServletResponse.SC_CONFLICT);
  }

}