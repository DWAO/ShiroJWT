package com.zorpz.zcoe.shiro.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zorpz.zcoe.shiro.config.RedisConfig;
import com.zorpz.zcoe.shiro.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * jwt 校验器
 * @author Punk
 * @date 2019/04/09
 */
public class JwtUtil {

    @Autowired
    RedisConfig redisConfig;

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        //根据密码生成JWT效验器
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withClaim("username", username)
                .build();
        //效验TOKEN
        verifier.verify(token);
        return true;
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return
     */
    public static String getClaimAsString(String token, String claim) {
        if (!StringUtils.isEmpty(token)) {
            try {
                DecodedJWT jwt = JWT.decode(token);
                return jwt.getClaim(claim).asString();
            } catch (JWTDecodeException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 获取claim
     * @param token
     * @param claim
     * @return
     */
    public static Long getClaimAsLong(String token, String claim) {
        if (!StringUtils.isEmpty(token)) {
            try {
                DecodedJWT jwt = JWT.decode(token);
                return jwt.getClaim(claim).asLong();
            } catch (JWTDecodeException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 生成签名,5min后过期
     *
     * @param username 用户名
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String  sign(String username, String secret, Long now) {
        if (now == null) {
            now = System.currentTimeMillis();
        }

        Algorithm algorithm = Algorithm.HMAC256(secret);

        RedisConfig redisConfig = (RedisConfig) SpringContextUtil.getBean("redisConfig");
        long expireTime = redisConfig.expireTime;
        Date date = new Date(now + expireTime);

        // 附带username信息
        return JWT.create()
                .withClaim("username", username)
                .withClaim("current", now)
                .withExpiresAt(date)
                .sign(algorithm);

    }


}
