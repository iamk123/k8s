package com.k8s.k8sapi.model.pod;

import lombok.Data;

/**
 * @author: 12492
 * @date: 2021/4/29 20:11
 */
@Data
public class PodSpec {
    /**
     * 容器列表
     */
    private Object container;
    /**
     * nodeName
     */
    private String nodeName;
    /**
     * 重启策略
     */
    private String restartPolicy;
    /**
     * schedulerName
     */
    private String schedulerName;
}
