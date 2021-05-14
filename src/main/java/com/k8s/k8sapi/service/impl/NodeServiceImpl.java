package com.k8s.k8sapi.service.impl;

import com.k8s.k8sapi.common.config.K8sInit;
import com.k8s.k8sapi.common.utils.DateUtil;
import com.k8s.k8sapi.common.utils.ResultUtil;
import com.k8s.k8sapi.model.vo.Node;
import com.k8s.k8sapi.service.INodeService;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Node;
import io.kubernetes.client.openapi.models.V1NodeAddress;
import io.kubernetes.client.openapi.models.V1NodeCondition;
import io.kubernetes.client.openapi.models.V1NodeList;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: 12492
 * @date: 2021/4/18 15:21
 */
@Service
public class NodeServiceImpl implements INodeService {
    @Resource
    private K8sInit k8sInit;

    @Override
    public List<Node> listNode(String key) throws ApiException {
        List<Node> res = new ArrayList<>();
        CoreV1Api apiInstance = new CoreV1Api(k8sInit.getConnection());
        String pretty = "true";
        V1NodeList v1NodeList = apiInstance.listNode(pretty, true, null,
                null, null, null, null, null, null);
        Node node;
        for(V1Node item : v1NodeList.getItems()) {
            // name
            String name = item.getMetadata().getName();
            if(name != null && !name.contains(key)) {
                continue;
            }
            // labels
            Map<String, String> labels = item.getMetadata().getLabels();
            List<String> labelsStr = new ArrayList<>();
            for(Map.Entry<String, String> entry : labels.entrySet()) {
                labelsStr.add("[" + entry.getKey() + "]" + " : " + entry.getValue());
            }
            // address
            String address = item.getStatus().getAddresses().get(0).getAddress();
            // condition
            List<V1NodeCondition> conditions = item.getStatus().getConditions();
            V1NodeCondition v1NodeCondition = conditions.get(conditions.size() - 1);
            String conditionType = v1NodeCondition.getType();
            String conditionStatus = v1NodeCondition.getStatus();

            node = new Node();
            node.setName(name);
            node.setLabels(labelsStr);
            node.setAddress(address);
            node.setConditionType(conditionType);
            node.setConditionStatus(conditionStatus);
            res.add(node);
        }
        return res;
    }
}
