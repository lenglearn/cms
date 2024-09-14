package com.briup.cms.exception;

/**
 * 用户自定义异常（业务异常）
 */
public class ExcelException extends RuntimeException{
    private String message;
    public ExcelException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
