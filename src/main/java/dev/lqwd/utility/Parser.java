package dev.lqwd.utility;

import dev.lqwd.exceptions.BadRequestException;

import java.math.BigDecimal;

public class Parser {

    private static final int SCALE = 6;

    public static BigDecimal parsRate(String rate){

        BigDecimal min = new BigDecimal("0.000001");
        BigDecimal max = new BigDecimal("999999.999999");
        return parsValue(rate, min, max, "rate");

    }

    public static BigDecimal parsAmount(String amount){

        BigDecimal min = new BigDecimal("0.000001");
        BigDecimal max = new BigDecimal("9999999.999999");
        return parsValue(amount, min, max, "amount");

    }

    private static BigDecimal parsValue(String value, BigDecimal min, BigDecimal max, String type){
        try {

            value = value.replace(",", ".");

            int dotIndex = value.indexOf('.');
            if (dotIndex != -1 && value.length() - dotIndex - 1 > SCALE) {
                throw new BadRequestException(String.format("maximum number of decimal places is %d", SCALE));
            }

            BigDecimal result = new BigDecimal(value).stripTrailingZeros();

            if(result.compareTo(min) < 0 || result.compareTo(max) > 0){
                throw new BadRequestException(String.format("Please provide %s from %s to %s",type, min, max));
            }

            return result;

        } catch (Exception e){
            throw new BadRequestException(String.format("Please provide %s from %s to %s",type, min, max));
        }
    }

}
