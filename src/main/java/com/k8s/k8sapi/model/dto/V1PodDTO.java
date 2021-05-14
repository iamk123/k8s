package com.k8s.k8sapi.model.dto;

import lombok.Data;

/**
 * @author: 12492
 * @date: 2021/4/9 19:48
 */
@Data
public class V1PodDTO {

    private String apiVersion;

    private String kind;

    private Metadata metadata;

    private Spec spec;
}
