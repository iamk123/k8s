package com.k8s.k8sapi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.k8s.k8sapi.common.config.K8sInit;
import com.k8s.k8sapi.common.utils.DateUtil;
import com.k8s.k8sapi.model.deployment.Deployment;
import com.k8s.k8sapi.model.deployment.Metadata;
import com.k8s.k8sapi.model.deployment.Spec;
import com.k8s.k8sapi.model.deployment.Status;
import com.k8s.k8sapi.model.dto.DeploymentDTO;
import com.k8s.k8sapi.model.pod.PodMetadata;
import com.k8s.k8sapi.model.pod.PodSpec;
import com.k8s.k8sapi.model.vo.DeploymentVO;
import com.k8s.k8sapi.service.IDeploymentService;
import com.k8s.k8sapi.service.IPodService;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author: 12492
 * @date: 2021/4/12 11:36
 */
@Service
public class DeploymentServiceImpl implements IDeploymentService {
    @Resource
    private K8sInit k8sInit;

    @Resource
    private IPodService podService;

    @Override
    public V1Deployment createV1Deployment(DeploymentDTO deploymentDTO) {
        V1Deployment body = new V1Deployment();
        // apiVersion
        body.setApiVersion("apps/v1");
        // deployment
        body.setKind("Deployment");
        //metadata
        body.setMetadata(new V1ObjectMeta()
                .name(deploymentDTO.getDeploymentName())
                .namespace(deploymentDTO.getNamespace())
                .labels(deploymentDTO.getLabels()));
        // spec
        // ports
        List<V1ContainerPort> portList = new ArrayList<>();
        V1ContainerPort port = new V1ContainerPort()
                // .name(deploymentDTO.getDeploymentName()) 不能和deployment的名字一样，会报错
                .containerPort(deploymentDTO.getContainerPort());
        portList.add(port);
        // containers
        List<V1Container> containerList = new ArrayList<>();
        V1Container container = new V1Container()
                .name(deploymentDTO.getDeploymentName())
                .image(deploymentDTO.getImage())
                .imagePullPolicy("IfNotPresent")
                .ports(portList);
        containerList.add(container);

        Map<String, String> labels = deploymentDTO.getLabels();
        labels.put(deploymentDTO.getDeploymentName(), "deploymentName");
        body.setSpec(new V1DeploymentSpec()
                .replicas(deploymentDTO.getReplicas())
                .selector(new V1LabelSelector().matchLabels(labels))
                .template(new V1PodTemplateSpec()
                        .metadata(new V1ObjectMeta()
                                .labels(deploymentDTO.getLabels()))
                        .spec(new V1PodSpec().containers(containerList))));
        return body;
    }

    @Override
    public Deployment readNamespacedDeployment(V1Deployment deployment) throws ApiException {
        if(deployment == null) {
            return null;
        }
        Deployment d = new Deployment();
        d.setApiVersion(deployment.getApiVersion());
        d.setKind(deployment.getKind());
        // metadata
        Metadata dMetadata = new Metadata();
        V1ObjectMeta metadata = deployment.getMetadata();
        if(metadata != null) {
            // createTime
            DateTime creationTimestamp = metadata.getCreationTimestamp();
            if(creationTimestamp != null) {
                Date date = creationTimestamp.toDate();
                String createTime = DateUtil.format(date);
                dMetadata.setCreateTime(createTime);
            }
            // labels
            Map<String, String> labels = metadata.getLabels();
            dMetadata.setLabels(labels);
            // name
            dMetadata.setName(metadata.getName());
            // namespace
            dMetadata.setNamespace(metadata.getNamespace());
            // uId
            dMetadata.setUId(metadata.getUid());

            d.setMetadata(dMetadata);
        }
        // spec
        Spec dSpec = new Spec();
        V1DeploymentSpec spec = deployment.getSpec();
        if(spec != null) {
            // replicas
            dSpec.setReplicas(spec.getReplicas());
            // selector
            dSpec.setSelector(spec.getSelector().getMatchLabels());
            // strategy   maxSurge是intOrString类型，直接转integer会保证错
            // dSpec.setStrategy(spec.getStrategy());
            // template
            PodMetadata podMetadata = podService.getMetadata(spec.getTemplate().getMetadata());
            PodSpec podSpec = podService.getPodSpec(spec.getTemplate().getSpec());
            dSpec.setPodMetadata(podMetadata);
            dSpec.setPodSpec(podSpec);

            d.setSpec(dSpec);
        }
        // status
        Status s = new Status();
        V1DeploymentStatus status = deployment.getStatus();
        if(status != null) {
            s.setAvailableReplicas(status.getAvailableReplicas());
            s.setReadyReplicas(status.getReadyReplicas());
            s.setReplicas(status.getReplicas());
            s.setConditions(status.getConditions());

            d.setStatus(s);
        }
        return d;
    }

    @Override
    public List<DeploymentVO> listDeploymentForAllNamespacesBrief(String key, String labelSelector) throws ApiException {
        List<DeploymentVO> res = new ArrayList<>();
        AppsV1Api apiInstance = new AppsV1Api(k8sInit.getConnection());
        V1DeploymentList deploymentList = apiInstance.listDeploymentForAllNamespaces(null, null, null,
                labelSelector, null, null, null, null, null);
        DeploymentVO deploymentVO;
        for(V1Deployment item : deploymentList.getItems()) {
            deploymentVO = new DeploymentVO();
            // name
            String name = item.getMetadata().getName();
            if(name != null && !name.contains(key)) {
                continue;
            }
            deploymentVO.setName(name);
            // namespace
            String namespace = item.getMetadata().getNamespace();
            deploymentVO.setNamespace(namespace);
            // createTime
            Date date = item.getMetadata().getCreationTimestamp().toDate();
            String createTime = DateUtil.format(date);
            deploymentVO.setCreateTime(createTime);
            // labels
            Map<String, String> labels = item.getMetadata().getLabels();
            deploymentVO.setLabels(labels);
            // replicas
            Integer replicas = item.getSpec().getReplicas();
            deploymentVO.setReplicas(replicas);
            // image
            V1PodSpec spec = item.getSpec().getTemplate().getSpec();
            if(spec != null) {
                String image = spec.getContainers().get(0).getImage();
                deploymentVO.setImage(image);
            }
            res.add(deploymentVO);
        }
        return res;
    }

    @Override
    public List<String> getDeploymentPodIpList(V1Deployment v1Deployment) throws ApiException {
        List<String> podIpList = new ArrayList<>();
        V1ObjectMeta metadata = v1Deployment.getMetadata();
        if(metadata == null) {
            return podIpList;
        }
        String name = metadata.getName();
        CoreV1Api coreApiInstance = new CoreV1Api(k8sInit.getConnection());
        V1PodList v1PodList = coreApiInstance.listNamespacedPod(metadata.getNamespace(), null, true,
                null, null, name, null, null,
                null, null);
        for(V1Pod pod : v1PodList.getItems()) {
            V1PodStatus status = pod.getStatus();
            if(status != null) {
                String podIp = status.getPodIP();
                podIpList.add(podIp);
            }
        }
        return podIpList;
    }


}
