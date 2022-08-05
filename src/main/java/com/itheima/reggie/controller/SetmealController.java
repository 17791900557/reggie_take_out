package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishService dishService;

    /**
     * 新增套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {

        setmealService.saveWithDish(setmealDto);

        return R.success("套餐保存成功");

    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //模糊查询
        queryWrapper.like(!StringUtils.isEmpty(name), Setmeal::getName, name);
        //排序
        queryWrapper.orderByAsc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, queryWrapper);

        Page<SetmealDto> setmealDtoPage = new Page<>();
        //拷贝除了records以外所有数据
        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");

        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //把records集合中的值拷贝到Dto中
            BeanUtils.copyProperties(item,setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }

            return setmealDto;
        }).collect(Collectors.toList());
        //将套餐名称信息传到dto对象
        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);

    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> remove(@RequestParam List<Long> ids){

        setmealService.removeWithDish(ids);

        return R.success("删除成功");

    }


    /**
     * 启售停售
     * @param status
     * @param ids
     * @param setmeal
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> update(@PathVariable int status , @RequestParam List<Long> ids, Setmeal setmeal) {
        setmeal.setStatus(status);

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        setmealService.update(setmeal,queryWrapper);
        return R.success("修改成功");
    }

    /**
     * 回显数据
     * @param id
     * @return
     */
     @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id){

         SetmealDto setmealDto = setmealService.getByIdWithDish(id);

         return R.success(setmealDto);
     }

    /**
     * 修改套餐
     * @param setmealDto
     * @return
     */
     @PutMapping
     public R<String> update(@RequestBody SetmealDto setmealDto){
         setmealService.updateWithDish(setmealDto);

         return R.success("修改成功");
     }


    /**
     * 查询正在售卖的套餐
     * @param setmeal
     * @return
     */
     @GetMapping("/list")
     public R<List<SetmealDto>> list(Setmeal setmeal){
         LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
         queryWrapper.eq(Setmeal::getCategoryId,setmeal.getCategoryId());
         queryWrapper.eq(Setmeal::getStatus,1);
         List<Setmeal> setmealList = setmealService.list(queryWrapper);

         List<SetmealDto> dto = setmealList.stream().map((item)->{
             SetmealDto setmealDto = new SetmealDto();
             BeanUtils.copyProperties(item,setmealDto);
             LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
             lambdaQueryWrapper.eq(SetmealDish::getSetmealId,item.getId());
             List<SetmealDish> setmealDishList = setmealDishService.list(lambdaQueryWrapper);
             setmealDto.setSetmealDishes(setmealDishList);
             return setmealDto;
         }).collect(Collectors.toList());
         return R.success(dto);
     }

     @GetMapping("/dish/{id}")
     public R<List<DishDto>> dish(@PathVariable Long id){

         LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
         queryWrapper.eq(SetmealDish::getSetmealId,id);
         List<SetmealDish> dishList = setmealDishService.list(queryWrapper);
         List<DishDto> dishDtoList = dishList.stream().map((item)->{
             DishDto dishDto = new DishDto();
             dishDto.setName(item.getName());
             dishDto.setCopies(item.getCopies());
             Long dishId = item.getDishId();
             Dish dish = dishService.getById(dishId);
             dishDto.setImage(dish.getImage());
             dishDto.setDescription(dish.getDescription());
             dishDto.setPrice(dish.getPrice());
             return dishDto;
         }).collect(Collectors.toList());


         return R.success(dishDtoList);
     }
}
