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
        if(code == null){
            throw new BadRequestException(MISSING_PARAMETERS.getMessage() + "code");
        }
        if (!CODE_PATTERN.matcher(code).matches()){
            throw new BadRequestException(INCORRECT_CODE.getMessage());
        }
    }

    public void validateName(String name) {
        if(name == null){
            throw new BadRequestException(MISSING_PARAMETERS.getMessage() + "name");
        }
        if(!NAME_PATTERN.matcher(name).matches()){
            throw new BadRequestException(INCORRECT_NAME.getMessage());
        }
    }

    public void validateSign(String sign) {
        if(sign == null){
            throw new BadRequestException(MISSING_PARAMETERS.getMessage() + "sign");
        }
        if(!SIGN_PATTERN.matcher(sign).matches()){
            throw new BadRequestException(INCORRECT_SIGN.getMessage());
        }
    }

    public void validatePair(String pair) {
        if(pair == null){
            throw new BadRequestException(MISSING_PARAMETERS.getMessage() + "pair");
        }
        if (!PAIR_PATTERN.matcher(pair).matches()){
            throw new BadRequestException(INCORRECT_PAIR.getMessage());
        }
    }

    public BigDecimal parsRate(String rate){
        if(rate == null){
            throw new BadRequestException(MISSING_PARAMETERS.getMessage() + "rate");
        };
        try {
            rate = rate.replace(",", ".");
            BigDecimal result = new BigDecimal(rate);
            result = result.setScale(6, RoundingMode.HALF_UP);
            BigDecimal min = new BigDecimal("0.000001");
            BigDecimal max = new BigDecimal("999999999");
            if(result.compareTo(min) < 0 || result.compareTo(max) > 0){
                throw new BadRequestException(INCORRECT_RATE.getMessage());
            }
            return result;
        } catch (Exception e){
            throw new BadRequestException(INCORRECT_RATE.getMessage());
        }

    }

}
