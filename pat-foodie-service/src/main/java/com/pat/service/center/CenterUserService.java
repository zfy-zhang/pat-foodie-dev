package com.pat.service.center;

import com.pat.pojo.Users;
import com.pat.pojo.bo.UserBO;
import com.pat.pojo.bo.center.CenterUserBO;

/**
 * @Description：
 * @Author 不才人
 * @Create Date 2020/5/9 9:09 上午
 * @Modify
 */
public interface CenterUserService {

    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    Users queryUserInfo(String userId);

    /**
     * 修改用户信息
     * @param userId
     * @param centerUserBO
     */
    Users updateUserInfo(String userId, CenterUserBO centerUserBO);
}
