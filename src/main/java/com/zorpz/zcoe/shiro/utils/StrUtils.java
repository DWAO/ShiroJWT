package com.zorpz.zcoe.shiro.utils;

/**
 * 字符串工具类
 * @author Punk
 * @date 2019/04/12
 */
public class StrUtils {

    public static Boolean isEmpty(String str) {
        return str==null|| str.trim().length()==0;
    }

    public static Long str2Long(String str) {
        return isEmpty(str)? null: Long.parseLong(str);
    }

    public static String obj2String(long l) {
        return String.valueOf(l);
    }
}
