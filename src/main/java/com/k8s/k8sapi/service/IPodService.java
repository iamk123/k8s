package com.k8s.k8sapi.service;

import com.k8s.k8sapi.model.dto.V1PatchDTO;
import com.k8s.k8sapi.model.dto.V1PodDTO;
import com.k8s.k8sapi.model.pod.Pod;
import com.k8s.k8sapi.model.pod.PodMetadata;
import com.k8s.k8sapi.model.pod.PodSpec;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodSpec;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: 12492
 * @date: 2021/4/9 19:34
 */
public interface IPodService {

    /**
     * 创建一个V1Pod对象
     * @param v1PodDTO v
     * @return
     */
    V1Pod creatV1Pod(V1PodDTO v1PodDTO);

    /**
     * 创建一个V1Patch对象
     * @param v1PatchDTO
     * @return
     */
    V1Patch createV1Patch(V1PatchDTO v1PatchDTO);

    /**
     * 提取Pod的metadata中需要的信息
     * @param meta
     * @return
     */
    PodMetadata getMetadata(V1ObjectMeta meta);

    /**
     * 提取Pod的spec
     * @param podSpec
     * @return
     */
    PodSpec getPodSpec(V1PodSpec podSpec);

    List<Pod> listNamespacedPod(String namespace, String labelSelector);
}
