package org.example.handler;

public enum ErrorMessages {
    INCORRECT_URL("URL not found"),
    INCORRECT_PATH_VARIABLES("Only one path segment are supported"),
    INCORRECT_PARAMETER("Incorrect parameter"),
    MOT_EXIST_CURRENCY("Currency not found"),
    EXIST_CURRENCY("Currency already exist"),
    EXIST_PAIR("Pair already exist"),
    INTERNAL_ERROR("Data Base is busy"),
    MISSING_CURRENCY("Currency missing in URL"),
    MISSING_PARAMETERS("Parameter are missing: "),
    INCORRECT_NAME("Key 'name' supports size from 1 to 46 chars, latin only"),
    INCORRECT_SIGN("Key 'sign' supports size from 1 to 5 char"),
    INCORRECT_CODE("Key 'currency code' supports only latina UPPER CASE, size is 3"),
    INCORRECT_RATE("Key 'rate' supports range from 0.000001 to 999,999,999.999999"),
    INCORRECT_METHOD("Method are not supported"),
    INCORRECT_PAIR("Key 'pair' supports only latina UPPER CASE, size is 6"),
    NOT_FOUND_PAIR("The exchange rate for the pair was not found"),
    MISSING_PAIR("Pair of currencies are missing");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}