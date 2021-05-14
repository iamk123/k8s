package com.k8s.k8sapi.model.deployment;

import com.k8s.k8sapi.model.pod.PodMetadata;
import com.k8s.k8sapi.model.pod.PodSpec;
import lombok.Data;

import java.util.Map;

/**
 * @author: 12492
 * @date: 2021/4/29 19:46
 */
@Data
public class Spec {
    /**
     * 副本数
     */
    private Integer replicas;
    /**
     * 选择器
     */
    private Map<String, String> selector;

    /**
     *  strategy: class V1DeploymentStrategy {
     *             rollingUpdate: class V1RollingUpdateDeployment {
     *                 maxSurge: 25%
     *                 maxUnavailable: 25%
     *             }
     *             type: RollingUpdate
     *         }
     */
    private Object strategy;
    /**
     * pod的metadata
     */
    private PodMetadata podMetadata;
    /**
     * pod的spec
     */
    private PodSpec podSpec;
    /**
     *
     */
    private Status status;

}
