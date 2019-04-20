package com.zorpz.zcoe.shiro.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 返回结果集
 * @author Punk
 * @date 2019/04/10
 */
@Data
public class Result<T> implements Serializable {

    private int code;

    private String msg;

    private T data;
}
