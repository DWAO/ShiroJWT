package com.zorpz.zcoe.shiro.auth;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.zorpz.zcoe.shiro.cache.JedisClient;
import com.zorpz.zcoe.shiro.common.Result;
import com.zorpz.zcoe.shiro.common.ResultGenerator;
import com.zorpz.zcoe.shiro.config.RedisConfig;
import com.zorpz.zcoe.shiro.constant.SecurityConstant;
import com.zorpz.zcoe.shiro.entity.User;
import com.zorpz.zcoe.shiro.service.UserService;
import com.zorpz.zcoe.shiro.utils.StrUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class JwtFilter extends BasicHttpAuthenticationFilter {

    @Autowired
    JedisClient redisClient;

    @Autowired
    UserService userService;

    @Autowired
    RedisConfig redisConfig;


    /**
     * 检测Header里Authorization字段
     * 判断是否登录
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        return getAuthorization(request) != null;
    }

    /**
     * 返回token
     * @param request
     * @param response
     * @return
     */
    private String getAuthorization(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        return (String) req.getHeader("Authorization");
    }

    /**
     * 登录验证
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response)  {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");

        JwtToken token = new JwtToken(authorization);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(token);

        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 刷新AccessToken，进行判断RefreshToken是否过期，未过期就返回新的AccessToken且继续正常访问
     */
    private boolean refreshToken(ServletRequest request, ServletResponse response) {
        String token = this.getAuthzHeader(request);
        String username = JwtUtil.getClaimAsString(token, SecurityConstant.ACCOUNT);

        if (redisClient.exists(token, 1)) {
            // 获取RefreshToken时间戳,及AccessToken中的时间戳,相比如果一致，进行AccessToken刷新
            long signTime = StrUtils.str2Long(redisClient.get(token, 1));
            long tokenTime = JwtUtil.getClaimAsLong(token, SecurityConstant.SIGN_TIME);


            if (tokenTime==tokenTime) {

                // 设置RefreshToken中的时间戳为当前最新时间戳
                long currentTimeMillis = System.currentTimeMillis();
                long expireTime = redisConfig.expireTime;
                String sign_time = StrUtils.obj2String(currentTimeMillis + expireTime);

                redisClient.set(token, sign_time,1);

                // 刷新AccessToken，为当前最新时间戳
                User user = userService.findByUsername(username);
                token = JwtUtil.sign(username, user.getPassword() ,null);

                // 使用AccessToken 再次提交给ShiroRealm进行认证，如果没有抛出异常则登入成功，返回true
                JwtToken jwtToken = new JwtToken(token);
                this.getSubject(request, response).login(jwtToken);

                // 设置响应的Header头新Token
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setHeader("Authorization", token);
                httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
                return true;
            }
        }
        return false;
    }

    /**
     * 是否允许访问
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            try {
                String authorization = getAuthorization(request);
                // 不在缓存中默认认证失败
                if (!redisClient.exists(authorization, 1)) {
                    throw new TokenExpiredException("token 无效");
                }
                this.executeLogin(request, response);
            } catch (Exception e) {
                String msg = e.getMessage();
                Throwable throwable = e.getCause();
                if (throwable != null && throwable instanceof SignatureVerificationException) {
                    msg = "Token或者密钥不正确(" + throwable.getMessage() + ")";
                } else if (throwable != null && throwable instanceof TokenExpiredException) {
                    // AccessToken已过期
                    if (this.refreshToken(request, response)) {
                        return true;
                    } else {
                        msg = "Token已过期";
                    }
                } else {
                    if (throwable != null) {
                        msg = throwable.getMessage();
                    }
                }
                try {
                    this.response401(request, response, msg);
                } catch (IOException e1) {
                    log.error("返回Response信息出现IOException异常:" + e1.getMessage());
                    e1.printStackTrace();
                }
                return false;
            }
        }
        return true;
    }

    /**
     * 401非法请求
     * @param req
     * @param resp
     */
    private void response401(ServletRequest req, ServletResponse resp, String message) throws IOException {
        HttpServletResponse response = WebUtils.toHttp(resp);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        out = response.getWriter();

        Result result = ResultGenerator.genFailureResult(HttpServletResponse.SC_UNAUTHORIZED, message);
        out.write(JSON.toJSONString(result));

    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        // 过滤链终止
        return false;
    }

}