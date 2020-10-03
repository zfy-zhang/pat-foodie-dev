package com.pat.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.pat.enums.YesOrNo;
import com.pat.mapper.ItemsCommentsMapperCustom;
import com.pat.mapper.OrderItemsMapper;
import com.pat.mapper.OrderStatusMapper;
import com.pat.mapper.OrdersMapper;
import com.pat.pojo.OrderItems;
import com.pat.pojo.OrderStatus;
import com.pat.pojo.Orders;
import com.pat.pojo.bo.center.OrderItemsCommentBO;
import com.pat.pojo.vo.MyCommentVO;
import com.pat.service.center.MyCommentsService;
import com.pat.utils.PagedGridResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/30
 * @Modify
 * @since
 */
@Service
public class MyCommentsServiceImpl extends BaseService implements MyCommentsService {

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private ItemsCommentsMapperCustom itemsCommentsMapperCustom;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<OrderItems> queryPendingComment(String orderId) {

        OrderItems orderItems = new OrderItems();
        orderItems.setOrderId(orderId);
        return orderItemsMapper.select(orderItems);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList) {

        // 1、保存评价 item_comments
        for (OrderItemsCommentBO orderItemsCommentBO : commentList) {
            orderItemsCommentBO.setCommentId(sid.nextShort());
        }

        Map<String, Object> map = new HashMap<>();

        map.put("userId", userId);
        map.put("commentList", commentList);
        itemsCommentsMapperCustom.saveComments(map);

        // 2、修改订单表，改为已评价
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setIsComment(YesOrNo.YES.type);
        ordersMapper.updateByPrimaryKeySelective(orders);

        // 3、修改订单状态表的留言时间 order_status
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("page", page);
        map.put("pageSize", pageSize);

        PageHelper.startPage(page, pageSize);
        List<MyCommentVO> myCommentVOS = itemsCommentsMapperCustom.queryMyComments(map);
        return setterPagedGrid(myCommentVOS, page);
    }
}
