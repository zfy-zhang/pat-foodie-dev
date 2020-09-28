package com.pat.enums;

/**
 * @Description：性别枚举类
 * @Author 不才人
 * @Create Date 2020/5/9 11:00 上午
 * @Modify
 */
public enum Sex {
    woman(0, "女"),
    man(1, "男"),
    secret(2, "保密");

    public final Integer type;
    public final String value;

    Sex(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
