package com.pat.controller;

import com.pat.enums.YesOrNo;
import com.pat.pojo.Carousel;
import com.pat.pojo.Category;
import com.pat.pojo.vo.CategoryVO;
import com.pat.pojo.vo.NewItemsVO;
import com.pat.service.CarouseService;
import com.pat.service.CategoryService;
import com.pat.utils.JsonUtils;
import com.pat.utils.RedisOperator;
import com.pat.utils.ResJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author 不才人
 * @Create Date 2020/5/10 1:07 下午
 * @Modify
 */
@Api(value = "首页", tags = {"首页展示的相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    CarouseService carouseService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public ResJSONResult carousel() {


        List<Carousel> list = new ArrayList<>();
        String carouselStr = redisOperator.get("carousel");
        if (StringUtils.isBlank(carouselStr)) {
            list = carouseService.queryAll(YesOrNo.YES.type);
            redisOperator.set("carousel", JsonUtils.objectToJson(list));
        } else {
            list = JsonUtils.jsonToList(carouselStr, Carousel.class);
        }

        return ResJSONResult.ok(list);
    }

    /**
     * 1. 后台运营系统，一旦广告（轮播图）发生更改， 就可以删除缓存，然后重置
     * 2. 定时重置，比如凌晨两点重置
     * 3. 每个轮播图都有可能是一个广告，每个广告都会有一个过期时间，过期了就重置
     */

    /**
     * 首页分类展示需求：
     * 1. 第一次刷新主页查询大分类，渲染展示到主页
     * 2. 如果鼠标移动到大分类，则加载其子分类到内容，如果存在子分类，则不需要加载（懒加载）
     */
    @ApiOperation(value = "获取商品分类（一级分类）", notes = "获取商品分类（一级分类）", httpMethod = "GET")
    @GetMapping("/cats")
    public ResJSONResult cats() {
        List<Category> list = categoryService.queryAllRootLevelCat();
        return ResJSONResult.ok(list);
    }

    @ApiOperation(value = "获取商品子分类", notes = "获取商品子分类", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public ResJSONResult subCat(@ApiParam(name = "rootCatId", value = "一级分类id", required = true)
                                @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            return ResJSONResult.errorMsg("分类不存在");
        }
        List<CategoryVO> list = categoryService.getSubCatList(rootCatId);
        return ResJSONResult.ok(list);
    }

    @ApiOperation(value = "查询每个一级分类下的最新6条商品数据", notes = "查询每个一级分类下的最新6条商品数据", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public ResJSONResult sixNewItems(@ApiParam(name = "rootCatId", value = "一级分类id", required = true)
                                @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            return ResJSONResult.errorMsg("分类不存在");
        }
        List<NewItemsVO> list = categoryService.getSixNewItemsLazy(rootCatId);
        return ResJSONResult.ok(list);
    }
}
