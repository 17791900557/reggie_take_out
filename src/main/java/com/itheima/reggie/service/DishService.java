package com.itheima.reggie.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    /**
     *   保存菜品到菜品表，以及菜品口味到菜品口味表
     * @param dishDto
     */
    public void saveWith(DishDto dishDto);

    /**
     * 修改回显口味以及基本信息
     * @param id
     * @return
     */
    public DishDto getByIdWithFlavor(Long id);
    /**
     * 修改菜品信息
     * @param dishDto
     */
    void updateWithFlavor(DishDto dishDto);
    /**
     * 删除菜品信息
     * @param ids
     */
    void deleteWithFlavor(List<Long> ids);
}

