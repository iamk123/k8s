package com.k8s.k8sapi.model.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author: 12492
 * @date: 2021/4/18 15:22
 */
@Data
public class Node {

    /**
     * 名称
     */
    private String name;
    /**
     * 标签
     */
    private List<String> labels;
    /**
     * ip地址
     */
    private String address;
    /**
     * Ready 或 UnReady
     */
    private String conditionType;
    /**
     * 如果节点是健康的且已经就绪可以接受新的 Pod。
     * 则节点Ready字段为 True。False表明了该节点不健康，不能够接受新的 Pod。
     */
    private String conditionStatus;

}
