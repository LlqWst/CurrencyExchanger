package org.example.handler;
import jakarta.servlet.http.HttpServletResponse;

public class CurrencyExistException extends CurrenciesExceptions {
  public CurrencyExistException(String message) {
    super(HttpServletResponse.SC_CONFLICT, message);
  }
}