package com.briup.cms.service;

import com.briup.cms.bean.basic.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author leng
 * @since 2024-09-13
 */
public interface ICategoryService extends IService<Category> {
    /**
     * 导出数据到字节输出流
     * @param outputStream
     */
    void exportData(OutputStream outputStream);

    /**
     * 从字节输入流导入数据
     * @param inputStream
     */
    void importData(InputStream inputStream);
}
