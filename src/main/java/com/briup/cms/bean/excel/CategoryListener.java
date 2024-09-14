package com.briup.cms.bean.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.briup.cms.bean.basic.Category;
import com.briup.cms.dao.basic.CategoryMapper;
import com.briup.cms.exception.CustomException;
import com.briup.cms.exception.ExcelException;
import com.briup.cms.util.BriupBeanUtils;
import com.briup.cms.util.ResultCode;

import java.util.ArrayList;
import java.util.List;

public class CategoryListener implements ReadListener<CategoryData> {
    private List<Category> cacheList = new ArrayList<>();
    private CategoryMapper mapper;
    public CategoryListener(CategoryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void invoke(CategoryData categoryData, AnalysisContext analysisContext) {
        //解析每一行：将数据保存到一个集合中

        //1.将CategoryData对象转化为category
        Category category = BriupBeanUtils.copyBean(categoryData, Category.class);
        //父栏目是否存在？
        //比较和数据库
        Category parent = new LambdaQueryChainWrapper<>(mapper)
                .eq(Category::getName, categoryData.getParentName())
                .oneOpt()
                .orElseThrow(() -> new CustomException(ResultCode.PCATEGORY_IS_INVALID));
        //比较表格中是否该父栏目新增栏目。
        category.setParentId(parent.getId());
        cacheList.add(category);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //当解析完成后，才进行入库操作：
        mapper.insert(cacheList);
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        //当解析数据时，如果数据类型错误
        if(exception instanceof ExcelDataConvertException){
            ExcelDataConvertException ex = (ExcelDataConvertException) exception;
            //获取具体的错误数据位置(从0开始表示第一行)
            Integer columnIndex = ex.getColumnIndex();
            Integer rowIndex = ex.getRowIndex();
            //将错误信息返回抛出web--统一异常处理
            String message = "第"+(rowIndex+1)+"行第"+(columnIndex+1)+"列数据格式错误";
            throw new ExcelException(message);
        }else {
            throw new CustomException(ResultCode.DATA_WRONG);
        }
    }
}