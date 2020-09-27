package com.pat.controller;

import com.pat.enums.OrderStatusEnum;
import com.pat.enums.PayMethod;
import com.pat.pojo.OrderStatus;
import com.pat.pojo.bo.ShopcartBO;
import com.pat.pojo.bo.SubmitOrderBO;
import com.pat.pojo.vo.MerchantOrdersVO;
import com.pat.pojo.vo.OrderVO;
import com.pat.service.OrderService;
import com.pat.utils.CookieUtils;
import com.pat.utils.JsonUtils;
import com.pat.utils.RedisOperator;
import com.pat.utils.ResJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public ResJSONResult create (@RequestBody SubmitOrderBO submitOrderBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {


        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type) {
            return ResJSONResult.errorMsg("支付方式不支持");
        }
        System.out.println(submitOrderBO);

        String shopcartJson = redisOperator.get(FOODIE_SHOPCART + ":" + submitOrderBO.getUserId());
        if (StringUtils.isBlank(shopcartJson)) {
            return ResJSONResult.errorMsg("购物车数据不正确");
        }

        List<ShopcartBO> shopcartList = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);

        // 1. 创建订单
        OrderVO orderVO = orderService.createOrder(shopcartList, submitOrderBO);
        String orderId = orderVO.getOrderId();


        // 2. 创建订单以后，移除购物车中已结算（已提交）的商品
        // 清理覆盖现有的redis汇总的购物车数据
        shopcartList.removeAll(orderVO.getToBeRemovedShopCatList());
        redisOperator.set(FOODIE_SHOPCART + ":" + submitOrderBO.getUserId(), JsonUtils.objectToJson(shopcartList));
        // 整合 Redis 之后，完善购物车中的已结算商品清除，并同步到前端的cookie
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopcartList) , true);

        // 3. 向支付中心发送当前订单，用于保存支付中心的订单数据
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(payReturnUrl);

        merchantOrdersVO.setAmount(1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId", "imooc");
        headers.add("password", "imooc");

        HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(merchantOrdersVO, headers);
        ResponseEntity<ResJSONResult> responseEntity = restTemplate.postForEntity(paymentUrl, entity, ResJSONResult.class);
        ResJSONResult paymentResult = responseEntity.getBody();
        if (paymentResult.getStatus() != 200) {
            return  ResJSONResult.errorMsg("支付中心创建订单失败，请联系管理员！");
        }
        return ResJSONResult.ok(orderId);
    }

    @PostMapping("/notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);

        return HttpStatus.OK.value();
    }

    @PostMapping("/getPaidOrderInfo")
    public ResJSONResult getPaidOrderInfo(String orderId) {
        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
        return ResJSONResult.ok(orderStatus);
    }
}
