package com.k8s.k8sapi.model.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author: 12492
 * @date: 2021/4/19 16:39
 */
@Data
public class ServiceVO {
    /**
     * 名称
     */
    String name;
    /**
     * 命名空间
     */
    private String namespace;
    /**
     * 标签
     */
    Map<String, String> labels;
    /**
     * 集群IP
     */
    private String clusterIp;
    /**
     * 创建时间
     */
    private String createTime;
}
