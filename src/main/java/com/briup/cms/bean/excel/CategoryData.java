package com.briup.cms.bean.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.briup.cms.bean.basic.Category;
import com.briup.cms.dao.basic.CategoryMapper;
import com.briup.cms.util.IOCUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 栏目信息的导入导出类
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryData {
    @ExcelProperty("栏目名称")
    private String name;

    @ExcelProperty("栏目描述")
    private String description;

    @ExcelProperty("栏目序号")
    private Integer orderNum;

    @ExcelProperty(value = "栏目删除状态",converter = DeletedConvert.class)
    private Integer deleted;

    @ExcelProperty("父栏目")
    private String parentName;
    //实际业务：有N个列的数据需要转换为其他类型格式的数据 提供N个转化器类,定义成内部类使用
    public static class DeletedConvert implements Converter<Integer> {


        @Override
        public WriteCellData<?> convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
            //逻辑处理0 和 1 显示问题
            String deleted = value == 0 ? "未删除":"已删除";
            //在spring中如何使用Bean对象？ 只能在其他bean对象注入使用bean对象
            //如何在任意的位置获取到一个Bean对象?


            return new WriteCellData<>(deleted);
        }

        @Override
        public Integer convertToJavaData(ReadConverterContext<?> context) throws Exception {
            //逻辑处理文字描述转数字
            Category c = Db.getById(1, Category.class);
            System.out.println("基于Mybatis静态方法查询："+c);
            //通过工具类实现获取ioc容器-->获取一个student对象
            CategoryMapper mapper = IOCUtil.context.getBean(CategoryMapper.class);
            Category c2 = mapper.selectById(1);
            System.out.println("基于IOCUtil静态方法查询："+c2);
            return Objects.equals(context.getReadCellData().getStringValue(),"未删除")? 0 : 1 ;
        }
    }

}