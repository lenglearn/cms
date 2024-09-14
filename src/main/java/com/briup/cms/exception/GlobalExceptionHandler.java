package com.briup.cms.exception;



import com.briup.cms.util.Result;
import com.briup.cms.util.ResultCode;
import com.briup.cms.util.ResultUtil;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception ex){
        //1.打印异常到控制台：
        ex.printStackTrace();
        //2.处理业务异常,将异常信息显示在前端
        if(ex instanceof CustomException){
            return ResultUtil.error(((CustomException) ex).getCode());
        }
        //处理解析表格异常
        if(ex instanceof ExcelException){
            return ResultUtil.error(ex.getMessage());
        }

        //3.处理非业务异常，提示统一的返回信息
        return ResultUtil.error(ResultCode.SYSTEM_INNER_ERROR);
    }
}