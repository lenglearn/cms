package com.briup.cms.util;

import org.springframework.beans.BeanUtils;

public class BriupBeanUtils {
    /**
     * 实现对象复制操作 属性名和类型相同
     * @param source  源对象
     * @param classname 目标对象类型
     * @return 目标对象
     * @param <T>
     */
    public static <T> T copyBean(Object source, Class<T> classname) {
        T target = null;
        try{
            target = classname.newInstance();
            BeanUtils.copyProperties(source,target);
        }catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
        return target;
    }

  /*  public static void main(String[] args) {
        Category category = new Category();
        CategoryData data = BriupBeanUtils.copyBean(category, CategoryData.class);
    }*/
}
