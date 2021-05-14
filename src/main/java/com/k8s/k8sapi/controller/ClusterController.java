package com.k8s.k8sapi.controller;

import com.k8s.k8sapi.common.config.K8sInit;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/v1/cluster")
public class ClusterController {

    @Resource
    private K8sInit k8sInit;

    public void GetK8sCluster() {
        // CustomObjectsApi apiInstance = new CustomObjectsApi();
        // String group = "flycloud.cn";
        // String version = "v1";
        // String plural = "clusters";
        // String pretty = "ture";
        // try {
        //     Object result = apiInstance.listClusterCustomObject(group, version, plural, pretty,null,
        //             null,null,null);
        //     String listCluster = JSON.toJSONString(result);
        //     System.out.println(listCluster);
        // } catch (ApiException e) {
        //     System.err.println("Exception when calling CustomObjectsApi#listClusterCustomObject");
        //     e.printStackTrace();
        // }
    }
}
