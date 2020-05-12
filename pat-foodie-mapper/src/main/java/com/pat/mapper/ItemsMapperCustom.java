package com.pat.mapper;

import com.pat.my.mapper.MyMapper;
import com.pat.pojo.Items;
import com.pat.pojo.vo.ItemCommentVO;
import com.pat.pojo.vo.SearchItemVO;
import com.pat.pojo.vo.ShopcartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom extends MyMapper<Items> {

    public List<ItemCommentVO> queryItemComments(@Param("paramsMap") Map<String, Object> map);

    public List<SearchItemVO> searchItems(@Param("paramsMap") Map<String, Object> map);

    public List<SearchItemVO> searchItemsByThirdCat(@Param("paramsMap") Map<String, Object> map);

    public List<ShopcartVO> queryItemsBySpecIds(@Param("paramsList")List specIdsList);
}