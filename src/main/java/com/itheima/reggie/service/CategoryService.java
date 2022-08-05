package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Category;

public interface CategoryService extends IService<Category> {

    /**
     * 根据Id特殊条件删除
     * @param id
     */
    void remove(Long id);
}
