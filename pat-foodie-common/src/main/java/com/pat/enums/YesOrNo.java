package com.pat.enums;

/**
 * @Description 是否 枚举
 * @Author 不才人
 * @Create Date 2020/5/10 1:11 下午
 * @Modify
 */
public enum YesOrNo {
    NO(0, "否"),
    YES(1, "是");

    public final Integer type;
    public final String value;

    YesOrNo(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}