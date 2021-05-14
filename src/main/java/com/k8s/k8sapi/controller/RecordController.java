package com.k8s.k8sapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.k8s.k8sapi.common.config.K8sInit;
import com.k8s.k8sapi.common.utils.ResultUtil;
import com.k8s.k8sapi.model.entity.Record;
import com.k8s.k8sapi.repository.RecordRepository;
import com.k8s.k8sapi.service.IDeploymentService;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1DeploymentList;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 12492
 * @date: 2021/5/11 16:58
 */
@RestController
@RequestMapping("api/v1/record")
public class RecordController {
    @Resource
    RecordRepository recordRepository;

    @Resource
    IDeploymentService deploymentService;

    @Resource
    private K8sInit k8sInit;

    @RequestMapping("/findAll")
    public JSONObject findAll() {
        List<Record> all = recordRepository.findAll();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", all);
        return jsonObject;
    }

    /**
     * 指定deployment收到的同labels下其他dep发来数据
     * @param name
     * @param namespace
     * @param labelsSelector
     * @return
     */
    @RequestMapping("/getReceiveData/{name}/{namespace}/{labelsSelector}")
    public ResultUtil getOneDeploymentReceiveData(@PathVariable String name,
                                      @PathVariable String namespace,
                                      @PathVariable String labelsSelector) {
        try {
            Map<String, Integer> dataMap = new HashMap<>();

            // deployment的ip list
            AppsV1Api apiInstance = new AppsV1Api(k8sInit.getConnection());
            V1Deployment dstDeployment = apiInstance.readNamespacedDeployment(name, namespace, "true", null, null);
            List<String> dstDeploymentPodIpList = deploymentService.getDeploymentPodIpList(dstDeployment);

            // 同labelsSelector下的deployment及ip list
            Map<String, List<String>> map = new HashMap<>();
            V1DeploymentList deploymentList = apiInstance.listNamespacedDeployment(namespace, null, null,
                    null, null, labelsSelector, null, null, null, null);
            for(V1Deployment item : deploymentList.getItems()) {
                List<String> podIpList = deploymentService.getDeploymentPodIpList(item);
                if(podIpList.size() == 0) {
                    continue;
                }
                V1ObjectMeta metadata = item.getMetadata();
                if(metadata != null) {
                    String dName = metadata.getName();
                    map.put(dName, podIpList);
                }
            }

            for(Map.Entry<String, List<String>> entry : map.entrySet()) {
                String key = entry.getKey();
                if(key.equals(name)) {
                    continue;
                }
                List<String> value = entry.getValue();
                List<Record> records = recordRepository.findAllBySrcAddrInAndDstAddrIn(value, dstDeploymentPodIpList);
                int sum = 0;
                for(Record record : records) {
                    sum += record.getContentLen();
                }
                dataMap.put(key, sum);
            }
            return ResultUtil.success(dataMap);

        } catch (ApiException e) {
            return ResultUtil.error(String.valueOf(e.getCode()), e.getMessage());
        }
    }

    /**
     * 指定deployment发送给同labels下其他dep的数据
     * @param name
     * @param namespace
     * @param labelsSelector
     * @return
     */
    @RequestMapping("/getSendData/{name}/{namespace}/{labelsSelector}")
    public ResultUtil getOneDeploymentSendData(@PathVariable String name,
                                                   @PathVariable String namespace,
                                                   @PathVariable String labelsSelector) {
        try {
            Map<String, Integer> dataMap = new HashMap<>();

            // deployment的ip list
            AppsV1Api apiInstance = new AppsV1Api(k8sInit.getConnection());
            V1Deployment dstDeployment = apiInstance.readNamespacedDeployment(name, namespace, "true", null, null);
            List<String> dstDeploymentPodIpList = deploymentService.getDeploymentPodIpList(dstDeployment);

            // 同labelsSelector下的deployment及ip list
            Map<String, List<String>> map = new HashMap<>();
            V1DeploymentList deploymentList = apiInstance.listNamespacedDeployment(namespace, null, null,
                    null, null, labelsSelector, null, null, null, null);
            for(V1Deployment item : deploymentList.getItems()) {
                List<String> podIpList = deploymentService.getDeploymentPodIpList(item);
                if(podIpList.size() == 0) {
                    continue;
                }
                V1ObjectMeta metadata = item.getMetadata();
                if(metadata != null) {
                    String dName = metadata.getName();
                    map.put(dName, podIpList);
                }
            }

            for(Map.Entry<String, List<String>> entry : map.entrySet()) {
                String key = entry.getKey();
                if(key.equals(name)) {
                    continue;
                }
                List<String> value = entry.getValue();
                List<Record> records = recordRepository.findAllBySrcAddrInAndDstAddrIn(dstDeploymentPodIpList, value);
                int sum = 0;
                for(Record record : records) {
                    sum += record.getContentLen();
                }
                dataMap.put(key, sum);
            }
            return ResultUtil.success(dataMap);

        } catch (ApiException e) {
            return ResultUtil.error(String.valueOf(e.getCode()), e.getMessage());
        }

    }

}
