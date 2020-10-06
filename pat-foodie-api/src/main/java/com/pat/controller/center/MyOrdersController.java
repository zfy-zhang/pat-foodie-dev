package com.pat.controller.center;

import com.pat.controller.BaseController;
import com.pat.pojo.Orders;
import com.pat.pojo.vo.OrderStatusCountsVO;
import com.pat.service.center.MyOrdersService;
import com.pat.utils.PagedGridResult;
import com.pat.utils.ResJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/28
 * @Modify
 * @since
 */
@Api(value = "用户中心我的订单", tags = {"用户中心我的订单相关接口"})
@RestController
@RequestMapping("myorders")
public class MyOrdersController extends BaseController {

    @Autowired
    private MyOrdersService myOrdersService;

    @ApiOperation(value = "查询订单列表", notes = "查询订单列表", httpMethod = "POST")
    @PostMapping("/query")
    public ResJSONResult search(@ApiParam(name = "userId", value = "用户id", required = true)
                                @RequestParam String userId,
                                @ApiParam(name = "orderStatus", value = "订单状态", required = false)
                                @RequestParam Integer orderStatus,
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


        PagedGridResult grid = myOrdersService.queryMyOrders(userId, orderStatus, page, pageSize);

        return ResJSONResult.ok(grid);
    }

    @ApiOperation(value="商家发货", notes = "商家发货", httpMethod = "GET")
    @GetMapping("/deliver")
    public ResJSONResult deliver(
            @ApiParam(name = "orderId", value = "orderId", required = true)
            @RequestParam String orderId) {

        if (StringUtils.isBlank(orderId)) {
            return ResJSONResult.errorMsg("订单id不能为空");
        }

        myOrdersService.updateDeliverOrderStatus(orderId);
        return ResJSONResult.ok();
    }

    @ApiOperation(value="用户确认收货", notes = "用户确认收货", httpMethod = "POST")
    @PostMapping("/confirmReceive")
    public ResJSONResult confirmReceive(
            @ApiParam(name = "orderId", value = "orderId", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "用户Id", required = true)
            @RequestParam String userId) {

        ResJSONResult checkJSONResult = checkUserOrder(userId, orderId);

        if (checkJSONResult.getStatus() != HttpStatus.OK.value()) {
            return checkJSONResult;
        }

        boolean result = myOrdersService.updateReceiveOrderStatus(orderId);
        if (!result) {
            ResJSONResult.errorMsg("订单确认收货失败！");
        }

        return ResJSONResult.ok();
    }

    @ApiOperation(value="删除订单", notes = "删除订单", httpMethod = "POST")
    @PostMapping("/delete")
    public ResJSONResult delete(
            @ApiParam(name = "orderId", value = "orderId", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "用户Id", required = true)
            @RequestParam String userId) {

        ResJSONResult checkJSONResult = checkUserOrder(userId, orderId);

        if (checkJSONResult.getStatus() != HttpStatus.OK.value()) {
            return checkJSONResult;
        }

        boolean result = myOrdersService.deleteOrder(userId, orderId);
        if (!result) {
            ResJSONResult.errorMsg("删除订单失败！");
        }

        return ResJSONResult.ok();
    }

    @ApiOperation(value = "获得订单状态数概况", notes = "获得订单状态数概况", httpMethod = "POST")
    @PostMapping("/statusCounts")
    public ResJSONResult statusCounts(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId) {

        if (StringUtils.isBlank(userId)) {
            return ResJSONResult.errorMsg(null);
        }

        OrderStatusCountsVO result = myOrdersService.getOrderStatusCounts(userId);

        return ResJSONResult.ok(result);
    }

    @ApiOperation(value = "查询订单动向", notes = "查询订单动向", httpMethod = "POST")
    @PostMapping("/trend")
    public ResJSONResult trend(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(userId)) {
            return ResJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult grid = myOrdersService.getOrdersTrend(userId,
                page,
                pageSize);

        return ResJSONResult.ok(grid);
    }

    /**
     * 用于验证用户和订单之间是否有关联，避免非法用户调用
     * @param orderId
     * @param userId
     * @return
     */
//    private ResJSONResult checkUserOrder(String userId, String orderId) {
//        Orders orders = myOrdersService.queryMyOrder(userId, orderId);
//        if (orders == null) {
//            return ResJSONResult.errorMsg("订单不存在");
//        }
//        return ResJSONResult.ok();
//    }
}
