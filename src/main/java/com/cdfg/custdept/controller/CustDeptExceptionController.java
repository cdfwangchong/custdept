package com.cdfg.custdept.controller;


import cn.cdfg.exceptionHandle.SelfMailNotFoundException;
import com.cdfg.custdept.pojo.until.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 自定义异常接口
 */
@CrossOrigin
@ControllerAdvice
public class CustDeptExceptionController {
    @ExceptionHandler(value = SelfMailNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @RequestMapping(produces="json/html; charset=UTF-8")
    @ResponseBody

    public Result<String> exception (SelfMailNotFoundException exception){

        int code= exception.getResultCode();
        String Msg = exception.getMsg();
        System.out.println(code+Msg);
        return new Result<String>(code,Msg,"");
    }
}
