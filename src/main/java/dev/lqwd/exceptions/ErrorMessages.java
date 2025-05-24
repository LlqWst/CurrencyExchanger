package dev.lqwd.exceptions;

public enum ErrorMessages {
    INCORRECT_URL("URL not found"),
    INCORRECT_PATH_VARIABLES("Only one path segment are supported"),
    INCORRECT_PARAMETER("Incorrect parameter"),
    INCORRECT_CONVERSION_INPUT("This currency pair does not exist. There is no cross-course set"),
    NOT_EXIST_CURRENCY("Currency not found: "),
    EXIST_CURRENCY("Currency already exist: "),
    EXIST_PAIR("Pair already exist"),
    INTERNAL_ERROR("Data Base is busy"),
    MISSING_CURRENCY("Currency missing in URL"),
    MISSING_PARAMETERS("One of form parameter are missing."),
    INCORRECT_NAME("Form 'name' supports size from 1 to 46 chars, latin only."),
    INCORRECT_SIGN("Form 'sign' supports size from 1 to 5 char."),
    INCORRECT_CODE("Form 'currency code' supports only latina UPPER CASE, size is 3."),
    INCORRECT_RATE("Form 'rate' supports range from 0.000001 to 999,999.999999."),
    INCORRECT_AMOUNT("Form 'amount' supports range from 0.000001 to 9,999,999.999999."),
    INCORRECT_METHOD("Method are not supported"),
    INCORRECT_PAIR("Form 'pair' supports only latina UPPER CASE, size is 6."),
    IDENTICAL_CURRENCIES("Currencies should be different"),
    NOT_FOUND_PAIR("The exchange rate for the pair was not found"),
    MISSING_PAIR("Pair of currencies are missing in URL");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}