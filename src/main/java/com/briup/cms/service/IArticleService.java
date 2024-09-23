package com.briup.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.briup.cms.bean.basic.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.briup.cms.service.dto.ArticleDTO;
import com.briup.cms.web.vo.ArticleVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author leng
 * @since 2024-09-18
 */
public interface IArticleService extends IService<Article> {


    /**
     * 分页多条件多表查询
     * @param vo  参数对象
     * @return
     */
    IPage findByPage(ArticleVO vo);

    Article findById(Integer id);
}
