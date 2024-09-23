package com.briup.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.briup.cms.bean.basic.Article;
import com.briup.cms.bean.basic.Comment;
import com.briup.cms.bean.basic.User;
import com.briup.cms.dao.basic.ArticleMapper;
import com.briup.cms.dao.basic.CommentMapper;
import com.briup.cms.dao.basic.UserMapper;
import com.briup.cms.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.briup.cms.service.dto.ArticleDTO;
import com.briup.cms.util.BriupBeanUtils;
import com.briup.cms.util.RedisUtil;
import com.briup.cms.web.vo.ArticleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author leng
 * @since 2024-09-18
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
    @Autowired
    private RedisTemplate redisTemplate;

    public IPage findByPage(ArticleVO vo) {
        //通过Mybatisplus实现将PO对象转化为DTO对象
        //多表查询： Mybatisplus不支持多表查询(@TableName) ,只能分开查询，将结果组合起来service
        //1.分页多条件查询数据库信息，返回结果封装PO对象
        Page<Article> page = new Page<>(vo.getPageNum(), vo.getPageSize());
        //包含文章对象的集合,提供分页查询和查询的条件
        LambdaQueryWrapper<Article> wrapper = Wrappers.lambdaQuery(Article.class).like(Article::getTitle, vo.getTitle());
        //2.转化后成为文章DTO对象的集合--
        List dtoList = Db.list(page, wrapper).stream()
                .map(article -> BriupBeanUtils.copyBean(article, ArticleDTO.class)
                        .setAuthor(Db.lambdaQuery(User.class).eq(User::getId,article.getUserId()).one())
                        .setComments(Db.lambdaQuery(Comment.class).eq(Comment::getArticleId,article.getId()).list())
                ).collect(Collectors.toList());
        //将原来的po对象替换为DTO对象
        page.setRecords(dtoList);
        return page;
    }

    @Override
    public Article findById(Integer id) {
        //从数据库查询文章信息
        Article article = Db.lambdaQuery(Article.class).eq(Article::getId, id).one();
        if(article != null){
            //当用户访问时，在指定的文章的原来的阅读量+1
            Long readNum = RedisUtil.increment("readNum", id.toString(), 1);
            //为了最新的文章阅读量，从redis中获取最新的文章阅读量
            article.setReadNum(readNum.intValue());
        }
        return article;
    }
}