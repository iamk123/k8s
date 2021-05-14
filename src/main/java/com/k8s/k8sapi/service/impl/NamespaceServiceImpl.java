package com.k8s.k8sapi.service.impl;

import com.k8s.k8sapi.common.config.K8sInit;
import com.k8s.k8sapi.common.utils.DateUtil;

import com.k8s.k8sapi.model.vo.Namespace;
import com.k8s.k8sapi.service.INamespaceService;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.openapi.models.V1NamespaceBuilder;
import io.kubernetes.client.openapi.models.V1NamespaceList;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: 12492
 * @date: 2021/4/18 11:26
 */
@Service
public class NamespaceServiceImpl implements INamespaceService {

    @Resource
    private K8sInit k8sInit;

    @Override
    public List<Namespace> listNamespace(String key) throws ApiException {
        List<Namespace> res = new ArrayList<>();
        CoreV1Api apiInstance = new CoreV1Api(k8sInit.getConnection());
        String pretty = "true";
        V1NamespaceList namespaceList = apiInstance.listNamespace(pretty, true, null,
                    null, null, null, null, null, null);

        Namespace namespace;
        for (V1Namespace item : namespaceList.getItems()) {
            namespace = new Namespace();
            // name
            String name = item.getMetadata().getName();
            if(name != null && !name.contains(key)) {
                continue;
            }
            namespace.setName(name);
            // createTime
            Date date = item.getMetadata().getCreationTimestamp().toDate();
            String createTime = DateUtil.format(date);
            namespace.setCreateTime(createTime);
            // labels
            Map<String, String> labels = item.getMetadata().getLabels();
            List<String> labelsStr = new ArrayList<>();
            if(labels != null) {
                for(Map.Entry<String, String> entry : labels.entrySet()) {
                    labelsStr.add("[" + entry.getKey() + "]" + " : " + entry.getValue());
                }
            } else {
                labelsStr.add("-");
            }
            namespace.setLabels(labelsStr);
            // status
            String status = item.getStatus().getPhase();
            namespace.setStatus(status);

            res.add(namespace);
        }
        return res;
    }

    @Override
    public V1Namespace createNamespace(String namespace) throws ApiException {
        CoreV1Api apiInstance = new CoreV1Api(k8sInit.getConnection());
        V1Namespace v1Namespace = new V1NamespaceBuilder()
                .withNewMetadata()
                .withName(namespace)
                .endMetadata()
                .build();
        V1Namespace ns = apiInstance.createNamespace(v1Namespace, null, null, null);
        return ns;
    }
}
