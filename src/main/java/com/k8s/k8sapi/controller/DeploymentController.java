package com.k8s.k8sapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.k8s.k8sapi.common.config.K8sInit;
import com.k8s.k8sapi.common.utils.ResultUtil;
import com.k8s.k8sapi.model.deployment.Deployment;
import com.k8s.k8sapi.model.dto.DeploymentDTO;
import com.k8s.k8sapi.model.vo.DeploymentVO;
import com.k8s.k8sapi.service.IDeploymentService;
import io.kubernetes.client.custom.V1Patch;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api/v1/deployment")
public class DeploymentController {

    @Resource
    private K8sInit k8sInit;

    @Resource
    IDeploymentService deploymentService;

    /**
     * 列出所有namespace下的deployment的简要信息
     * @param key 根据deployment的name模糊查询
     * @param labelSelector 标签选择器 根据“labels”的自定义key来筛选
     * @return
     * @throws ApiException
     */
    @GetMapping("/listDeploymentForAllNamespaces")
    public ResultUtil listDeploymentForAllNamespacesBrief(@RequestParam(defaultValue = "") String key,
                                                     @RequestParam(defaultValue = "") String labelSelector) throws ApiException {
        List<DeploymentVO> res = deploymentService.listDeploymentForAllNamespacesBrief(key, labelSelector);
        return ResultUtil.success(res);
    }

    /**
     * 列出所有namespace下的deployment的详细信息
     * @param key 根据deployment的name模糊查询
     * @param labelSelector 标签选择器 根据“labels”的自定义key来筛选
     * @return
     * @throws ApiException
     */
    @GetMapping("/listDeploymentForAllNamespaces2")
    public ResultUtil listDeploymentForAllNamespaces(@RequestParam(defaultValue = "") String key,
                                                          @RequestParam(defaultValue = "") String labelSelector) throws ApiException {
        List<Deployment> res = new ArrayList<>();
        AppsV1Api apiInstance = new AppsV1Api(k8sInit.getConnection());
        V1DeploymentList deploymentList = apiInstance.listDeploymentForAllNamespaces(null, null, null,
                labelSelector, null, null, null, null, null);
        for(V1Deployment item : deploymentList.getItems()) {
            Deployment deployment = deploymentService.readNamespacedDeployment(item);
            res.add(deployment);
        }

        return ResultUtil.success(res);
    }



    /**
     * 根据name、namespace查看deployment的信息
     * https://blog.csdn.net/dfBeautifulLive/article/details/103735048?spm=1001.2014.3001.5501
     * @param namespace
     * @param name
     * @return
     */
    @GetMapping("/readNamespacedDeployment/{name}/{namespace}")
    public ResultUtil readNamespacedDeployment(@PathVariable String name,
                                                @PathVariable String namespace) throws ApiException {

        AppsV1Api apiInstance = new AppsV1Api(k8sInit.getConnection());
        V1Deployment deployment = apiInstance.readNamespacedDeployment(name, namespace, "true", null, null);
        // System.out.println(deployment);
        Deployment res;
        try {
            res = deploymentService.readNamespacedDeployment(deployment);
        } catch (ApiException e) {
            return ResultUtil.error(String.valueOf(e.getCode()), e.getMessage());
        }
        return ResultUtil.success(res);
    }

    /**
     * 根据name、namespace修改deployment的副本数
     * https://blog.csdn.net/dfBeautifulLive/article/details/103735048?spm=1001.2014.3001.5501
     * @return
     */
    @PostMapping("/patchNamespacedDeployment")
    public ResultUtil patchNamespacedDeployment(@RequestBody DeploymentVO deploymentVO) {
        System.out.println(deploymentVO);
            // String namespace, String name, int replicas) {
        AppsV1Api apiInstance = new AppsV1Api(k8sInit.getConnection());
        // 更新副本的json串
        String jsonPatchStr = "[{\"op\":\"replace\",\"path\":\"/spec/replicas\", \"value\": " + deploymentVO.getReplicas() + " }]";
        V1Patch body = new V1Patch(jsonPatchStr);
        V1Deployment v1Deployment;
        try {
            v1Deployment = apiInstance.patchNamespacedDeployment(
                    deploymentVO.getName(),
                    deploymentVO.getNamespace(),
                    body,
                    null,
                    null,
                    null,
                    null);
        } catch (ApiException e) {
            // e.printStackTrace();
            return ResultUtil.error(String.valueOf(e.getCode()), e.getMessage());
        }
        return ResultUtil.success();
    }



    /**
     * 列出指定namespace下的deployment
     * @param namespace
     * @return
     */
    @GetMapping("/listNamespacedDeployment/{namespace}")
    public ResultUtil listNamespacedDeployment(@PathVariable String namespace) {
        Gson gson = new Gson();
        AppsV1Api apiInstance = new AppsV1Api(k8sInit.getConnection());
        V1DeploymentList result;
        try {
            result = apiInstance.listNamespacedDeployment(namespace, "true", null,
                    null, null, null, null, null, null, null);
        } catch (ApiException e) {
            return ResultUtil.error(String.valueOf(e.getCode()), e.getMessage());
        }
        return ResultUtil.success(gson.toJson(result));
    }


    /**
     * 创建一个deployment
     * @param deploymentDTO
     * @return
     */
    @PostMapping("/createNamespacedDeployment")
    public ResultUtil createNamespacedDeployment(@RequestBody DeploymentDTO deploymentDTO) {
        System.out.println(deploymentDTO);
        Gson gson = new Gson();
        AppsV1Api apiInstance = new AppsV1Api(k8sInit.getConnection());
        V1Deployment result;

        V1Deployment body = deploymentService.createV1Deployment(deploymentDTO);

        try {
            result = apiInstance.createNamespacedDeployment(
                    deploymentDTO.getNamespace(),
                    body,
                    null,
                    null,
                    null);
        } catch (ApiException e) {
            e.printStackTrace();
            return ResultUtil.error(String.valueOf(e.getCode()), e.getMessage());
        }
        return ResultUtil.success(gson.toJson(result));
    }

    /**
     * 获取namespace下同一labels下的deployment以及他们的pod ip list
     * @param namespace 命名空间
     * @param labelsSelector 标签
     * @return
     */
    @RequestMapping("/listNamespaceDeploymentAndAllPodByLabel/{namespace}/{labelsSelector}")
    public ResultUtil listAllNamespaceDeploymentAndAllPodByLabel(@PathVariable String namespace,
                                                                 @PathVariable String labelsSelector) {
        // 在部署时需要添加两个标签，一个用于指定同一系列的微服务，一个用于指定deployment下的pod（默认用deployment的name）

        // 获取同一项目的所有微服务
        // 1.通过标签选择器labelSelector, 部署时键都指定为weteam

        // 获取deployment的所有pod的ip：
        // 1.通过遍历所有pod，看其是否包含的与deployment同名的容器
        // 2.遍历pod，看其name中是否以deployment的name为前缀
        // 3.通过标签选择器labelSelector（此处用的这个）

        AppsV1Api apiInstance = new AppsV1Api(k8sInit.getConnection());
        Map<String, List<String>> map = new HashMap<>();
        V1DeploymentList deploymentList;
        try {
            deploymentList = apiInstance.listNamespacedDeployment(namespace, null, null,
                    null, null, labelsSelector, null, null, null, null);
            for(V1Deployment item : deploymentList.getItems()) {
                List<String> podIpList = deploymentService.getDeploymentPodIpList(item);
                if(podIpList.size() == 0) {
                    continue;
                }
                V1ObjectMeta metadata = item.getMetadata();
                if(metadata != null) {
                    String name = metadata.getName();
                    map.put(name, podIpList);
                }
            }
        } catch (ApiException e) {
            return ResultUtil.error(String.valueOf(e.getCode()), e.getMessage());
        }


        return ResultUtil.success(map);
    }


}
