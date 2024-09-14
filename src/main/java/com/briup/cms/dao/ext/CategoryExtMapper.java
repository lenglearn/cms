package com.briup.cms.dao.ext;

import com.briup.cms.bean.excel.CategoryData;

import java.util.List;

//在mybatisplus中不能mapper接口进行继承
public interface CategoryExtMapper{
    List<CategoryData> selectAll();
}
