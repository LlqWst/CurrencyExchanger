package org.example.dto;

import org.example.handler.BadRequestException;

import java.util.regex.Pattern;

public class CurrencyDto {
    private static final Pattern CODE_PATTERN = Pattern.compile("[A-Z]{3}");
    private static final Pattern NAME_PATTERN = Pattern.compile("^(?!\\s*$)[a-zA-Z ]{1,46}$");
    private static final Pattern SIGN_PATTERN = Pattern.compile("\\S{1,5}");

    private String code;
    private String name;
    private String sign;

    public CurrencyDto() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if (!CODE_PATTERN.matcher(code).matches()){
            throw new BadRequestException("Key 'code' contains only latina UPPER CASE, size is 3");
        }
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(!NAME_PATTERN.matcher(name).matches()){
            throw new BadRequestException("Key 'name' size is 1 - 46 chars, latin only");
        }
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        if(!SIGN_PATTERN.matcher(sign).matches()){
            throw new BadRequestException("Key 'sign' size is 1 - 5 chars, space not applicable");
        }
        this.sign = sign;
    }
}
