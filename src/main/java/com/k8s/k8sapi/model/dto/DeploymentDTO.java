package com.k8s.k8sapi.model.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author: 12492
 * @date: 2021/4/11 15:06
 */
@Data
public class DeploymentDTO {
    private String namespace;
    private String deploymentName;
    private Integer replicas;
    // private String metadataLabelsApp;
    private Map<String, String> labels;
    private String image;
    private String portName;
    private Integer containerPort;

}
