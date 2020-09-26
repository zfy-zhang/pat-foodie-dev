package com.pat.service;

import com.pat.pojo.UserAddress;
import com.pat.pojo.bo.AddressBO;

import java.util.List;

/**
 * @Description：
 * @Author 不才人
 * @Create Date 2020/5/13 9:23 上午
 * @Modify
 */
public interface AddressService {


    /**
     * 根据用户id查询用户的收货地址类标
     * @param userId
     * @return
     */
    public List<UserAddress> queryAll(String userId);

    /**
     * 用户新增地址
     * @param addressBO
     */
    public void addNewUserAddress(AddressBO addressBO);

    /**
     * 用户修改地址
     * @param addressBO
     */
    public void updateUserAddress(AddressBO addressBO);

    /**
     * 根据用户id和地址id删除对应的用户地址信息
     * @param userId
     * @param addressId
     */
    public void deleteUserAddress(String userId, String addressId);

    /**
     * 修改默认地址
     * @param userId
     * @param addressId
     */
    public void updateUserAddressToBeDefault(String userId, String addressId);

    /**
     * 根据用户id和地址id查询用户地址具体对象信息
     * @param userId
     * @param addressId
     * @return
     */
    public UserAddress queryUserAddress(String userId, String addressId);
}
