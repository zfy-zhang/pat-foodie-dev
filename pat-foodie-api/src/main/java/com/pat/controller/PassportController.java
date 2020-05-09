package com.pat.controller;

import com.pat.pojo.bo.UserBO;
import com.pat.service.UserService;
import com.pat.utils.ResJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description
 * @Author 不才人
 * @Create Date 2020/5/9 9:20 上午
 * @Modify
 */
@Api(value = "注册登录", tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("passport")
public class PassportController {

    @Autowired
    private UserService userService;

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
    private ResJSONResult regist(@RequestBody UserBO userBO) {

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
        userService.createUser(userBO);

        return ResJSONResult.ok();
    }
}
