package com.briup.cms.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.briup.cms.bean.basic.Article;
import com.briup.cms.dao.basic.ArticleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//文章阅读量监听器：当项目启动时，
@Configuration
@Slf4j
public class ReadNumListener {
    @Autowired
    private ArticleMapper mapper;


    @EventListener(ApplicationStartedEvent.class)
    public void initReadNum(){
        log.info("启动项目，初始化redis数据");
        LambdaQueryWrapper<Article> wrapper = Wrappers.lambdaQuery(Article.class)
                .select(Article::getId,Article::getReadNum);
        List<Map<String, Object>> list = mapper.selectMaps(wrapper);
        log.info(list.toString());
        Map<Object, Object> map = list.stream().collect(Collectors.toMap(m -> m.get("id"),
                                                                         m -> m.get("readNum")));
        log.info(map.toString());


    }
}
