package com.pat.controller;

import com.pat.pojo.Users;
import com.pat.pojo.bo.ShopcartBO;
import com.pat.pojo.bo.UserBO;
import com.pat.pojo.vo.UsersVO;
import com.pat.service.UserService;
import com.pat.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Description: 注册登录
 * @Author 不才人
 * @Create Date 2020/5/9 9:20 上午
 * @Modify
 */
@Api(value = "注册登录", tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("passport")
public class PassportController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    private ResJSONResult usernameIsExist(@RequestParam String username) {
        // 1. 判段用户名不能为空
        if (StringUtils.isBlank(username)) {
            return ResJSONResult.errorMsg("用户名不能为空");
        }

        // 2. 查找注册的用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);

        if (isExist) {
            return ResJSONResult.errorMsg("用户名已存在");
        }

        // 3. 请求成功，用户名没有重复
        return ResJSONResult.ok();
    }

    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/regist")
    private ResJSONResult regist(@RequestBody UserBO userBO,
                                 HttpServletRequest request, HttpServletResponse response) {

        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPwd = userBO.getConfirmPassword();

        // 1. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return ResJSONResult.errorMsg("用户名或密码不能为空");
        }

        // 2. 查询用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return ResJSONResult.errorMsg("用户名已存在");
        }

        // 3. 密码长度不小于6
        if (password.length() < 6) {
            return ResJSONResult.errorMsg("密码长度不能小于6");
        }
        // 4. 判断两次密码是否一致
        if (!password.equals(confirmPwd)) {
            return ResJSONResult.errorMsg("两次密码输入不一致");
        }

        // 5. 实现注册
        Users userResult = userService.createUser(userBO);

        userResult = setNullProperty(userResult);

        // 实现用户的redis会话
        String uniqueToken = UUID.randomUUID().toString();
        redisOperator.set(REDIS_USER_TOKEN + ":" + userResult.getId(), uniqueToken);

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(userResult, usersVO);

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(usersVO), true);

        // TODO 生存用户token，存入redis会话
        // 同步购物车数据
        syncShopCartData(userResult.getId(), request, response);


        return ResJSONResult.ok();
    }


    /**
     * 注册登陆成功后，同步 cookie 和 redis 中的购物车数据
     * @param userId
     * @param request
     * @param response
     */
    private void syncShopCartData(String userId, HttpServletRequest request, HttpServletResponse response) {
        /**
         * 1、redis 中无数据，如果cookie中的购物车数据为空，那么此时不做任何处理
         *                  如果cookie中的购物车数据不为空，此时直接放入redis中
         * 2、redis 中有数据，如果cookie中的购物车数据为空，那么直接把redis中的数据覆盖至本地cookie
         *                  如果cookie中的购物车数据不为空，
         *                      如果cookie中的某个商品在redis中存在，
         *                      则以cookie为主，删除redis中的，
         *                      把cookie中的商品直接覆盖至redis中（仿京东）
         * 3、同步到redis中以后，覆盖本地cookie的购物车数据，保证本地购物车的数据是同步至最新的
         *
         */

        // 1、从 redis 中获取购物车
        String shopcartJsonRedis = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        // 2、从 cookie 中获取购物车
        String shopcartStrCookie = CookieUtils.getCookieValue(request, FOODIE_SHOPCART, true);

        if (StringUtils.isBlank(shopcartJsonRedis)) {
            // redis 为空，cookie不为空，直接把cookie数据放到redis中
            if (StringUtils.isNotBlank(shopcartStrCookie)) {
                redisOperator.set(FOODIE_SHOPCART + ":" + userId, shopcartStrCookie);
            }
        } else {
            // redis 不为空，cookie不为空， 合并cookie和redis中购物车的商品数据（统一商品则覆盖redis）
            if (StringUtils.isNotBlank(shopcartStrCookie)) {
                /**
                 *  1、已经存在的，把cookie中对应的数量，覆盖redis（参考京东）
                 *  2、该项商品标记为待删除，统一放入待删除的list中
                 *  3、从cookie中清理所有的待删除list
                 *  4、合并redis和cookie中的数据
                 *  5、更新到redis和cookie中
                 */
                List<ShopcartBO> shopCartListRedis = JsonUtils.jsonToList(shopcartJsonRedis, ShopcartBO.class);
                List<ShopcartBO> shopCartListCookie = JsonUtils.jsonToList(shopcartStrCookie, ShopcartBO.class);

                List<ShopcartBO> pendingDeleteList = new ArrayList<>();
                // 定义待删除list
                for (ShopcartBO redisShopCart : shopCartListRedis) {
                    String redisSpecId = redisShopCart.getSpecId();
                    for (ShopcartBO cookieShopCart : shopCartListCookie) {
                        String cookieSpecId = cookieShopCart.getSpecId();
                        if (redisSpecId.equals(cookieSpecId)) {
                            // 覆盖购买数量，不累加，仿京东
                            redisShopCart.setBuyCounts(cookieShopCart.getBuyCounts());
                            // 把 cookieShopCart 放入待删除 list ，用于最后的删除合并
                            pendingDeleteList.add(cookieShopCart);
                        }
                    }
                }
                // 从现有的cookie中删除对应的覆盖过的商品
                shopCartListCookie.removeAll(pendingDeleteList);
                // 合并两个list
                shopCartListRedis.addAll(shopCartListCookie);

                //更新到 redis 和 cookie
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopCartListRedis), true);
                redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopCartListRedis));

            } else {
                // redis 不为空， cookie为空，直接把redis覆盖cookie
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, shopcartJsonRedis, true);
            }
        }


    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    private ResJSONResult login(@RequestBody UserBO userBO,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {

        String username = userBO.getUsername();
        String password = userBO.getPassword();

        // 1. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return ResJSONResult.errorMsg("用户名或密码不能为空");
        }

        // 2. 实现登陆
        Users userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));

        if (userResult == null) {
            return ResJSONResult.errorMsg("用户名或密码错误");
        }

        userResult = setNullProperty(userResult);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userResult), true);

        // TODO 生存用户token，存入redis会话
        // 同步购物车数据
        syncShopCartData(userResult.getId(), request, response);

        return ResJSONResult.ok(userResult);
    }

    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public ResJSONResult logout(@RequestParam String userId, HttpServletRequest request, HttpServletResponse response) {
        // 清除用户相关信息的 cookie
        CookieUtils.deleteCookie(request, response, "user");

        // 用户推出登录，需要清空购物车
        CookieUtils.deleteCookie(request, response, FOODIE_SHOPCART);
        // TODO 分布式会话中，需要清除用户数据

        return ResJSONResult.ok();
    }

}
