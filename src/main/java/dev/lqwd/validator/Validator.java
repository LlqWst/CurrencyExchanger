package dev.lqwd.validator;

import dev.lqwd.exceptions.custom_exceptions.BadRequestException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

import static dev.lqwd.exceptions.ErrorMessages.*;

public class Validator {

    private static final Pattern CODE_PATTERN = Pattern.compile("[A-Z]{3}");
    private static final Pattern NAME_PATTERN = Pattern.compile("^(?!\\s*$)[a-zA-Z ]{1,46}$");
    private static final Pattern SIGN_PATTERN = Pattern.compile("\\S{1,5}");
    private static final Pattern PAIR_PATTERN = Pattern.compile("[A-Z]{6}");

    public static final int SCALE = 6;

    public void validateParameter(String param, String paramName) {
        if (param == null) {
            throw new BadRequestException(MISSING_PARAMETERS.getMessage() + paramName);
        }
    }

    public void validatePair(String pair) {
        if (!PAIR_PATTERN.matcher(pair).matches()){
            throw new BadRequestException(INCORRECT_PAIR.getMessage());
        }
        String base = pair.substring(0, 3);
        String target = pair.substring(3, 6);
        this.validateOnEquals(base, target);
    }

    public void validatePair(String base, String target) {
        validateOnEquals(base, target);
        this.validateCode(base);
        this.validateCode(target);
    }

    private void validateOnEquals(String base, String target){
        if (base.equals(target)){
            throw new BadRequestException(IDENTICAL_CURRENCIES.getMessage());
        }
    }

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

    public BigDecimal parsRate(String rate){
        try {
            BigDecimal min = new BigDecimal("0.000001");
            BigDecimal max = new BigDecimal("999999.999999");
            return parsValue(rate, min, max);
        } catch (Exception e){
            throw new BadRequestException(INCORRECT_RATE.getMessage());
        }
    }

    public BigDecimal parsAmount(String amount){
        try {
            BigDecimal min = new BigDecimal("0.000001");
            BigDecimal max = new BigDecimal("9999999.999999");
            return parsValue(amount, min, max);
        } catch (Exception e){
            throw new BadRequestException(INCORRECT_AMOUNT.getMessage());
        }
    }

    public BigDecimal patchParsRate(String str) {
        String key = "rate=";
        String[] params = str.split("&");
        for (String param : params) {
            if (param.startsWith(key)) {
                String rate = param.substring(key.length())
                        .replace("%2C", ".");
                return this.parsRate(rate);
            }
        } throw new BadRequestException(MISSING_PARAMETERS.getMessage() + "rate");
    }

    private BigDecimal parsValue(String value,BigDecimal min, BigDecimal max ){
        try {
            value = value.replace(",", ".");
            int dotIndex = value.indexOf('.');
            if (dotIndex != -1 && value.length() - dotIndex - 1 > 6) {
                throw new BadRequestException();
            }
            BigDecimal result = new BigDecimal(value)
                    .setScale(SCALE, RoundingMode.HALF_UP)
                    .stripTrailingZeros();
            if(result.compareTo(min) < 0 || result.compareTo(max) > 0){
                throw new BadRequestException();
            }
            return result;
        } catch (Exception e){
            throw new BadRequestException();
        }
    }

}
