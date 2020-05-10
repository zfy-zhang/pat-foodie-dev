package com.pat.pojo.vo;

import java.util.List;

/**
 * @Description: 二级分类VO
 * @Author 不才人
 * @Create Date 2020/5/10 10:21 下午
 * @Modify
 */
public class CategoryVO {

    private Integer id;
    private String name;
    private String type;
    private Integer fatherId;
    // 三级分类
    private List<SubCategoryVO> subCatList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getFatherId() {
        return fatherId;
    }

    public void setFatherId(Integer fatherId) {
        this.fatherId = fatherId;
    }

    public List<SubCategoryVO> getSubCatList() {
        return subCatList;
    }

    public void setSubCatList(List<SubCategoryVO> subCatList) {
        this.subCatList = subCatList;
    }
}
