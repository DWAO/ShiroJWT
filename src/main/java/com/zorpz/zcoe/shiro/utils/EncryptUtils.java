package com.zorpz.zcoe.shiro.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 加密工具类
 * @author Punk
 * @date 2019/04/10
 */
public class EncryptUtils {

    /**
     * 校验输入密码是否正确
     * @param encryptPassword
     * @param salt
     * @param password
     * @return
     */
    public static Boolean verfifyPassword(String encryptPassword, String salt, String password) {
        String encryptStr = encryptPassword(password, salt);
        return encryptPassword.equals(encryptStr);
    }

    /**
     * 获得加密后的密码
     * @param salt
     * @param password
     * @return
     */
    public static String encryptPassword(String password, String salt) {
        return DigestUtils.md5Hex(password+salt);
    }





}
