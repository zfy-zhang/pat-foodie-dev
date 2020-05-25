package com.pat.config;

import com.pat.service.OrderService;
import com.pat.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author 不才人
 * @Create Date 2020/5/25 4:43 下午
 * @Modify
 */
@Component
public class OrderJob {

    @Autowired
    private OrderService orderService;

//    @Scheduled(cron = "0/3 * * * * ?")
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void autoCloseOrder() {
        orderService.closeOrder();
        System.out.println("执行定时任务当前时间为：" + DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
    }
}
