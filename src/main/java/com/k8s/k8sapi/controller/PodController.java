package com.k8s.k8sapi.controller;

import com.k8s.k8sapi.common.config.K8sInit;
import com.k8s.k8sapi.common.utils.ResultUtil;
import com.k8s.k8sapi.model.dto.V1PodDTO;
import com.k8s.k8sapi.model.pod.Pod;
import com.k8s.k8sapi.service.IPodService;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/pod")
public class PodController {

    @Resource
    private K8sInit k8sInit;

    @Resource
    IPodService podService;

    /**
     * 列出namespace下的所有pod
     * @param namespace
     * @return
     */
    @GetMapping("/listNamespacedPod/{namespace}")
    public ArrayList<String> listNamespacedPod(@PathVariable String namespace, @RequestParam String labelSelector) {
        // List<Pod> podList = podService.listNamespacedPod(namespace, labelSelector);

        ArrayList<String> res = new ArrayList<>();
        CoreV1Api apiInstance = new CoreV1Api(k8sInit.getConnection());
        String pretty = "true";
        V1PodList result = new V1PodList();
        try {
            result = apiInstance.listNamespacedPod(namespace, pretty, true,
                    null, null, null, null, null,
                    null, null);
            for(V1Pod pod : result.getItems()) {
                String name = pod.getMetadata().getName();
                if(name != null) {
                    res.add(name);
                }
            }
        } catch (ApiException e) {
            System.err.println("---Exception when calling CoreV1Api#listNamespacedPod");
            System.err.println("---Status code: " + e.getCode());
            System.err.println("---Reason: " + e.getResponseBody());
            System.err.println("---Response headers: " + e.getResponseHeaders());
            e.printStackTrace();

        }

        return res;
    }

    /**
     * 在指定的namespace下创建pod
     * {
     * 	"apiVersion":"v1",
     * 	"kind":"Pod",
     * 	"metadata":{
     *         "name":"memory-demo"
     *     },
     *     "spec": {
     *         "containers": {
     *             "name": "memory-demo-ctr",
     *             "image": "polinux/stress",
     *             "command": ["/bin/bash", "-ce", "tail -f /dev/null"],
     *             "imagePullPolicy": "IfNotPresent"
     *         }
     *     }
     * }
     * @param namespace 指定namespace
     * @param v1PodDTO
     * @return
     */
    @PostMapping("/createNamespacePod/{namespace}")
    public ResultUtil createNamespacePod(@PathVariable String namespace,
                                         @RequestBody V1PodDTO v1PodDTO) {
        CoreV1Api apiInstance = new CoreV1Api(k8sInit.getConnection());
        // System.out.println(v1PodDTO);
        V1Pod body = podService.creatV1Pod(v1PodDTO);
        V1Pod result;
        try {
            result = apiInstance.createNamespacedPod(namespace, body, "true", null, null);
            System.out.println(result);
        } catch (ApiException e) {
            return ResultUtil.error(String.valueOf(e.getCode()), e.getMessage());
        }
        return ResultUtil.success(result);
    }

    /**
     * 根据name、namespace删除pod TODO:能删除，会报错 Expected a string but was BEGIN_OBJECT
     * @param name
     * @param namespace
     * @return
     */
    @GetMapping("/deleteNamespacePod")
    public ResultUtil deleteNamespacePod(@RequestParam String name, @RequestParam String namespace) {
        CoreV1Api apiInstance = new CoreV1Api(k8sInit.getConnection());
        V1DeleteOptions body = new V1DeleteOptions();
        V1Status result;
        try {
            apiInstance.deleteNamespacedPod(name, namespace, "true", null, null,
                    null, null, null);
        } catch (ApiException e) {
            return ResultUtil.error(String.valueOf(e.getCode()), e.getMessage());
        }
        return ResultUtil.success();
    }

    /**
     * 查询指定pod
     * @param name pod的名字
     * @param namespace 命名空间
     * @return
     */
    @GetMapping("/readNamespacedPod/{name}/{namespace}")
    public ResultUtil readNamespacedPod(@PathVariable String name,
                                        @PathVariable String namespace) {
        System.out.println("readNamespacedPod");
        CoreV1Api apiInstance = new CoreV1Api(k8sInit.getConnection());
        V1Pod result;
        try {
            result = apiInstance.readNamespacedPod(name, namespace, null, null, null);
        } catch (ApiException e) {
            return ResultUtil.error(String.valueOf(e.getCode()), e.getMessage());
        }
        System.out.println(result);
        return ResultUtil.success(result);
    }


    @GetMapping("/patchNamespacedPod/{name}/{namespace}")
    public ResultUtil patchNamespacedPod(@PathVariable(required = true) String name,
                                         @PathVariable(required = true) String namespace) {
                                         // @RequestBody V1PatchDTO v1PatchDTO) {
        CoreV1Api apiInstance = new CoreV1Api(k8sInit.getConnection());
        // V1Patch body = podService.createV1Patch(v1PatchDTO);
        String str = "[{\"op\": \"replace\", \"path\": \"/metadata/labels/test\", \"value\":\"false\"}]";
        V1Patch body = new V1Patch(str);
        V1Pod result;
        try {
            result = apiInstance.patchNamespacedPod(name, namespace, body, "true", null, null, true);

        } catch (ApiException e) {
            e.printStackTrace();
            return ResultUtil.error(String.valueOf(e.getCode()), e.getMessage());
        }
        return ResultUtil.success(result);
    }

    @GetMapping("/replaceNode/{name}")
    public ResultUtil replaceNode(@PathVariable String name){
        CoreV1Api apiInstance = new CoreV1Api(k8sInit.getConnection());
        V1Node result;
        Map<String, String> labels = new HashMap<>();
        labels.put("name", "memory-demo-label");
        V1Node body = new V1Node().
                kind("Pod").apiVersion("v1").metadata(
                        new V1ObjectMeta().name("memory-demo").labels(labels));
        try {
            result = apiInstance.replaceNode(name, body, "true", null, null);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#replaceNode");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
            return ResultUtil.error(String.valueOf(e.getCode()), e.getMessage());
        }
        return ResultUtil.success(result);
    }


}
