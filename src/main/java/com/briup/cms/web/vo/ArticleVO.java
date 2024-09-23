package com.briup.cms.web.vo;

import lombok.Data;

/**
 * VO对象 与浏览器交互的对象
 */
@Data
public class ArticleVO {
    private Integer pageNum;
    private Integer pageSize;
    private String title;
    private String status;
}
