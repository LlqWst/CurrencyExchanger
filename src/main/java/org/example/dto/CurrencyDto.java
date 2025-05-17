package org.example.dto;

import org.example.handler.custom_exceptions.BadRequestException;

import java.util.regex.Pattern;

import static org.example.handler.ErrorMessages.*;

public class CurrencyDto {
    private static final Pattern CODE_PATTERN = Pattern.compile("[A-Z]{3}");
    private static final Pattern NAME_PATTERN = Pattern.compile("^(?!\\s*$)[a-zA-Z ]{1,46}$");
    private static final Pattern SIGN_PATTERN = Pattern.compile("\\S{1,5}");

    private int id;
    private String code;
    private String name;
    private String sign;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CurrencyDto() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if(code == null){
            throw new BadRequestException(MISSING_PARAMETERS.getMessage() + "code");
        }
        if (!CODE_PATTERN.matcher(code).matches()){
            throw new BadRequestException(INCORRECT_CODE.getMessage());
        }
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name == null){
            throw new BadRequestException(MISSING_PARAMETERS.getMessage() + "name");
        }
        if(!NAME_PATTERN.matcher(name).matches()){
            throw new BadRequestException(INCORRECT_NAME.getMessage());
        }
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        if(sign == null){
            throw new BadRequestException(MISSING_PARAMETERS.getMessage() + "sign");
        }
        if(!SIGN_PATTERN.matcher(sign).matches()){
            throw new BadRequestException(INCORRECT_SIGN.getMessage());
        }
        this.sign = sign;
    }
}
