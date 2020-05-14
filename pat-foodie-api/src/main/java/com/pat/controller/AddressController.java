package com.pat.controller;

import com.pat.pojo.UserAddress;
import com.pat.pojo.bo.AddressBO;
import com.pat.service.AddressService;
import com.pat.utils.MobileEmailUtils;
import com.pat.utils.ResJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description
 * @Author 不才人
 * @Create Date 2020/5/13 9:03 上午
 * @Modify
 */
@Api(value = "地址相关", tags = {"地址相关的api接口"})
@RequestMapping("/address")
@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     * 用户在确认订单页面，可以针对收货地址做如下操作
     * 1. 查询用户的所有收货列表
     * 2. 新增收货地址
     * 3. 删除收货地址
     * 4. 修改收货地址
     * 5. 设置默认地址
     */

    @ApiOperation(value = "根据用户id查询收获地址列表", notes = "根据用户id查询收获地址列表", httpMethod = "POST")
    @RequestMapping("/list")
    public ResJSONResult list(@RequestParam String userId) {
        if (StringUtils.isBlank(userId)) {
            return ResJSONResult.errorMsg("");
        }
        List<UserAddress> list = addressService.queryAll(userId);
        return ResJSONResult.ok(list);
    }

    @ApiOperation(value = "用户新增地址", notes = "用户新增地址", httpMethod = "POST")
    @RequestMapping("/add")
    public ResJSONResult add(@RequestBody AddressBO addressBO) {
        ResJSONResult resJSONResult = checkAddress(addressBO);
        if (resJSONResult.getStatus() != 200) {
            return resJSONResult;
        }
        addressService.addNewUserAddress(addressBO);
        return ResJSONResult.ok();
    }

    private ResJSONResult checkAddress(AddressBO addressBO) {
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return ResJSONResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return ResJSONResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return ResJSONResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return ResJSONResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return ResJSONResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return ResJSONResult.errorMsg("收货地址信息不能为空");
        }

        return ResJSONResult.ok();
    }

    @ApiOperation(value = "用户修改地址", notes = "用户修改地址", httpMethod = "POST")
    @RequestMapping("/update")
    public ResJSONResult update(@RequestBody AddressBO addressBO) {
        if (StringUtils.isBlank(addressBO.getAddressId())) {
            return ResJSONResult.errorMsg("修改地址错误：addressId不能为空");
        }
        ResJSONResult resJSONResult = checkAddress(addressBO);
        if (resJSONResult.getStatus() != 200) {
            return resJSONResult;
        }
        addressService.updateUserAddress(addressBO);
        return ResJSONResult.ok();
    }

    @ApiOperation(value = "用户删除地址", notes = "用户删除地址", httpMethod = "POST")
    @RequestMapping("/delete")
    public ResJSONResult delete(@RequestParam String userId,
                                @RequestParam String addressId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return ResJSONResult.errorMsg("");
        }

        addressService.deleteUserAddress(userId, addressId);
        return ResJSONResult.ok();
    }

    @ApiOperation(value = "用户设置默认地址", notes = "用户设置默认地址", httpMethod = "POST")
    @RequestMapping("/setDefault")
    public ResJSONResult setDefault(@RequestParam String userId,
                                @RequestParam String addressId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return ResJSONResult.errorMsg("");
        }

        addressService.updateUserAddressToBeDefault(userId, addressId);
        return ResJSONResult.ok();
    }
}
