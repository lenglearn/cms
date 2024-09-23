package com.briup.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.briup.cms.bean.basic.Slideshow;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author leng
 * @since 2024-09-13
 */
public interface ISlideshowService {
    Slideshow queryById(Integer id);

    void deleteInBatch(List<Integer> ids);

    void saveOrUpdate(Slideshow slideshow);

    List<Slideshow> queryAllEnable();

    IPage<Slideshow> query(Integer page, Integer pageSize, String status, String desc);
}
