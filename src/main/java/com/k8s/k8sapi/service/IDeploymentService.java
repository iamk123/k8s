package com.k8s.k8sapi.service;

import com.alibaba.fastjson.JSONObject;
import com.k8s.k8sapi.model.deployment.Deployment;
import com.k8s.k8sapi.model.dto.DeploymentDTO;
import com.k8s.k8sapi.model.vo.DeploymentVO;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Deployment;

import java.util.List;

/**
 * @author: 12492
 * @date: 2021/4/12 11:34
 */
public interface IDeploymentService {
    V1Deployment createV1Deployment(DeploymentDTO deploymentDTO);

    Deployment readNamespacedDeployment(V1Deployment deployment) throws ApiException;

    /**
     * 获取所有namespace下所有deployment的简要信息
     * @param key
     * @param labelSelector
     * @return
     * @throws ApiException
     */
    List<DeploymentVO> listDeploymentForAllNamespacesBrief(String key, String labelSelector) throws ApiException;

    /**
     * 获取Deployment的所有pod的ip
     * @param v1Deployment
     * @return
     * @throws ApiException
     */
    List<String> getDeploymentPodIpList(V1Deployment v1Deployment) throws ApiException;

}
