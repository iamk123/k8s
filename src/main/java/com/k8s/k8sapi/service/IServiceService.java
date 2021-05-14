package com.k8s.k8sapi.service;

import com.k8s.k8sapi.model.dto.ServiceDTO;
import com.k8s.k8sapi.model.vo.ServiceVO;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1Status;

import java.util.List;

/**
 * @author: 12492
 * @date: 2021/4/10 13:05
 */
public interface IServiceService {

    /**
     * 创建一个V1Service
     * @param serviceDTO
     * @return
     */
    V1Service createV1Service(ServiceDTO serviceDTO);

    List<ServiceVO> listServiceForAllNamespaces(String key) throws ApiException;

    V1Service createNamespacedService(String namespace, ServiceDTO serviceDTO) throws ApiException;

    V1Status deleteNamespacedService(String name, String namespace) throws ApiException;
}
