package com.pat.config;

import com.pat.service.OrderService;
import com.pat.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author 不才人
 * @Create Date 2020/5/25 4:43 下午
 * @Modify
 */
@Slf4j
@Component
public class OrderJob {

    @Autowired
    private OrderService orderService;

    /**
     * 使用定时任务关闭超期未支付订单，会存在的弊端：
     * 1、会有时间差，程序便会不严谨
     *      10：39下单，11：00检查不足一小时，12：00检查，超过一小时单多余39分钟（39分钟就是时间差）
     * 2、不支持集群
     *      单机没问题，使用集群就会存在多个定时任务，
     *      解决方案：只是用一台计算机节点，单独运行所有的定时任务
     * 3、会对数据库进行全表搜索，及其影响数据库性能：select * from order where orderStatus = 10;（真实环境并不会这么做）
     * 定时任务仅仅适用于小型轻量级项目（传统项目），电商项目不适用。
     *
     * 电商互联网项目可用消息队列去解决
     *          -> 延时任务（队列）。
     *          10：12 下单的，未付款（10）状态，适用延时任务就会在 11：12 去检查，如果当前状态还是10，则会直接关闭。
     *
     */

//    @Scheduled(cron = "0/3 * * * * ?")
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void autoCloseOrder() {
        orderService.closeOrder();
        log.info("执行定时任务当前时间为：{}",  DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
    }
}
