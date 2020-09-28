package com.pat.controller.center;

import com.pat.controller.BaseController;
import com.pat.service.center.MyOrdersService;
import com.pat.utils.PagedGridResult;
import com.pat.utils.ResJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

}
