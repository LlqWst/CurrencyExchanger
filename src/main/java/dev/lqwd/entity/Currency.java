package dev.lqwd.entity;

public class Currency extends Entity {
    private int id;
    private String name;
    private String code;
    private String sign;

    public Currency(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return super.requireNonNull(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return super.requireNonNull(code);
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getSign() {
        return super.requireNonNull(sign);
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
