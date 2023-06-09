package com.itheima.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.*;
import com.itheima.reggie.mapper.OrdersMapper;
import com.itheima.reggie.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.sampled.FloatControl;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    private OrderDetailService detailServiceService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;

    /**
     * 提交订单
     * @param orders
     */

    @Override
    @Transactional
    public void submit(Orders orders) {
        //获取当前用户Id
        Long userId = BaseContext.getCurrentId();
        //查询购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);

        if (shoppingCartList == null || shoppingCartList.size() == 0){
            throw new CustomException("购物车为空，下单失败");
        }
        //查询用户数据和地址数据
        User user = userService.getById(userId);
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);

        if (addressBookId == null){
            throw new CustomException("地址为空，下单失败");
        }
        long orderId = IdWorker.getId();
        //向订单表插入一条数据
        AtomicInteger amount = new AtomicInteger(0);

        List<OrderDetail> orderDetails = shoppingCartList.stream().map((item)->{
              OrderDetail orderDetail = new OrderDetail();
              orderDetail.setAmount(item.getAmount());
              orderDetail.setOrderId(orderId);
              orderDetail.setNumber(item.getNumber());
              orderDetail.setDishFlavor(item.getDishFlavor());
              orderDetail.setName(item.getName());
              orderDetail.setDishId(item.getDishId());
              orderDetail.setSetmealId(item.getSetmealId());
              orderDetail.setImage(item.getImage());
              amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
              return orderDetail;
        }).collect(Collectors.toList());

        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orders.getId()));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        this.save(orders);

        //向订单明细表添加数据
        detailServiceService.saveBatch(orderDetails);
        //清空购物车
        shoppingCartService.remove(queryWrapper);

    }
}
