package com.k8s.k8sapi.model.pod;

import lombok.Data;

/**
 * @author: 12492
 * @date: 2021/5/9 17:23
 */
@Data
public class PodStatus {
    /**
     * 所在node节点的ip
     */
    private String hostIp;
    /**
     * pod的ip
     */
    private String podIp;
    /**
     * 启动时间
     */
    private String startTime;


}
