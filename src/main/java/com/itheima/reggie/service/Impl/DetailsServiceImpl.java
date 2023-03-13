package com.itheima.reggie.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.Details;
import com.itheima.reggie.mapper.DetailsMapper;
import com.itheima.reggie.service.DetailsService;
import org.springframework.stereotype.Service;

@Service
public class DetailsServiceImpl extends ServiceImpl<DetailsMapper, Details> implements DetailsService {
}
