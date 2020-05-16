package com.pat.service;

import com.pat.pojo.bo.SubmitOrderBO;

import java.util.List;

/**
 * @Description
 * @Author 不才人
 * @Create Date 2020/5/15 1:40 下午
 * @Modify
 */
public interface OrderService {

    /**
     * 用于创建订单相关信息
     * @param submitOrderBO
     */
    public String createOrder(SubmitOrderBO submitOrderBO);

    /**
     * 修改订单状态
     * @param orderId
     * @param orderStatus
     */
    public void updateOrderStatus(String orderId, Integer orderStatus);
}
