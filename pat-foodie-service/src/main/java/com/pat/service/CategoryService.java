package com.pat.service;

import com.pat.pojo.Category;
import com.pat.pojo.vo.CategoryVO;
import com.pat.pojo.vo.NewItemsVO;

import java.util.List;

/**
 * @Description
 * @Author 不才人
 * @Create Date 2020/5/10 7:52 下午
 * @Modify
 */
public interface CategoryService {

    /**
     * 查询所有一级分类
     * @return
     */
    public List<Category> queryAllRootLevelCat();

    /**
     * 根据一级分类id查询子分类信息
     * @param rootCatId
     * @return
     */
    public List<CategoryVO> getSubCatList(Integer rootCatId);

    /**
     * 查询首页每一个分类下最新商品数据
     * @param rootCatId
     * @return
     */
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId);

}
