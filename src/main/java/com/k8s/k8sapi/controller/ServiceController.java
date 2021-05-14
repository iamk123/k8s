package com.k8s.k8sapi.controller;

import com.google.gson.Gson;
import com.k8s.k8sapi.common.config.K8sInit;
import com.k8s.k8sapi.common.utils.ResultUtil;
import com.k8s.k8sapi.model.dto.ServiceDTO;
import com.k8s.k8sapi.model.vo.ServiceVO;
import com.k8s.k8sapi.service.IServiceService;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1DeleteOptions;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.openapi.models.V1ServiceList;
import io.kubernetes.client.openapi.models.V1Status;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/service")
public class ServiceController {

    @Resource
    private K8sInit k8sInit;

    @Resource
    private IServiceService serviceService;

    @GetMapping("/listServiceForAllNamespaces")
    public ResultUtil listServiceForAllNamespaces(@RequestParam(defaultValue = "") String key) throws ApiException {
        List<ServiceVO> res = serviceService.listServiceForAllNamespaces(key);
        System.out.println(res);
        return ResultUtil.success(res);
    }


    /**
     * 创建一个service
     * @param namespace
     * @param serviceDTO
     * @return
     */
    @PostMapping("/createNamespacedService/{namespace}")
    public ResultUtil createNamespacedService(@PathVariable String namespace,
                                              @RequestBody ServiceDTO serviceDTO) {
        System.out.println(serviceDTO);
        try {
            V1Service namespacedService = serviceService.createNamespacedService(namespace, serviceDTO);
            System.out.println(namespacedService);
        }catch (ApiException e) {
            return ResultUtil.error(String.valueOf(e.getCode()), e.getMessage());
        }
        return ResultUtil.success();
    }

    /**
     * 删除service
     * @param name service的名称
     * @param namespace 命名空间
     * @return
     */
    @GetMapping("/deleteNamespacedService/{name}/{namespace}")
    public ResultUtil deleteNamespacedService(@PathVariable String name,
                                              @PathVariable String namespace) {
        try {
            V1Status result = serviceService.deleteNamespacedService(name, namespace);
            System.out.println(result);
        }catch (ApiException e) {
            return ResultUtil.error(String.valueOf(e.getCode()), e.getMessage());
        }
        return ResultUtil.success();
    }











    /**
     * 获取指定namespace下的service
     * @param namespace
     * @return
     */
    @GetMapping("/listNamespacedService/{namespace}")
    public ArrayList<String> listNamespacedService(@PathVariable String namespace) {
        ArrayList<String> res = new ArrayList<>();
        ApiClient client = k8sInit.getConnection();
        CoreV1Api apiInstance = new CoreV1Api(client);
        String pretty = "true";
        try {
            V1ServiceList result = apiInstance.listNamespacedService(namespace, pretty, true,
                    null, null, null, null, null, null, null);
            for(V1Service service : result.getItems()) {
                String name = service.getMetadata().getName();
                if(name != null) {
                    res.add(name);
                }
            }
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#listNamespacedService");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 获取全部service
     * @return
     */
    // @GetMapping("/listServiceForAllNamespaces")
    // public ArrayList<String> listServiceForAllNamespaces() {
    //     ArrayList<String> res = new ArrayList<>();
    //     CoreV1Api apiInstance = new CoreV1Api(k8sInit.getConnection());
    //     String pretty = "true";
    //     try {
    //         V1ServiceList result = apiInstance.listServiceForAllNamespaces(true, null,
    //                 null, null, null, pretty, null, null, null);
    //         for(V1Service service : result.getItems()) {
    //             String name = service.getMetadata().getName();
    //             if(name != null) {
    //                 res.add(name);
    //             }
    //         }
    //     } catch (ApiException e) {
    //         System.err.println("Exception when calling CoreV1Api#listServiceForAllNamespaces");
    //         System.err.println("Status code: " + e.getCode());
    //         System.err.println("Reason: " + e.getResponseBody());
    //         System.err.println("Response headers: " + e.getResponseHeaders());
    //         e.printStackTrace();
    //     }
    //     return res;
    // }

    /**
     * 获取指定service信息
     * @param name
     * @param namespace
     * @return
     */
    @GetMapping("/readNamespacedService/{name}/{namespace}")
    public ResultUtil readNamespacedService(@PathVariable String name,
                                            @PathVariable String namespace) {
        Gson gson = new Gson();
        CoreV1Api apiInstance = new CoreV1Api(k8sInit.getConnection());
        V1Service result;
        try {
            result = apiInstance.readNamespacedService(name, namespace, null, true, true);
        } catch (ApiException e) {
            return ResultUtil.error(String.valueOf(e.getCode()), e.getMessage());
        }
        System.out.println(result);
        return ResultUtil.success(gson.toJson(result));
    }

}
