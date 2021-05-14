package com.k8s.k8sapi.model.deployment;

import lombok.Data;

import java.util.Map;

/**
 * @author: 12492
 * @date: 2021/4/29 19:48
 */
@Data
public class Metadata {
    /**
     * 注解
     */
    private Map<String, String> annotations;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 标签
     */
    private Map<String, String> labels;
    /**
     * 名称
     */
    private String name;
    /**
     * 命名空间
     */
    private String namespace;
    /**
     * uid
     */
    private String uId;
}
