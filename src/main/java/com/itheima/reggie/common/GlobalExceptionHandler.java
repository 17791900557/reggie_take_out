package com.itheima.reggie.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        if (ex.getMessage().contains("for key")) {
            String[] split = ex.getMessage().split(" ");
            System.out.println(split[5]);
            if (split[5].equals("'employee.phone'")){
                return R.error( "手机号已存在");
            }else if (split[5].equals("'employee.id_number'")){
                return R.error("身份证号已存在");
            }else if (split[5].equals("'account.PRIMARY'")){
                return R.error("账户已存在");
            }
        }
        return R.error("未知错误");
    }

    /**
     * 自定义异常处理器
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex) {
        return R.error(ex.getMessage());
    }
}
