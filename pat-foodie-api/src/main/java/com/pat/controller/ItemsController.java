package com.pat.controller;

import com.pat.pojo.Items;
import com.pat.pojo.ItemsImg;
import com.pat.pojo.ItemsParam;
import com.pat.pojo.ItemsSpec;
import com.pat.pojo.vo.CommentLevelCountsVO;
import com.pat.pojo.vo.ItemInfoVO;
import com.pat.pojo.vo.ShopcartVO;
import com.pat.service.ItemService;
import com.pat.utils.PagedGridResult;
import com.pat.utils.ResJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description
 * @Author 不才人
 * @Create Date 2020/5/11 9:18 上午
 * @Modify
 */
@Api(value = "商品接口", tags = {"商品信息展示的相关接口"})
@RestController
@RequestMapping("items")
public class ItemsController extends BaseController {

    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public ResJSONResult info(@ApiParam(name = "itemId", value = "商品id", required = true)
                                @PathVariable String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return ResJSONResult.errorMsg(null);
        }
        Items items = itemService.queryItemById(itemId);
        List<ItemsImg> itemImgList = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemSpecList = itemService.queryItemSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemsParam(itemId);

        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(items);
        itemInfoVO.setItemImgList(itemImgList);
        itemInfoVO.setItemSpecList(itemSpecList);
        itemInfoVO.setItemParams(itemsParam);
        return ResJSONResult.ok(itemInfoVO);
    }


    @ApiOperation(value = "查询商品评价等级", notes = "查询商品评价等级", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public ResJSONResult commentLevel(@ApiParam(name = "itemId", value = "商品id", required = true)
                              @RequestParam String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return ResJSONResult.errorMsg(null);
        }
        CommentLevelCountsVO countsVO = itemService.queryCommentCounts(itemId);

        return ResJSONResult.ok(countsVO);
    }

    @ApiOperation(value = "查询商品评论", notes = "查询商品评论", httpMethod = "GET")
    @GetMapping("/comments")
    public ResJSONResult comments(@ApiParam(name = "itemId", value = "商品id", required = true)
                                  @RequestParam String itemId,
                                  @ApiParam(name = "level", value = "评价等级", required = false)
                                  @RequestParam Integer level,
                                  @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
                                  @RequestParam Integer page,
                                  @ApiParam(name = "pageSize", value = "分页每一页显示的记录数", required = false)
                                      @RequestParam Integer pageSize
                                ) {
        if (StringUtils.isBlank(itemId)) {
            return ResJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult grid = itemService.queryPagedComments(itemId, level, page, pageSize);

        return ResJSONResult.ok(grid);
    }

    @ApiOperation(value = "搜索商品列表", notes = "搜索商品列表", httpMethod = "GET")
    @GetMapping("/search")
    public ResJSONResult search(@ApiParam(name = "keywords", value = "关键字", required = true)
                                  @RequestParam String keywords,
                                  @ApiParam(name = "sort", value = "排序", required = false)
                                  @RequestParam String sort,
                                  @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
                                  @RequestParam Integer page,
                                  @ApiParam(name = "pageSize", value = "分页每一页显示的记录数", required = false)
                                  @RequestParam Integer pageSize
    ) {
        if (StringUtils.isBlank(keywords)) {
            return ResJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        PagedGridResult grid = itemService.searchItems(keywords, sort, page, pageSize);

        return ResJSONResult.ok(grid);
    }

    @ApiOperation(value = "通过分类id搜索商品列表", notes = "通过分类id搜索商品列表", httpMethod = "GET")
    @GetMapping("/catItems")
    public ResJSONResult catItems(@ApiParam(name = "catId", value = "三级分类id", required = true)
                                  @RequestParam Integer catId,
                                  @ApiParam(name = "sort", value = "排序", required = false)
                                  @RequestParam String sort,
                                  @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
                                  @RequestParam Integer page,
                                  @ApiParam(name = "pageSize", value = "分页每一页显示的记录数", required = false)
                                  @RequestParam Integer pageSize
    ) {
        if (catId == null) {
            return ResJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        PagedGridResult grid = itemService.searchItems(catId, sort, page, pageSize);

        return ResJSONResult.ok(grid);
    }


    // 用于长时间未登录网站，刷新购物车中的数据（主要是商品价格），类似淘宝京东
    @ApiOperation(value = "根据商品规格id查询最新商品数据", notes = "根据商品规格id查询最新商品数据", httpMethod = "GET")
    @GetMapping("/refresh")
    public ResJSONResult refresh(@ApiParam(name = "itemSpecIds", value = "拼接的规格ids", required = true, example = "1001,1002,1003")
                                  @RequestParam String itemSpecIds
    ) {
        if (StringUtils.isBlank(itemSpecIds)) {
            return ResJSONResult.ok();
        }

        List<ShopcartVO> list = itemService.queryItemsBySpecIds(itemSpecIds);
        return ResJSONResult.ok(list);
    }
}
