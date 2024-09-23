package com.briup.cms.dao.ext;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.briup.cms.bean.basic.Slideshow;

import java.util.List;

public interface SlideshowExtMapper extends BaseMapper<Slideshow> {
    public int getSizeByIds(List<Integer> ids);
}
