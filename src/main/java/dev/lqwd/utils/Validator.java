package dev.lqwd.utils;

import dev.lqwd.dto.CurrencyRequestDto;
import dev.lqwd.exception.BadRequestException;

import java.util.regex.Pattern;

public class Validator {

    private static final Pattern CODE_PATTERN = Pattern.compile("[A-Z]{3}");
    private static final Pattern NAME_PATTERN = Pattern.compile("^(?!\\s*$)[a-zA-Z ]{1,46}$");
    private static final Pattern SIGN_PATTERN = Pattern.compile("\\S{1,5}");
    private static final String IDENTICAL_CURRENCIES = "Currencies should be different";
    private static final String INCORRECT_SIGN = "Form 'sign' supports size from 1 to 5 char.";
    private static final String INCORRECT_NAME = "Form 'name' supports size from 1 to 46 chars, latin only.";
    private static final String INCORRECT_CODE = "Form 'currency code' supports only latina UPPER CASE, size is 3.";
    private static final String MISSING_PARAMETERS = "One of form parameter are missing: %s";
    private static final String SPACE = " ";

    public static void validateParameter(String param, String paramName) {
        if (param == null || param.isEmpty()) {
            throw new BadRequestException(String.format(MISSING_PARAMETERS, paramName));
        }
    }

    public static void validate(String code) {

        validateParameter(code, "CurrencyCode");

        if (isIncorrectCode(code)) {
            throw new BadRequestException(INCORRECT_CODE);
        }
    }

    public static void validate(CurrencyRequestDto dto) {

        String errorMessage = "";

        validateParameter(dto.getCode(), "CurrencyCode");
        validateParameter(dto.getName(), "Name");
        validateParameter(dto.getSign(), "Sign");

        if (isIncorrectCode(dto.getCode())) {
            errorMessage = INCORRECT_CODE + SPACE;
        }
        if (isIncorrectName(dto.getName())) {
            errorMessage += INCORRECT_NAME + SPACE;
        }
        if (isIncorrectSign(dto.getSign())) {
            errorMessage += INCORRECT_SIGN;
        }
        if (!errorMessage.isEmpty()) {
            throw new BadRequestException(errorMessage);
        }
    }

    public static void validate(String base, String target) {

        validate(base);
        validate(target);

        if (base.equals(target)) {
            throw new BadRequestException(IDENTICAL_CURRENCIES);
        }
    }

    private static boolean isIncorrectCode(String code) {

        return !CODE_PATTERN.matcher(code).matches();
    }

    private static boolean isIncorrectName(String name) {

        return !NAME_PATTERN.matcher(name).matches();
    }

    private static boolean isIncorrectSign(String sign) {

        return !SIGN_PATTERN.matcher(sign).matches();
    }

}
