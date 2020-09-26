package com.pat.pojo.vo;

/**
 * @Description
 * @Author 不才人
 * @Create Date 2020/5/16 4:56 下午
 * @Modify
 */
public class MerchantOrdersVO {
    private String merchantOrderId;     // 商户订单号
    private String merchantUserId;      // 商户方发起用户的用户id
    private Integer amount;             // 实际支付总金额
    private Integer payMethod;          // 支付方式
    private String returnUrl;           // 支付成功后回调地址

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public String getMerchantUserId() {
        return merchantUserId;
    }

    public void setMerchantUserId(String merchantUserId) {
        this.merchantUserId = merchantUserId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }
}
