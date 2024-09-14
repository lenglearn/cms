package com.briup.cms.util;


import com.briup.cms.exception.CustomException;

public abstract class BriupAssert {
    /**
     * 判断字符是否为空，如果为空抛出异常
     * @param text
     * @param rc
     */
    public static void hasText(String text,ResultCode rc){
        if(text == null || "".equals(text.trim())){
            throw new CustomException(rc);
        }
    }
    public static void notNull(Object obj,ResultCode rc){
        if(obj == null){
            throw new CustomException(rc);
        }
    }
    public static void repeat(Object obj,ResultCode rc){
        if (obj != null){
            throw new CustomException(rc);
        }
    }

    /**
     *
     * @param expression
     * @param rc
     */
    public static void isTrue(boolean expression,ResultCode rc){
        if (!expression){
            throw new CustomException(rc);
        }
    }
}
