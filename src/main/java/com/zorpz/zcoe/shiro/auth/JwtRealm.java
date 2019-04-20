package com.zorpz.zcoe.shiro.auth;

import com.zorpz.zcoe.shiro.entity.User;
import com.zorpz.zcoe.shiro.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Punk
 * @date 2019/04/14
 */
@Service
public class JwtRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String username = JwtUtil.getClaimAsString(token, "username");
        if (username == null) {
            throw new AuthenticationException("token无效");
        }

        User userBean = userService.findByUsername(username);
        if (userBean == null) {
            throw new AuthenticationException("用户不存在!");
        }
        String password = userBean.getPassword();

        JwtUtil.verify(token, username, password);

        return new SimpleAuthenticationInfo(token, token, "my_realm");
    }
}
