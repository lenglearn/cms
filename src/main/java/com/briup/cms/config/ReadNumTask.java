package com.briup.cms.config;

import com.briup.cms.dao.basic.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class ReadNumTask {
    @Autowired
    private ArticleMapper mapper;
    @Scheduled(cron = "*/5 * * * * *")
    public void saveReadNum(){

    }
}
