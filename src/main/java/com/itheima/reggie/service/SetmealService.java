package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.SetmealDto;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    /**
     * 保存套餐信息以及关联菜品
     * @param setmealDto
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 批量删除
     * @param ids
     */
    void removeWithDish(List<Long> ids);

    /**
     * 回显数据
     * @param id
     * @return
     */
    SetmealDto getByIdWithDish(Long id);

    /**
     * 修改数据
     * @param setmealDto
     */
    void updateWithDish(SetmealDto setmealDto);
}
