package com.pat.service;

import com.pat.pojo.Users;
import com.pat.pojo.bo.UserBO;

/**
 * @Description：
 * @Author 不才人
 * @Create Date 2020/5/9 9:09 上午
 * @Modify
 */
public interface UserService {

    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    public boolean queryUsernameIsExist(String username);

    /**
     * 创建用户
     * @param userBO
     * @return
     */
    public Users createUser(UserBO userBO);
}
