package com.pat.controller;

import com.pat.enums.OrderStatusEnum;
import com.pat.enums.PayMethod;
import com.pat.pojo.bo.SubmitOrderBO;
import com.pat.service.OrderService;
import com.pat.utils.CookieUtils;
import com.pat.utils.ResJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @Author 不才人
 * @Create Date 2020/5/15 8:47 上午
 * @Modify
 */
@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RestController
@RequestMapping("orders")
public class OrdersController extends BaseController {

    @Autowired
    OrderService orderService;

    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public ResJSONResult create (@RequestBody SubmitOrderBO submitOrderBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {


        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type) {
            return ResJSONResult.errorMsg("支付方式不支持");
        }
        System.out.println(submitOrderBO);

        // 1. 创建订单
        String orderId = orderService.createOrder(submitOrderBO);
        // 2. 创建订单以后，移除购物车中已结算（已提交）的商品
        // TODO 整合 Redis 之后，完善购物车中的已结算商品清除，并同步到前端的cookie
//        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, "", true);
        // 3. 向支付中心发送当前订单，用于保存支付中心的订单数据
        return ResJSONResult.ok(orderId);
    }

    @PostMapping("/notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);

        return HttpStatus.OK.value();
    }
}
