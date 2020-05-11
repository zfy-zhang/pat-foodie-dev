package com.pat.enums;

/**
 * @Description: 商品评价等级 枚举
 * @Author 不才人
 * @Create Date 2020/5/11 2:02 下午
 * @Modify
 */
public enum CommentLevel {
    GOOD(1, "好评"),
    NORMAL(2, "中评"),
    BAD(3, "差评");

    public final Integer type;
    public final String value;

    CommentLevel(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
