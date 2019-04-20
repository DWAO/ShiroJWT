package com.zorpz.zcoe.shiro.service;


import com.zorpz.zcoe.shiro.entity.User;

public interface UserService {


    User findByUsername(String username);
}
