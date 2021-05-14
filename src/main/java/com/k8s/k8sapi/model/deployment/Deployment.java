package com.k8s.k8sapi.model.deployment;

import lombok.Data;

/**
 * @author: 12492
 * @date: 2021/4/29 19:46
 */
@Data
public class Deployment {
    private String apiVersion;
    private String kind;
    private Metadata metadata;
    private Spec spec;
    private Status status;
}
