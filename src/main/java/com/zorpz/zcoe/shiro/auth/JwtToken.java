package com.zorpz.zcoe.shiro.auth;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author Punk
 * @date 2019/04/14
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}