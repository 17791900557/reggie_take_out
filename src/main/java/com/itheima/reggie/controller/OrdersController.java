package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.OrdersDto;
import com.itheima.reggie.entity.AddressBook;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.AddressBookService;
import com.itheima.reggie.service.OrderDetailService;
import com.itheima.reggie.service.OrdersService;
import com.itheima.reggie.service.ShoppingCartService;
import com.sun.org.apache.xpath.internal.operations.Or;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private AddressBookService addressBookService;
    /**
     * 用户下单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> sub(@RequestBody Orders orders) {
        ordersService.submit(orders);
        return R.success("下单成功");
    }

    /**
     * 查询订单信息
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> page(@RequestParam int page, int pageSize) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDto> dtoPage = new Page<>();
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, userId);
        ordersService.page(pageInfo, queryWrapper);
        List<Orders> records = pageInfo.getRecords();
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<OrdersDto> dtoList = records.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            Long ordersId = item.getId();
            LambdaQueryWrapper<OrderDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(OrderDetail::getOrderId, ordersId);
            List<OrderDetail> detailList = orderDetailService.list(lambdaQueryWrapper);
            ordersDto.setOrderDetails(detailList);
            return ordersDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(dtoList);
        return R.success(dtoPage);
    }

    /**
     * 再来一单功能，必须是status=4
     *
     * @return
     */
    @PostMapping("/again")
    public R<String> again(@RequestBody Orders orders) {
        Long userID = BaseContext.getCurrentId();
        Long ordersId = orders.getId();
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId, ordersId);
        List<OrderDetail> detailList = orderDetailService.list(queryWrapper);
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId, userID);
        shoppingCartService.remove(lambdaQueryWrapper);

        List<ShoppingCart> shoppingCartList = detailList.stream().map((item) -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUserId(userID);
            shoppingCart.setImage(item.getImage());
            shoppingCart.setName(item.getName());
            shoppingCart.setDishFlavor(item.getDishFlavor());
            shoppingCart.setNumber(item.getNumber());
            shoppingCart.setAmount(item.getAmount());
            if (item.getDishId() != null) {
                shoppingCart.setDishId(item.getDishId());
            }
            if (item.getSetmealId() != null) {
                shoppingCart.setSetmealId(item.getSetmealId());
            }
            return shoppingCart;
        }).collect(Collectors.toList());
        shoppingCartService.saveBatch(shoppingCartList);
        return R.success("添加成功");
    }

    /**
     * 后台订单显示
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String number, String beginTime, String endTime) {
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        Page<OrdersDto> dtoPage = new Page<>();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(beginTime != null && endTime != null,Orders::getOrderTime,beginTime,endTime);
        queryWrapper.like(number != null,Orders::getNumber,number);
        queryWrapper.orderByAsc(Orders::getOrderTime);
        ordersService.page(pageInfo,queryWrapper);
        List<Orders> records = pageInfo.getRecords();
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<OrdersDto> dtoList = records.stream().map((item)->{
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item,ordersDto);
            Long addressBookId = item.getAddressBookId();
            AddressBook addressBook = addressBookService.getById(addressBookId);
            ordersDto.setAddress(addressBook.getDetail());
            ordersDto.setUserName(addressBook.getConsignee());
            return ordersDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(dtoList);
        return R.success(dtoPage);
    }


    /**
     * 修改订单状态
     * @param orders
     * @return
     */

    @PutMapping
    public R<String> update(@RequestBody Orders orders){

        ordersService.updateById(orders);
        return R.success("修改成功");
    }
}
