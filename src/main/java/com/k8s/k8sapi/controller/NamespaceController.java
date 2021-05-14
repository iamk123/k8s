package com.k8s.k8sapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.k8s.k8sapi.common.config.K8sInit;
import com.k8s.k8sapi.common.utils.ResultUtil;
import com.k8s.k8sapi.model.vo.Namespace;
import com.k8s.k8sapi.service.INamespaceService;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/namespace")
public class NamespaceController {
    @Resource
    private K8sInit k8sInit;
    @Resource
    private INamespaceService namespaceService;

    /**
     * 列出所有namespace
     * @return
     */
    @GetMapping("/listNamespace")
    public ResultUtil listNamespace(@RequestParam(defaultValue = "") String key) throws ApiException {
        // System.out.println(key);
        List<Namespace> res = namespaceService.listNamespace(key);
        return ResultUtil.success(res);
    }

    /**
     * 创建namespace
     * https://github.com/zq2599/blog_demos/blob/master/kubernetesclient/openapi/src/main/java/com/bolingcavalry/openapi/OpenAPIDemoApplication.java
     * @param namespace
     * @return
     */
    @GetMapping("/createNamespace/{namespace}")
    public ResultUtil createNamespace(@PathVariable String namespace) {
        CoreV1Api apiInstance = new CoreV1Api(k8sInit.getConnection());

        V1Namespace v1Namespace = new V1NamespaceBuilder()
                .withNewMetadata()
                .withName(namespace)
                .endMetadata()
                .build();

        V1Namespace ns;
        try {
            ns = apiInstance.createNamespace(v1Namespace, null, null, null);
        } catch (ApiException e) {
            if(e.getCode() == 409) {
                return ResultUtil.error("409", "Error: Namespace already exists！");
            }
            if(e.getCode() == 201) {
                return ResultUtil.error("201", "Error: Namespace already exists！");
            }
            if(e.getCode() == 401) {
                return ResultUtil.error("401", "Error: No permission!");
            }
            return ResultUtil.error(String.valueOf(e.getCode()), e.getMessage());
        }
        return ResultUtil.success(ns);
    }


    /**
     * 根据名称删除namespace  TODO: 能删除成功，但会提示错误 Expected a string but was BEGIN_OBJECT 官方错误
     * @return
     */
    @GetMapping("/deleteNamespace/{name}")
    public ResultUtil deleteNamespace(@PathVariable String name) {
        System.out.println("删除");
        ApiClient client = k8sInit.getConnection();
        CoreV1Api apiInstance = new CoreV1Api(client);
        String pretty = "true";
        try {
            apiInstance.deleteNamespace(name, pretty, null, null, true,
                    null, null);
        } catch (Exception ignored) {

        }
        return ResultUtil.success();
    }

    /**
     * 根据name查询namespace的信息
     * @param name namespace的名称
     * @return
     */
    @GetMapping("/readNamespace/{name}")
    public ResultUtil readNamespace(@PathVariable String name) {
        CoreV1Api apiInstance = new CoreV1Api(k8sInit.getConnection());
        V1Namespace result;
        try {
            result = apiInstance.readNamespace(name, "true", null, null);
        } catch (ApiException e) {
            return ResultUtil.error(String.valueOf(e.getCode()), e.getMessage());
        }
        return ResultUtil.success(result);
    }







    /**
     * 替换namespace的内容
     * 可以使用，但没有搞懂具体作用
     * @param body
     * @return
     */
    @PostMapping("/replaceNamespace")
    public ResultUtil replaceNamespace(@RequestBody V1Namespace body) {
        CoreV1Api apiInstance = new CoreV1Api(k8sInit.getConnection());
        V1Namespace result;
        // V1NamespaceStatus status = new V1NamespaceStatus();
        // status.setPhase("Terminating");
        // body.setStatus(status);
        try {
            result = apiInstance.replaceNamespace("test-namespace", body, "true", null, null);
            System.out.println(result);
        } catch (ApiException e) {
            return ResultUtil.error(String.valueOf(e.getCode()), e.getMessage());
        }
        return ResultUtil.success(result);
    }


    // 更新namespace TODO： 422 ： Unprocessable Entity
    @PatchMapping("/patchNamespace")
    public ResultUtil patchNamespace(@RequestBody V1Patch body) {
        CoreV1Api apiInstance = new CoreV1Api(k8sInit.getConnection());
        String pretty = "true";
        V1Namespace result;
        System.out.println(body);
        try {
            result = apiInstance.patchNamespace("test-namespace", body, pretty, null, null, true);
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#patchNamespace");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
            return ResultUtil.error(String.valueOf(e.getCode()), e.getMessage());
        }
        return ResultUtil.success(result);
    }
}
