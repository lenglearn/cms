package com.briup.cms;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.briup.cms.bean.basic.Category;
import com.briup.cms.bean.excel.CategoryData;
import com.briup.cms.dao.basic.CategoryMapper;
import com.briup.cms.exception.CustomException;
import com.briup.cms.service.IArticleService;
import com.briup.cms.util.BriupBeanUtils;
import com.briup.cms.util.ResultCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class CmsApplicationTests {

    @Autowired
    private CategoryMapper mapper;
    @Autowired
    private IArticleService service;
    @Test
    void contextLoads() {
        //只能查询出逻辑未删除的数据
        // select * from where deleted = 0 and ( deleted = 1)
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getName,"文学");
        List<Category> categories = mapper.selectList(wrapper);
        //查询所有数据
    }
    @Test
    public void method(){
        LambdaQueryWrapper<Category> wrapper = Wrappers.lambdaQuery(Category.class)
                .select(Category::getName)
                .eq(Category::getId,1);
        List<String> objects = mapper.selectObjs(wrapper);
        //获取第一个元素
        String name = objects.stream().findFirst().orElse("");
        System.out.println(name);
    }
    @Test
    public void method2(){
        //通过工具类读取到数据
        List<CategoryData> list = new ArrayList<>();
        list.add(new CategoryData("二级栏目","描述信息",1,0,"文学"));
        //1.判断读取到数据中父栏目是否存在？抛出异常 数据错误
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
        //2.获取到List<Category>
        System.out.println(categories);
        mapper.insert(categories);
    }
    @Test
    public void method3(){

    }

}
