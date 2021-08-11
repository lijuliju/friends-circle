package com.friends.exception;

import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.friends.result.Result;
import com.friends.result.ResultFactory;


/**
 * Global exception handler.
 *
 * @author Evan
 * @date 2019/11
 */
@ControllerAdvice
@ResponseBody
public class DefaultExceptionHandler {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(DefaultExceptionHandler.class);
	
    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(Exception e) {
        String message = null;

        if (e instanceof IllegalArgumentException) {
            message = "传入了错误的参数";
        }

        if (e instanceof MethodArgumentNotValidException) {
            message = ((MethodArgumentNotValidException) e).getBindingResult().getFieldError().getDefaultMessage();
        }

        if (e instanceof UnauthorizedException) {
            message = "权限认证失败";
        }

//        System.out.println("未知异常！原因是:"+e);
        logger.error("未知异常！原因是:",e);
        if(StringUtils.isEmpty(message)) {
//        	System.out.println("未知异常！原因是:"+e.getMessage());
        	return ResultFactory.buildFailResult(e.getMessage());
        }
        return ResultFactory.buildFailResult(message);
    }
    
}
