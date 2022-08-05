package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 登陆功能实现
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //1.将页面提交的password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2.根据页面提交的的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        //通过上述方法来获取数据库中所有数据，不能使用getById因为提供的是userName
        Employee emp = employeeService.getOne(queryWrapper);

        //3.如果没有查询到则返回登陆失败结果
        if (emp == null) {
            return R.error("登陆失败，用户名不存在。");
        }

        //4.密码比对，如果不一样返回失败
        if (!emp.getPassword().equals(password)) {
            return R.error("登陆失败，密码错误");
        }

        //5.查看员工状态status如果禁用则返回已禁用
        if (emp.getStatus() == 0) {
            return R.error("账户已被禁用");
        }
        //6.登陆成功，将员工ID存入Session并返回登陆成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 退出功能
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //清楚session中的数据
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增功能
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(employee.toString());
        //1.设置密码初始值
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//         //获取当前登录用户的ID
//        Long empID = (Long) request.getSession().getAttribute("employee");
//
//        employee.setCreateUser(empID);
//        employee.setUpdateUser(empID);

        try {
            employeeService.save(employee);
        } catch (Exception e) {
           R.error("新增员工失败");
        }
        return R.success("添加成功");
    }

    /**
     * 分页查询
     * <page>是MP提供的一个类型因为这个
     * 类型中的records可以根据前端中需要的数据进行返沪回
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> pageR(int page, int pageSize, String name) {

        log.info("page={},pageSize{},name={}", page, pageSize, name);
        Page pageInfo = new Page(page, pageSize);

        //构造条件查询器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件就相当于在sql语句后加上了like name = y;
        queryWrapper.like(!StringUtils.isEmpty(name), Employee::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getCreateTime);
        //调用查询
        employeeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);

    }

    /**
     * 根据id修改
     * Long数据转页面Json有精度丢失
     * 处理方法将数据转为String类型
     *
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(employee.toString());
//        Long empId = (Long) request.getSession().getAttribute("employee");

//        employee.setUpdateUser(empId);
//        employee.setUpdateTime(LocalDateTime.now());

        employeeService.updateById(employee);
        return R.success("信息修改成功");
    }


    /**
     * 按Id查询回显数据
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {

        Employee emp = employeeService.getById(id);
        if (emp != null) {
            return R.success(emp);
        }
        return R.error("没有查询到此员工信息");


    }

}
