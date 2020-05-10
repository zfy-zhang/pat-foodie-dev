package com.pat.mapper;

import com.pat.my.mapper.MyMapper;
import com.pat.pojo.Category;
import com.pat.pojo.vo.CategoryVO;
import com.pat.pojo.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryMapperCustom {

    public List<CategoryVO> getSubCatList(Integer rootCatId);

    public List<NewItemsVO> getSixNewItemsLazy(@Param("paramsMap") Map<String, Object> map);
}