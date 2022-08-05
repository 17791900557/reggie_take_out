package com.itheima.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;

import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    /**
     * 保存菜品到Dish以及菜品口味到Dishflavor
     * @param dishDto
     */
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Transactional
    public void saveWith(DishDto dishDto) {

        //保存菜品信息
        this.save(dishDto);
        Long DishId = dishDto.getId();
        //保存口味信息，要先将菜品。id导入到list
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item)->{
            item.setDishId(DishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 修改回显
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);

        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(flavors);

        return dishDto;
    }

    /**
     * 修改数据
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);
        Long dishId = dishDto.getId();
        //先清除口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishId);
        dishFlavorService.remove(queryWrapper);

        //在保存新的
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);

    }

    /**
     * 批量删除
     * @param ids
     */
    @Override
    @Transactional
    public void deleteWithFlavor(List<Long> ids) {
//        for (Long id : ids) {
//            dishService.removeById(id);
//            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(DishFlavor::getDishId, id);
//            dishFlavorService.remove(queryWrapper);
//        }
        //先判断菜品是否正在售卖中

        LambdaQueryWrapper<Dish> queryWrapper =new LambdaQueryWrapper<>();
        //dishId in ids
        queryWrapper.in(Dish::getId,ids);
        //status = 1不能删除
        queryWrapper.eq(Dish::getStatus,1);
        int count = this.count(queryWrapper);
        //如果>0不能删除抛出异常提示信息
        if (count > 0){
            throw new CustomException("有菜品正在售卖不能删除");
        }
        //删除对应菜品
        this.removeByIds(ids);
        LambdaQueryWrapper<DishFlavor> queryWrapperFlavor = new LambdaQueryWrapper<>();
        queryWrapperFlavor.in(DishFlavor::getDishId,ids);
        dishFlavorService.remove(queryWrapperFlavor);
    }
}
