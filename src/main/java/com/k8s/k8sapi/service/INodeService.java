package com.k8s.k8sapi.service;

import com.k8s.k8sapi.model.vo.Node;
import io.kubernetes.client.openapi.ApiException;

import java.util.List;

/**
 * @author: 12492
 * @date: 2021/4/18 15:20
 */
public interface INodeService {
    List<Node> listNode(String key) throws ApiException;
}
