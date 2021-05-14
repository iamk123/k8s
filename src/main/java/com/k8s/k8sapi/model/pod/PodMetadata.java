package com.k8s.k8sapi.model.pod;

import lombok.Data;

import java.util.Map;

/**
 * @author: 12492
 * @date: 2021/4/29 20:11
 */
@Data
public class PodMetadata {
    /**
     * 名称
     */
    private String name;
    /**
     * 命名空间
     */
    private String namespace;
    /**
     * 标签
     */
    private Map<String, String> labels;
    /**
     *        ownerReferences: [class V1OwnerReference {
     *             apiVersion: apps/v1
     *             blockOwnerDeletion: true
     *             controller: true
     *             kind: ReplicaSet
     *             name: weteam-6fc99b4bd7
     *             uid: bb1d8af0-2ec7-49f1-8b9f-0b0502d6d507
     *         }]
     */
    private Object ownerReferences;
    /**
     * uId
     */
    private String uId;
}
