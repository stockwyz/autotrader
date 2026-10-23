package com.mcwilliamauto.common;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Result<?> handleValidException(Exception e) {
        String msg = "Validation failed";
        if (e instanceof MethodArgumentNotValidException ex) {
            FieldError fe = ex.getBindingResult().getFieldError();
            if (fe != null) msg = fe.getField() + ": " + fe.getDefaultMessage();
        } else if (e instanceof BindException ex) {
            FieldError fe = ex.getBindingResult().getFieldError();
            if (fe != null) msg = fe.getField() + ": " + fe.getDefaultMessage();
        }
        return Result.fail(400, msg);
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        e.printStackTrace();
        return Result.fail("Internal server error: " + e.getMessage());
    }
}
