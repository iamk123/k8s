package com.k8s.k8sapi.service.impl;

import com.k8s.k8sapi.common.config.K8sInit;
import com.k8s.k8sapi.model.dto.ServiceDTO;
import com.k8s.k8sapi.model.vo.ServiceVO;
import com.k8s.k8sapi.service.IServiceService;
import io.kubernetes.client.custom.IntOrString;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 12492
 * @date: 2021/4/10 13:06
 */
@Service
public class ServiceServiceImpl implements IServiceService {

    @Resource
    private K8sInit k8sInit;

    @Override
    public V1Service createV1Service(ServiceDTO serviceDTO) {

        V1Service v1Service = new V1Service();
        v1Service.setApiVersion("v1");
        v1Service.setKind("Service");
        // metadata
        Map<String, String> labels = serviceDTO.getLabels();
        // labels.put("app", serviceDTO.getMetadataLabelsApp());
        v1Service.setMetadata(new V1ObjectMeta()
                .name(serviceDTO.getMetadataName())
                .labels(labels));
        // spec
        Map<String, String> selector = serviceDTO.getLabels();
        List<V1ServicePort> v1ServicePorts = new ArrayList<>();
        v1ServicePorts.add(new V1ServicePort()
                // name(serviceDTO.getSpecPortsName()).
                .protocol(serviceDTO.getSpecPortsProtocol())
                .port(serviceDTO.getSpecPortsPort())
                .nodePort(serviceDTO.getSpecPortsNodePort())
                .targetPort(new IntOrString(serviceDTO.getSpecPortsTargetPort())));
        v1Service.setSpec(new V1ServiceSpec()
                .selector(selector)
                .ports(v1ServicePorts)
                .type(serviceDTO.getSpecType()));
        // System.out.println(v1Service);
        return v1Service;
    }

    @Override
    public List<ServiceVO> listServiceForAllNamespaces(String key) throws ApiException {
        List<ServiceVO> res = new ArrayList<>();
        CoreV1Api apiInstance = new CoreV1Api(k8sInit.getConnection());
        V1ServiceList result = apiInstance.listServiceForAllNamespaces(null, null, null, null, null, "true", null,
                null, null);
        for(V1Service item : result.getItems()) {
            ServiceVO service = new ServiceVO();
            // name
            String name = item.getMetadata().getName();
            if(name != null && !name.contains(key)) {
                continue;
            }
            service.setName(name);
            // namespace
            String namespace = item.getMetadata().getNamespace();
            service.setNamespace(namespace);
            // labels
            Map<String, String> labels = item.getMetadata().getLabels();
            service.setLabels(labels);
            // address
            String clusterIp = item.getSpec().getClusterIP();
            service.setClusterIp(clusterIp);
            res.add(service);
            item.getSpec().getSelector();
        }
        return res;
    }

    @Override
    public V1Service createNamespacedService(String namespace, ServiceDTO serviceDTO) throws ApiException {
        CoreV1Api apiInstance = new CoreV1Api(k8sInit.getConnection());
        V1Service body = createV1Service(serviceDTO);
        return apiInstance.createNamespacedService(namespace, body, "true", null, null);
    }

    @Override
    public V1Status deleteNamespacedService(String name, String namespace) throws ApiException {
        CoreV1Api apiInstance = new CoreV1Api(k8sInit.getConnection());
        V1DeleteOptions body = new V1DeleteOptions();
        V1Status result = apiInstance.deleteNamespacedService(name, namespace, "true", null,
                    null, true, null, body);
        return result;
    }
}
