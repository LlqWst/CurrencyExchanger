package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.entity.Currency;
import org.example.handler.custom_exceptions.BadRequestException;

import java.util.regex.Pattern;

import static org.example.handler.ErrorMessages.*;

public class ExchangeRateDto {

    private int id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private String rate;

    public ExchangeRateDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        try {
            this.id = id;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Currency baseCurrency) {
        try {
            this.baseCurrency = baseCurrency;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(Currency targetCurrency) {
        try {
            this.targetCurrency = targetCurrency;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        try {
            this.rate = rate;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
