package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import com.itheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private StringRedisTemplate redisTemplate;


    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody Employee employee, HttpServletRequest request) {
        String phone = employee.getPhone();
        if (phone != null) {
            //生成短信验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info(code);
            //调用阿里云发送短信
//            SMSUtils.sendMessage("签名","模板code",phone,code);
            //生成的验证码保存到Session
            //request.getSession().setAttribute(phone, code);
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return R.success("验证码发送成功");
        }
        return R.error("短信发送失败");
    }

    /**
     * 登陆功能实现
     *
     * @param request
     * @param map
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Map map) {
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();


        Object codeSession = redisTemplate.opsForValue().get(phone);
        //判断验证啊是否一致
        if (code != null && code.equals(codeSession)) {
            //一致后判断是否为新用户是的话保存用户信息
            LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Employee::getPhone, phone);
            Employee employee = employeeService.getOne(queryWrapper);
            if (employee == null) {
                employee = new Employee();
                employee.setPhone(phone);
                employee.setName("员工");
                employee.setSex("男");
                employee.setStatus(1);
                employee.setIdNumber("610113200009211611");
                employeeService.save(employee);
            }
            if (employee.getStatus() == 0) {
                return R.error("账户已被禁用");
            }
            //将userID存入session
            request.getSession().setAttribute("employee", employee.getId());
            //登陆成功删除验证码
            redisTemplate.delete(phone);
            return R.success(employee);
        }
        return R.error("登录验证码错误");
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
     * @param
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save( @RequestBody Employee employee) {
        log.info(employee.toString());
        employeeService.save(employee);
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
        queryWrapper.orderByDesc(Employee::getId);
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
    public R<String> update( @RequestBody Employee employee) {

        Long currentId = BaseContext.getCurrentId();
        if (currentId != 1){
            return R.error("您没有权限");
        }else {
            employeeService.updateById(employee);
            return R.success("信息修改成功");
        }
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
