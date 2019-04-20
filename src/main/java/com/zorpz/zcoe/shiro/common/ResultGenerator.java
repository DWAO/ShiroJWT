package com.zorpz.zcoe.shiro.common;

/**
 * 响应结果生成器
 * @author Punk
 * @date 2019/04/10
 */
public class ResultGenerator {

    /**
     * 返回数据集
     * @param data
     * @return
     */
    public static Result genSuccessResult(Object data) {
        Result result = new Result();
        result.setCode(Constants.SUCCESS);
        result.setMsg(Constants.DEFAULT_SUCCESS_MESSAGE);
        result.setData(data);
        return result;
    }

    /**
     * 返回无数据集
     * @return
     */
    public static Result genSuccessResult() {
        Result result = new Result();
        result.setCode(Constants.SUCCESS);
        result.setMsg(Constants.DEFAULT_SUCCESS_MESSAGE);
        return result;
    }

    /**
     *  返回操作失败说明
     * @param code
     * @param msg
     * @return
     */
    public static Result genFailureResult(int code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
