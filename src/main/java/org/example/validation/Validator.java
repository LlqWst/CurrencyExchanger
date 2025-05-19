package org.example.validation;

import org.example.handler.custom_exceptions.BadRequestException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

import static org.example.handler.ErrorMessages.*;

public class Validator {

    private static final Pattern CODE_PATTERN = Pattern.compile("[A-Z]{3}");
    private static final Pattern NAME_PATTERN = Pattern.compile("^(?!\\s*$)[a-zA-Z ]{1,46}$");
    private static final Pattern SIGN_PATTERN = Pattern.compile("\\S{1,5}");
    private static final Pattern PAIR_PATTERN = Pattern.compile("[A-Z]{6}");

    public void validateCode(String code) {
        if (!CODE_PATTERN.matcher(code).matches()){
            throw new BadRequestException(INCORRECT_CODE.getMessage());
        }
    }

    public void validateName(String name) {
        if(!NAME_PATTERN.matcher(name).matches()){
            throw new BadRequestException(INCORRECT_NAME.getMessage());
        }
    }

    public void validateSign(String sign) {
        if(!SIGN_PATTERN.matcher(sign).matches()){
            throw new BadRequestException(INCORRECT_SIGN.getMessage());
        }
    }

    public void validatePair(String pair) {
        if (!PAIR_PATTERN.matcher(pair).matches()){
            throw new BadRequestException(INCORRECT_PAIR.getMessage());
        }
    }

    public BigDecimal parsRate(String rate){
        try {
            rate = rate.replace(",", ".");
            int dotIndex = rate.indexOf('.');
            if (dotIndex != -1 && rate.length() - dotIndex - 1 > 6) {
                throw new BadRequestException(INCORRECT_RATE.getMessage());
            }
            BigDecimal result = new BigDecimal(rate)
                    .setScale(6, RoundingMode.HALF_UP)
                    .stripTrailingZeros();
            BigDecimal min = new BigDecimal("0.000001");
            BigDecimal max = new BigDecimal("999999999.999999");
            if(result.compareTo(min) < 0 || result.compareTo(max) > 0){
                throw new BadRequestException(INCORRECT_RATE.getMessage());
            }
            return result;
        } catch (Exception e){
            throw new BadRequestException(INCORRECT_RATE.getMessage());
        }

    }

    public String patchParsRate(String str) {
        String key = "rate=";
        String[] params = str.split("&");
        for (String param : params) {
            if (param.startsWith(key)) {
                return param.substring(key.length())
                        .replace("%2C", ".");
            }
        }
        throw new BadRequestException(MISSING_PARAMETERS.getMessage() + "rate");
    }

    public void validateParameter(String param, String paramName) {
        if (param == null) {
            throw new BadRequestException(MISSING_PARAMETERS.getMessage() + paramName);
        }
    }

}
