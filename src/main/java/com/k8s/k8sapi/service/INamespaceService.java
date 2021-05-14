package com.k8s.k8sapi.service;

import com.alibaba.fastjson.JSONObject;
import com.k8s.k8sapi.model.vo.Namespace;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.openapi.models.V1NamespaceList;

import java.util.List;

/**
 * @author: 12492
 * @date: 2021/4/18 11:25
 */
public interface INamespaceService {


    List<Namespace> listNamespace(String key) throws ApiException;

    V1Namespace createNamespace(String namespace) throws ApiException;
}
