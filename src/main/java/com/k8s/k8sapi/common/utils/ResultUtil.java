package com.k8s.k8sapi.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;


public class ResultUtil  {
    /**
     * 结果标记
     */
    private Boolean flag;
    /**
     * 消息状态码
     */
    private String status;

    /**
     * 消息
     */
    private String msg;
    /**
     * 返回数据
     */
    private Object data;

    public ResultUtil() {
    }

    public ResultUtil(boolean flag, Object data) {
        super();
        this.flag = flag;
        this.data = data;
    }

    public ResultUtil(boolean flag, String status, String msg) {
        super();
        this.flag = flag;
        this.status = status;
        this.msg = msg;
    }

    public ResultUtil(Boolean flag, String status, String msg, Object data) {
        this.flag = flag;
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 响应成功
     *
     * @param data 返回数据
     * @return ResultUtil
     */
    public static ResultUtil success(Object data) {
        return new ResultUtil(true, "200", "响应成功", data);
    }

    public static ResultUtil success(Object data, String msg) {
        return new ResultUtil(true, "200", msg, data);
    }

    public static ResultUtil success() {
        return new ResultUtil(true, "200", "响应成功", null);
    }

    /**
     * 响应失败
     *
     * @param status 状态码
     * @param msg    提示消息
     * @return
     */
    public static ResultUtil error(String status, String msg) {
        return new ResultUtil(false, status, msg, null);
    }
}
