package com.pat.pojo.vo;

import com.pat.pojo.bo.ShopcartBO;

import java.util.List;

/**
 * @Description
 * @Author 不才人
 * @Create Date 2020/5/16 8:45 下午
 * @Modify
 */
public class OrderVO {
    private String orderId;
    private MerchantOrdersVO merchantOrdersVO;
    private List<ShopcartBO> toBeRemovedShopCatList;

    public List<ShopcartBO> getToBeRemovedShopCatList() {
        return toBeRemovedShopCatList;
    }

    public void setToBeRemovedShopCatList(List<ShopcartBO> toBeRemovedShopCatList) {
        this.toBeRemovedShopCatList = toBeRemovedShopCatList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public MerchantOrdersVO getMerchantOrdersVO() {
        return merchantOrdersVO;
    }

    public void setMerchantOrdersVO(MerchantOrdersVO merchantOrdersVO) {
        this.merchantOrdersVO = merchantOrdersVO;
    }
}
