package com.k8s.k8sapi.controller;

import com.k8s.k8sapi.common.config.K8sInit;
import com.k8s.k8sapi.common.utils.ResultUtil;
import com.k8s.k8sapi.model.vo.Node;
import com.k8s.k8sapi.service.INodeService;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Node;
import io.kubernetes.client.openapi.models.V1NodeList;
import io.kubernetes.client.openapi.models.V1NodeStatus;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("api/v1/node")
public class NodeController {

    @Resource
    private K8sInit k8sInit;
    @Resource
    INodeService nodeService;

    /**
     * 查看所有node节点信息
     * @return
     */
    @GetMapping("/listNode")
    public ResultUtil listNode(@RequestParam(defaultValue = "") String key) throws ApiException {
        List<Node> res = nodeService.listNode(key);
        return ResultUtil.success(res);
    }

    /**
     * 根据name查看指定node信息
     * @param name
     * @return
     */
    @GetMapping("/readNode/{name}")
    public ResultUtil readNode(@PathVariable String name) {
        CoreV1Api apiInstance = new CoreV1Api(k8sInit.getConnection());
        V1Node result;
        try {
            result = apiInstance.readNode(name, "true", true, true);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#readNode");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
            return ResultUtil.error(String.valueOf(e.getCode()), e.getMessage());
        }
        return ResultUtil.success(result);
    }


    @GetMapping("/replaceNode/{name}")
    public ResultUtil replaceNode(@PathVariable String name) {
        CoreV1Api apiInstance = new CoreV1Api(k8sInit.getConnection());
        V1Node body = new V1Node();
        V1ObjectMeta v1ObjectMeta = new V1ObjectMeta();

        v1ObjectMeta.setName(name);
        body.setMetadata(v1ObjectMeta);
        V1Node result;
        try {
            result = apiInstance.replaceNode(name, body, "true", null, null);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#replaceNodeStatus");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
            return ResultUtil.error(String.valueOf(e.getCode()), e.getMessage());
        }
        return ResultUtil.success(result);
    }


    @GetMapping("/createNode")
    public ResultUtil createNode() {
        CoreV1Api apiInstance = new CoreV1Api(k8sInit.getConnection());
        V1Node body = new V1Node();
        body.setApiVersion("v1");
        // body.setKind();
        V1Node result;
        try {
            result = apiInstance.createNode(body, "true", null, null);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#createNode");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
            return ResultUtil.error(String.valueOf(e.getCode()), e.getMessage());
        }
        return ResultUtil.success(result);
    }

}
