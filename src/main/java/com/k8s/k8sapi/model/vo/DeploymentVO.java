package com.k8s.k8sapi.model.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author: 12492
 * @date: 2021/4/18 18:11
 */
@Data
public class DeploymentVO {

    /**
     * 名称
     */
    private String name;
    /**
     * 命名空间
     */
    private String namespace;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 标签
     */
    private Map<String, String> labels;
    /**
     * 副本数
     */
    private Integer replicas;
    /**
     * 镜像
     */
    private String image;
}
