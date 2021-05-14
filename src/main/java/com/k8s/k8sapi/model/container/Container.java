package com.k8s.k8sapi.model.container;

import lombok.Data;

import java.util.List;

/**
 * @author: 12492
 * @date: 2021/4/30 12:41
 */
@Data
public class Container {

    private String command;
    private String env;
    /**
     * 镜像拉取策略
     */
    private String imagePullPolicy;
    /**
     * 镜像
     */
    private String image;
    /**
     * 名称
     */
    private String name;
    /**
     * 端口列表
     */
    List<Object> ports;
}
