package com.k8s.k8sapi.model.deployment;

import lombok.Data;

/**
 * @author: 12492
 * @date: 2021/4/29 19:46
 */
@Data
public class Status {
    /**
     * 可用副本数
     */
    private Integer availableReplicas;
    /**
     *         conditions: [class V1DeploymentCondition {
     *             lastTransitionTime: 2021-04-29T13:58:25.000+08:00
     *             lastUpdateTime: 2021-04-29T13:58:25.000+08:00
     *             message: Deployment has minimum availability.
     *             reason: MinimumReplicasAvailable
     *             status: True
     *             type: Available
     *         }, class V1DeploymentCondition {
     *             lastTransitionTime: 2021-04-29T13:58:23.000+08:00
     *             lastUpdateTime: 2021-04-29T13:58:25.000+08:00
     *             message: ReplicaSet "test-deployment-68d5b5b9b" has successfully progressed.
     *             reason: NewReplicaSetAvailable
     *             status: True
     *             type: Progressing
     *         }]
     */
    private Object conditions;
    /**
     * 就绪副本数
     */
    private Integer readyReplicas;
    /**
     * 副本数
     */
    private Integer replicas;
}
