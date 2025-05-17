package org.example.dto;

import org.example.handler.custom_exceptions.BadRequestException;

import java.util.regex.Pattern;

import static org.example.handler.ErrorMessages.*;

public class ExchangePairDto {
    private static final Pattern CODE_PATTERN = Pattern.compile("[A-Z]{6}");
    private static final Pattern RATE_PATTERN = Pattern.compile("^(?!0+\\.?0*$)(?!1\\d{9}|[2-9]\\d{8,})([0-9]{1,9}|[0-9]{1,9}\\.[0-9]{1,6})$");

    private String baseCurrency;
    private String targetCurrency;
    private String rate;

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setPair(String pair) {
        if(pair == null){
            throw new BadRequestException(MISSING_PARAMETERS.getMessage() + "pair");
        }
        if (!CODE_PATTERN.matcher(pair).matches()){
            throw new BadRequestException(INCORRECT_PAIR.getMessage());
        }
        this.baseCurrency = pair.substring(0,3);
        this.targetCurrency = pair.substring(3,6);
    }

    public void setPair(String baseCurrency, String targetCurrency){
        String pair = baseCurrency + targetCurrency;
        this.setPair(pair);
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        if(rate == null){
            throw new BadRequestException(MISSING_PARAMETERS.getMessage() + "rate");
        }
        double result = Double.parseDouble(rate);
        if(!RATE_PATTERN.matcher(rate).matches() &&
                (result < 0.000001 || result > 999_999_999)){
            throw new BadRequestException(INCORRECT_RATE.getMessage());
        }
        this.rate = rate;
    }
}
