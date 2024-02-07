package com.onlinehosp.yygh.common.exception;


import com.onlinehosp.yygh.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
//@ControllerAdvice 是 Spring 框架中用于处理全局异常的注解。
// 在 Spring MVC 中，@ControllerAdvice 注解的类可以包含全局控制器建议（advice），主要用于集中处理异常和全局性的控制器配置。
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)  //当出现Exception.class类型的异常时就会执行此方法
    @ResponseBody //使得方法能够返回json数据
    public Result error(Exception e){
        e.printStackTrace();
        return Result.ok();
    }


    @ExceptionHandler(YyghException.class)
    @ResponseBody
    public Result error(YyghException e){
        e.printStackTrace();
        //抛出异常信息
        return Result.build(e.getCode(),e.getMessage());
    }
}
