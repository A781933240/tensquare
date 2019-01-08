package com.tensquare.spit.base.controller;


import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *  统一处理异常类
 */
@RestControllerAdvice   //拦截所有controller
public class BaseExceptionHandler {

    @ExceptionHandler(Exception.class) //异常处理的注解；//拦截所有的异常
    public Result error(Exception e){
        return new Result(false, StatusCode.ERROR,e.getMessage());
    }

}
