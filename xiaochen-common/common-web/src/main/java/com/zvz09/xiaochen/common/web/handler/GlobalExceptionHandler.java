/**
 * @projectName xiaochen
 * @package com.zvz09.xiaochen.common.web.handler
 * @className com.zvz09.xiaochen.common.web.handler.GlobalExceptionHandler
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.zvz09.xiaochen.common.web.handler;

import com.zvz09.xiaochen.common.core.response.ApiCode;
import com.zvz09.xiaochen.common.core.response.ApiResult;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandler
 *
 * @author zvz09
 * @version 1.0
 * @description 全局异常拦截器
 * @date 2023/8/28 11:11
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 请求体格式校验
     *
     * @param e 异常
     * @return 返回结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.PAYMENT_REQUIRED)
    public ApiResult<String> handleHttpMessageNotReadableException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String errorMsg = "";
        FieldError fieldError = e.getBindingResult().getFieldError();
        if (fieldError != null) {
            errorMsg = fieldError.getDefaultMessage();
        }
        return ApiResult.fail(ApiCode.INVALID_ARGUMENT_FORMAT, errorMsg);
    }


    /**
     * 业务异常
     *
     * @param e 异常
     * @return 返回结果
     */
    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public ApiResult<String> businessExceptionHandle(BusinessException e) {
        log.error("业务异常-{}", e.getMessage(), e);
        return ApiResult.fail(e.getMessage());
    }

    /**
     * 系统异常
     *
     * @param e 异常
     * @return 返回结果
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ApiResult<String> exceptionHandle(Exception e) {
        log.error("系统异常", e);
        return ApiResult.fail("系统异常");
    }
}
 