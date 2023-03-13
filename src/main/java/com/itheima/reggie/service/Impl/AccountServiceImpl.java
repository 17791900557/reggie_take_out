package com.itheima.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;


import com.itheima.reggie.entity.Account;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.AccountMapper;

import com.itheima.reggie.service.AccountService;
import com.itheima.reggie.service.DishFlavorService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Autowired
    private AccountService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;



}
