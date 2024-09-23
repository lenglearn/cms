package com.briup.cms.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.briup.cms.bean.basic.Article;
import com.briup.cms.service.IArticleService;
import com.briup.cms.service.dto.ArticleDTO;
import com.briup.cms.util.Result;
import com.briup.cms.util.ResultUtil;
import com.briup.cms.web.vo.ArticleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author leng
 * @since 2024-09-18
 */
@RestController
@Api(tags = "资讯模块")
public class ArticleController {
    @Autowired
    private IArticleService service;

    @ApiOperation("分页多条件查询文章信息")
    @PostMapping("/auth/article/query")
    public Result findByPage(@RequestBody ArticleVO vo){
        IPage<ArticleDTO> page =  service.findByPage(vo);
        return ResultUtil.success(page);
    }
    @ApiOperation("根据文章编号查询文章相关信息")
    @GetMapping("/articles/{id}")
    public Result findById(@PathVariable Integer id) {
        Article article = service.findById(id);
        return ResultUtil.success(article);
    }
}
