package com.briup.cms.service.dto;

import com.briup.cms.bean.basic.Article;
import com.briup.cms.bean.basic.Comment;
import com.briup.cms.bean.basic.User;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 数据传输对象：service和其他层进行数据交换的传输的信息
 */
@Data
@Accessors(chain = true)
public class ArticleDTO extends Article {
    //文章信息 继承获取
    //作者信息
    private User author;
    //一级评论信息
    private List<Comment> comments;
}
