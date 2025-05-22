package dev.lqwd.dto;

import dev.lqwd.exceptions.custom_exceptions.DataBaseException;

public class CurrencyDto extends Dto {
    private int id;
    private String name;
    private String code;
    private String sign;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return requireNonNull(code);
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return requireNonNull(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return requireNonNull(sign);
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
