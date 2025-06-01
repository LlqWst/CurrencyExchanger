package dev.lqwd.utility;

import dev.lqwd.dto.CurrencyRequestDto;
import dev.lqwd.exceptions.BadRequestException;

import java.util.regex.Pattern;

import static dev.lqwd.exceptions.ErrorMessages.*;

public class Validator {

    private static final Pattern CODE_PATTERN = Pattern.compile("[A-Z]{3}");
    private static final Pattern NAME_PATTERN = Pattern.compile("^(?!\\s*$)[a-zA-Z ]{1,46}$");
    private static final Pattern SIGN_PATTERN = Pattern.compile("\\S{1,5}");
    private static final String SPACE = " ";

    public static void validateParameter(String param) {
        if (param == null || param.isEmpty()) {
            throw new BadRequestException(MISSING_PARAMETERS.getMessage());
        }
    }

    public static void validateParameter(String param, String paramName) {
        if (param == null || param.isEmpty()) {
            throw new BadRequestException(MISSING_PARAMETERS.getMessage() + SPACE + paramName);
        }
    }

    public static void validate(String code) {
        validateParameter(code, "CurrencyCode");
        if (isIncorrectCode(code)){
            throw new BadRequestException(INCORRECT_CODE.getMessage());
        }
    }

    public static void validate(CurrencyRequestDto dto){
        String errorMessage = "";

        validateParameter(dto.getCode(), "CurrencyCode");
        validateParameter(dto.getName(), "Name");
        validateParameter(dto.getSign(), "Sign");

        if (isIncorrectCode(dto.getCode())){
            errorMessage = INCORRECT_CODE.getMessage() + SPACE;
        }
        if(isIncorrectName(dto.getName())){
            errorMessage += INCORRECT_NAME.getMessage() + SPACE;
        }
        if(isIncorrectSign(dto.getSign())){
            errorMessage += INCORRECT_SIGN.getMessage();
        }
        if (!errorMessage.isEmpty()){
            throw new BadRequestException(errorMessage);
        }
    }

    public static void validate(String base, String target) {
        if(isIncorrectCode(base) || isIncorrectCode(target)){
            throw new BadRequestException(INCORRECT_CODE.getMessage());
        }
        if(base.equals(target)){
            throw new BadRequestException(IDENTICAL_CURRENCIES.getMessage());
        }
    }

    public static void validatePathVariable(String pathInfo){
        if(pathInfo != null){
            throw new BadRequestException(INCORRECT_PATH_VARIABLES.getMessage());
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
