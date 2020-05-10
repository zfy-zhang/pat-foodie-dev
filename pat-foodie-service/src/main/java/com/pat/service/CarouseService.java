package com.pat.service;

import com.pat.pojo.Carousel;

import java.util.List;

/**
 * @Description
 * @Author 不才人
 * @Create Date 2020/5/10 1:01 下午
 * @Modify
 */
public interface CarouseService {
    /**
     * 查询所有的轮播图列表
     * @param isShow
     * @return
     */
    public List<Carousel> queryAll(Integer isShow);
}
