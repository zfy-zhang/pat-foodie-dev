package com.pat.service.center;

import com.pat.pojo.OrderItems;
import com.pat.pojo.Orders;
import com.pat.pojo.bo.center.OrderItemsCommentBO;
import com.pat.utils.PagedGridResult;

import java.util.List;

/**
 * @Description:
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Create 2020/9/30
 * @Modify
 * @since
 */
public interface MyCommentsService {

    /**
     * 根据订单查询相关的商品
     * @param orderId
     * @return
     */
    List<OrderItems> queryPendingComment(String orderId);

    /**
     * 保存用户评论
     * @param orderId
     * @param userId
     * @param commentList
     */
    void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList);

    /**
     * 查询我的评价
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize);
}
