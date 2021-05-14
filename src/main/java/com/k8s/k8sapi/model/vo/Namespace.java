package com.k8s.k8sapi.model.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: 12492
 * @date: 2021/4/18 12:48
 */
@Data
public class Namespace {
    /**
     * 名称
     */
    String name;
    /**
     * 创建时间
     */
    String createTime;
    /**
     * 标签
     */
    List<String> labels;
    /**
     * 状态
     */
    String status;
}
