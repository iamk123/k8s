package com.k8s.k8sapi.service.impl;

import com.k8s.k8sapi.model.dto.V1PatchDTO;
import com.k8s.k8sapi.model.dto.V1PodDTO;
import com.k8s.k8sapi.model.pod.Pod;
import com.k8s.k8sapi.model.pod.PodMetadata;
import com.k8s.k8sapi.model.pod.PodSpec;
import com.k8s.k8sapi.service.IPodService;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodSpec;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: 12492
 * @date: 2021/4/9 19:34
 */
@Service
public class PodServiceImpl implements IPodService {
    @Override
    public V1Pod creatV1Pod(V1PodDTO v1PodDTO) {
        V1Pod v1Pod = new V1Pod();
        // apiVersion
        v1Pod.setApiVersion(v1PodDTO.getApiVersion());
        // kind
        v1Pod.setKind(v1PodDTO.getKind());
        // metadata
        V1ObjectMeta metadata = new V1ObjectMeta().
                name(v1PodDTO.getMetadata().getName()).
                namespace(v1PodDTO.getMetadata().getNamespace());
        v1Pod.setMetadata(metadata);
        // spec
        V1PodSpec v1PodSpec = new V1PodSpec();
        List<V1Container> list = new ArrayList<>();
        V1Container v1Container = new V1Container().
                name(v1PodDTO.getSpec().getContainers().getName()).
                image(v1PodDTO.getSpec().getContainers().getImage()).
                command(v1PodDTO.getSpec().getContainers().getCommand()).
                imagePullPolicy(v1PodDTO.getSpec().getContainers().getImagePullPolicy());
        list.add(v1Container);
        v1PodSpec.setContainers(list);

        v1Pod.setSpec(v1PodSpec);
        return v1Pod;
    }

    @Override
    public V1Patch createV1Patch(V1PatchDTO v1PatchDTO) {
        return null;
    }

    @Override
    public PodMetadata getMetadata(V1ObjectMeta meta) {
        if(meta == null) {
            return null;
        }
        PodMetadata podMetadata = new PodMetadata();
        // labels
        podMetadata.setLabels(meta.getLabels());
        // name
        podMetadata.setName(meta.getName());
        // namespace
        podMetadata.setNamespace(meta.getNamespace());
        // ownerReferences
        podMetadata.setOwnerReferences(meta.getOwnerReferences());
        // uId
        podMetadata.setUId(meta.getUid());

        return podMetadata;
    }

    @Override
    public PodSpec getPodSpec(V1PodSpec podSpec) {
        if(podSpec == null) {
            return null;
        }
        PodSpec spec = new PodSpec();
        spec.setContainer(podSpec.getContainers());
        spec.setNodeName(podSpec.getNodeName());
        spec.setRestartPolicy(podSpec.getRestartPolicy());
        spec.setSchedulerName(podSpec.getSchedulerName());

        return spec;
    }

    @Override
    public List<Pod> listNamespacedPod(String namespace, String labelSelector) {
        return null;
    }
}
