package com.briup.cms.service.impl;

import com.briup.cms.bean.basic.User;
import com.briup.cms.dao.basic.UserMapper;
import com.briup.cms.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author leng
 * @since 2024-09-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
