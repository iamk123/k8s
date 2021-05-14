package com.k8s.k8sapi.common.exception;

/**
 * @author: 12492
 * @date: 2021/4/18 11:09
 * 基础接口类，自定义的错误描述枚举类需实现该接口
 */
public interface BaseErrorInfoInterface {
    /** 错误码*/
    String getResultCode();

    /** 错误描述*/
    String getResultMsg();
}
