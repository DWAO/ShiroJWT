package com.zorpz.zcoe.shiro.service.impl;

import com.zorpz.zcoe.shiro.dao.UserDao;
import com.zorpz.zcoe.shiro.entity.User;
import com.zorpz.zcoe.shiro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Punk
 * @date 2019/04/10
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
