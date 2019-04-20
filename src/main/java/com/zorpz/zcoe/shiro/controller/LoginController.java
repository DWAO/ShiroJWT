package com.zorpz.zcoe.shiro.controller;

import com.zorpz.zcoe.shiro.auth.JwtUtil;
import com.zorpz.zcoe.shiro.cache.JedisClient;
import com.zorpz.zcoe.shiro.common.Result;
import com.zorpz.zcoe.shiro.common.ResultGenerator;
import com.zorpz.zcoe.shiro.config.RedisConfig;
import com.zorpz.zcoe.shiro.constant.SecurityConstant;
import com.zorpz.zcoe.shiro.entity.User;
import com.zorpz.zcoe.shiro.service.UserService;
import com.zorpz.zcoe.shiro.utils.EncryptUtils;
import com.zorpz.zcoe.shiro.utils.StrUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 登录
 *
 * @author Punk
 * @date 2019/04/10
 */
@Controller
public class LoginController {


    @Autowired
    private JedisClient redisClient;

    @Autowired
    private RedisConfig redisConfig;

    @RequestMapping("a")
    @ResponseBody
    private String test() {
        return "a";
    }

    @Autowired
    private UserService userService;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    private String login() {
        return "login";
    }


    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    private Result login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        User user = userService.findByUsername(username);
        String salt = user.getSalt();
        String encryptPassword = user.getPassword();
        String encryptStr = EncryptUtils.encryptPassword(password, salt);

        if (user == null || user.getDisabled()) {
            throw new AuthenticationException("用户不存在！");
        } else if (!encryptPassword.equals(encryptStr)) {
            throw new AuthenticationException(("账号和密码不一致！"));
        }

        long now = System.currentTimeMillis();
        String token = JwtUtil.sign(username, encryptPassword, now);

        long refreshTime = redisConfig.expireTime;
        String signTime = StrUtils.obj2String(now);

        String oldToken = redisClient.get(username, 1);

        // 缓存token以及签发时间
        redisClient.set(username, token, 1);
        redisClient.set(token, signTime, 1);

        if (oldToken!=null) {
            redisClient.set(token+ SecurityConstant.PREKEY, token, 1);
        }

        response.addHeader("Authorization", token);
        return ResultGenerator.genSuccessResult();
    }


}
