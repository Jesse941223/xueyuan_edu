package com.xueyuan.eduservice.handler;

import com.xueyuan.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
//做通知的操作，用到aop
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    //两个注解，1，定义只要是异常就执行这个方法
    //2. 返回数据
    @ResponseBody
    public R error( Exception e){
        //可以将异常打印，也可以不打印
        e.printStackTrace();

        return R.error().message("出现了异常");
    }
@ExceptionHandler(ArithmeticException.class)
@ResponseBody
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("0不能作为除数");
    }

    /**
     *
     * 自定义异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(EduException.class)
    @ResponseBody
    public R error(EduException e){

        log.error(e.getMessage());
        return R.error().code(e.getCode()).message(e.getMessage());
    }

}
