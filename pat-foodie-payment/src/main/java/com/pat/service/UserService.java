package com.pat.service;

import com.pat.pojo.Orders;
import com.pat.pojo.Users;
import com.pat.pojo.bo.MerchantOrdersBO;

public interface UserService {

	/**
	 * @Description: 查询用户信息
	 */
	public Users queryUserInfo(String userId, String pwd);

}

