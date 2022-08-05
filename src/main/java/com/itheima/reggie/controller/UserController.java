package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.SMSUtils;
import com.itheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 验证码发送生成
     *
     * @param user
     * @param
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpServletRequest request) {
        String phone = user.getPhone();
        if (phone != null) {
            //生成短信验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info(code);
            //调用阿里云发送短信
//            SMSUtils.sendMessage("签名","模板code",phone,code);
            //生成的验证码保存到Session
           request.getSession().setAttribute(phone,code);
            return R.success("验证码发送成功");
        }
        return R.error("短信发送失败");
    }

    /**
     * 用户登录验证
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpServletRequest request) {
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();

        //从session中获取验证码
        Object codeSession = request.getSession().getAttribute(phone);

        //判断验证啊是否一致
        if (codeSession != null && codeSession.equals(code)) {
            //一致后判断是否为新用户是的话保存用户信息
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);

            if (user == null) {
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            //将userID存入session
            request.getSession().setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登录验证码错误失败");
    }

    /**
     * 用户退出
     * @param request
     * @return
     */
    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("user");

        return R.success("退出成功");
    }
}
