package dev.lqwd.dto;

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
        return super.requireNonNull(code);
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return super.requireNonNull(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return super.requireNonNull(sign);
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
