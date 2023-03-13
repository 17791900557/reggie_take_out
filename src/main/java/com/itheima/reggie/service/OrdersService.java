package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;

public interface OrdersService extends IService<Orders> {
    /**
     * 提交订单
     * @param orders
     */
    void submit(Orders orders);
}
