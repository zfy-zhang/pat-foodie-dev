package com.pat.controller;

import com.pat.pojo.Orders;
import com.pat.pojo.Users;
import com.pat.pojo.vo.UsersVO;
import com.pat.service.center.MyOrdersService;
import com.pat.utils.RedisOperator;
import com.pat.utils.ResJSONResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.UUID;

/**
 * @Description
 * @Author 不才人
 * @Create Date 2020/5/11 3:31 下午
 * @Modify
 */
@Controller
public class BaseController {

    @Autowired
    private RedisOperator redisOperator;

    public static final String FOODIE_SHOPCART = "shopcart";

    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;

    public static final String REDIS_USER_TOKEN = "REDIS_USER_TOKEN";

    // 支付中心的调用地址
//    String paymentUrl = "http://payment.t.mukewang.com/pat-foodie-payment/payment/createMerchantOrder";		// produce
    String paymentUrl = "http://localhost:8089/payment/createMerchantOrder";		// produce

    // 微信支付成功 -> 支付中心 -> 天天吃货平台
    //                       |-> 回调通知的url
    String payReturnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaid";
//    String payReturnUrl = "http://api.z.mukewang.com/foodie-dev-api/orders/notifyMerchantOrderPaid";

    // 用户上传头像的位置
    public static final String IMAGE_USER_FACE_LOCATION = File.separator + "Users" +
            File.separator + "alisha" +
            File.separator + "IdeaProjects" +
            File.separator + "pat-foodie-dev" +
            File.separator + "workspaces" +
            File.separator + "images" +
            File.separator + "foodie" +
            File.separator + "faces";


    @Autowired
    private MyOrdersService myOrdersService;

    /**
     * 用于验证用户和订单之间是否有关联，避免非法用户调用
     * @param orderId
     * @param userId
     * @return
     */
    public ResJSONResult checkUserOrder(String userId, String orderId) {
        Orders orders = myOrdersService.queryMyOrder(userId, orderId);
        if (orders == null) {
            return ResJSONResult.errorMsg("订单不存在");
        }
        return ResJSONResult.ok(orders);
    }

    public UsersVO convertUserVO(Users userResult) {
        // 实现用户的redis会话
        String uniqueToken = UUID.randomUUID().toString();
        redisOperator.set(REDIS_USER_TOKEN + ":" + userResult.getId(), uniqueToken);

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(userResult, usersVO);
        usersVO.setUserUniqueToken(uniqueToken);
        return usersVO;
    }

}
