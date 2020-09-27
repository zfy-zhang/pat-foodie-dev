package com.pat.controller.center;

import com.pat.pojo.Users;
import com.pat.service.center.CenterUserService;
import com.pat.utils.ResJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/27
 * @Modify
 * @since
 */
@RestController
@RequestMapping("center")
@Api(value = "center - 用户中心", tags = {"用户中心展示的相关接口"})
public class CenterController {

    @Autowired
    private CenterUserService centerUserService;

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", httpMethod = "GET")
    @GetMapping("/userInfo")
    public ResJSONResult userInfo(
                @ApiParam(name = "userId", value = "用户id", required = true)
                @RequestParam String userId) {
        Users user = centerUserService.queryUserInfo(userId);
        return ResJSONResult.ok(user);
    }

}
