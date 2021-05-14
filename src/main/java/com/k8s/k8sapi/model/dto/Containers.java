package com.k8s.k8sapi.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author: 12492
 * @date: 2021/4/9 20:24
 */
@Data
public class Containers {
    private String name;
    private String image;
    private List<String> command;
    private List<String> args;
    private String imagePullPolicy;
}
