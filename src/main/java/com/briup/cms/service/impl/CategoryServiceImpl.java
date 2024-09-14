package com.briup.cms.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.briup.cms.bean.basic.Category;
import com.briup.cms.bean.excel.CategoryData;
import com.briup.cms.bean.excel.CategoryListener;
import com.briup.cms.dao.basic.CategoryMapper;
import com.briup.cms.dao.ext.CategoryExtMapper;
import com.briup.cms.exception.CustomException;
import com.briup.cms.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.briup.cms.util.BriupBeanUtils;
import com.briup.cms.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author leng
 * @since 2024-09-13
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {
    @Autowired
    private CategoryMapper mapper;
    @Autowired
    private CategoryExtMapper extMapper;


    public void exportData2(OutputStream outputStream) {
        //1.提供一个导出栏目的类（模板）CategoryData
        //2.提供类的数据，导出数据(数据库)
        //数据库数据逻辑删除 where deleted = 0
        List<CategoryData> list = extMapper.selectAll();
        //使用工具类实现导出操作
        EasyExcel.write(outputStream, CategoryData.class)
                .sheet().doWrite(list);
    }
    @Override
    public void exportData(OutputStream outputStream) {
        //不编写sql语句，直接使用mapper对象实现查询所有栏目信息
        //默认添加deleted = 0  希望查询出所有的数据包含逻辑删除数据
        List<Category> categories = mapper.selectList(null);
        //categories  --map(编写转换逻辑)--> list
        List<CategoryData> list = categories.stream().map(category -> {
            //转换逻辑：category  CategoryData
            CategoryData data = BriupBeanUtils.copyBean(category, CategoryData.class);

            //查询父栏目的名称 select name from xxx where id = xxx
            LambdaQueryWrapper<Category> wrapper = Wrappers.lambdaQuery(Category.class)
                    .select(Category::getName)
                    .eq(Category::getId, category.getParentId());
            List<String> names = mapper.selectObjs(wrapper);
            String parentName = names.stream().findFirst().orElse("");
            data.setParentName(parentName);
            return data;
        }).collect(Collectors.toList());
        EasyExcel.write(outputStream, CategoryData.class)
                .sheet().doWrite(list);
    }

    @Override
    public void importData(InputStream inputStream) {
        //1.自定义一个监听器，实现解析和读取数据
        CategoryListener listener = new CategoryListener(mapper);
        //2.导入数据
        EasyExcel.read(inputStream,CategoryData.class,listener).sheet().doRead();
    }

    public void importData2(InputStream inputStream) {
        //导出数据：
        EasyExcel.read(inputStream,CategoryData.class,new PageReadListener<CategoryData>(list -> {
            List<Category> categories = list.stream().map(categoryData -> {
                //根据父栏目名称获取栏目对象
                Category parent = new LambdaQueryChainWrapper<>(mapper)
                        .eq(Category::getName, categoryData.getParentName())
                        .oneOpt()
                        .orElseThrow(() -> new CustomException(ResultCode.PCATEGORY_IS_INVALID));
                Category category = BriupBeanUtils.copyBean(categoryData, Category.class);
                category.setParentId(parent.getId());
                return category;
            }).collect(Collectors.toList());
            //保存数据
            mapper.insert(categories);
        },Integer.MAX_VALUE)).sheet().doRead();
    }

}