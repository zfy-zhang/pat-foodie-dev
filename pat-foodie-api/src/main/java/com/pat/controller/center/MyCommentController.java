package com.pat.controller.center;

import com.pat.controller.BaseController;
import com.pat.enums.YesOrNo;
import com.pat.pojo.OrderItems;
import com.pat.pojo.Orders;
import com.pat.pojo.bo.center.OrderItemsCommentBO;
import com.pat.service.center.MyCommentsService;
import com.pat.utils.PagedGridResult;
import com.pat.utils.ResJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/30
 * @Modify
 * @since
 */
@Api(value = "用户中心评价模块", tags = {"用户中心评价模块相关接口"})
@RestController
@RequestMapping("mycomments")
public class MyCommentController extends BaseController {

    @Autowired
    private MyCommentsService myCommentsService;

    @ApiOperation(value = "查询订单列表", notes = "查询订单列表", httpMethod = "POST")
    @PostMapping("/pending")
    public ResJSONResult pending(@ApiParam(name = "userId", value = "用户id", required = true)
                                @RequestParam String userId,
                                @ApiParam(name = "orderId", value = "订单id", required = true)
                                @RequestParam String orderId) {
        ResJSONResult checkJSONResult = checkUserOrder(userId, orderId);

        if (checkJSONResult.getStatus() != HttpStatus.OK.value()) {
            return checkJSONResult;
        }

        // 判断该笔评价是否已经评价过
        Orders data = (Orders) checkJSONResult.getData();
        if (data.getIsComment() == YesOrNo.YES.type) {
            return ResJSONResult.errorMsg("该订单已评价！");
        }

        List<OrderItems> orderItems = myCommentsService.queryPendingComment(orderId);

        return ResJSONResult.ok(orderItems);
    }

    @ApiOperation(value = "保存评论列表", notes = "保存评论列表", httpMethod = "POST")
    @PostMapping("/saveList")
    public ResJSONResult saveList(@ApiParam(name = "userId", value = "用户id", required = true)
                                  @RequestParam String userId,
                                  @ApiParam(name = "orderId", value = "订单id", required = true)
                                  @RequestParam String orderId,
                                  @ApiParam(name = "评论列表", value = "评论列表", required = true)
                                  @RequestBody List<OrderItemsCommentBO> commentList) {

        ResJSONResult checkJSONResult = checkUserOrder(userId, orderId);

        if (checkJSONResult.getStatus() != HttpStatus.OK.value()) {
            return checkJSONResult;
        }

        // 判断评价内容list不能为空
        if (commentList == null || commentList.isEmpty() || commentList.size() == 0) {
            return ResJSONResult.errorMsg("评论内容不能为空！");
        }
        myCommentsService.saveComments(orderId, userId, commentList);

        return ResJSONResult.ok();
    }

    @ApiOperation(value = "查询评论列表", notes = "查询评论列表", httpMethod = "POST")
    @PostMapping("/query")
    public ResJSONResult query(@ApiParam(name = "userId", value = "用户id", required = true)
                                @RequestParam String userId,
                                @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
                                @RequestParam Integer page,
                                @ApiParam(name = "pageSize", value = "分页每一页显示的记录数", required = false)
                                @RequestParam Integer pageSize
    ) {
        if (StringUtils.isBlank(userId)) {
            return ResJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }


        PagedGridResult grid = myCommentsService.queryMyComments(userId, page, pageSize);

        return ResJSONResult.ok(grid);
    }

}
