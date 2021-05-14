package com.k8s.k8sapi;

import com.k8s.k8sapi.common.utils.DateUtil;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreApi;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.auth.ApiKeyAuth;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.kubernetes.client.util.ClientBuilder.*;

public class apiClient {

    // k8s集群配置文件
    public String kubeConfigPath = "src/main/resources/config/config";

    public void getAllPods() throws IOException, ApiException {
        //加载confg
        io.kubernetes.client.openapi.ApiClient client;
        client = kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();

        //将加载config的client设置为默认的client
        Configuration.setDefaultApiClient(client);

        //创建一个api对象
        CoreV1Api api = new CoreV1Api();

        //打印所有的pod
        V1PodList list = api.listPodForAllNamespaces(null, null, null,
                null, null, null, null, null, null);

        for (V1Pod item : list.getItems()) {
            System.out.println(item.getMetadata().getName());
        }

        V1ComponentStatusList v1ComponentStatusList = api.listComponentStatus(null, null,
                null, null, null, null, null, null, null);

        // for (V1ComponentStatus item : v1ComponentStatusList.getItems()) {
        //     System.out.println(item.getMetadata().getName());
        // }
    }

    // public static void main(String[] args) throws IOException, ApiException {
    //     apiClient ac = new apiClient();
    //     ac.getAllPods();
    // }

    // public static void main(String[] args) {
    //     ApiClient defaultClient = Configuration.getDefaultApiClient();
    //     defaultClient.setBasePath("http://192.168.2.3:6443");
    //
    //     // Configure API key authorization: BearerToken
    //     ApiKeyAuth BearerToken = (ApiKeyAuth) defaultClient.getAuthentication("BearerToken");
    //     BearerToken.setApiKey(" eyJhbGciOiJSUzI1NiIsImtpZCI6IlN3U2FhNlBlRUo2UVVTVVpTYnFzTXFqcW5SQ2xXdS00RlBRTmswWm5zTDgifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJrdWJvYXJkLXVzZXItdG9rZW4tNmJsd3IiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC5uYW1lIjoia3Vib2FyZC11c2VyIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQudWlkIjoiMTNjMDNiMTUtYzI3Ny00NTQxLTlhZTctZDAwMGQ2Y2VmMjAzIiwic3ViIjoic3lzdGVtOnNlcnZpY2VhY2NvdW50Omt1YmUtc3lzdGVtOmt1Ym9hcmQtdXNlciJ9.WFK16Y4TRNvL9OWACUP9Tvl5gCdRVbQSgkOBxwqAY6Vjg3p9u1RzmDiF4_A0J91mA5UXjyFLpIzfbmRUimNd01BJ8ex8L9PwJqcudbfGpBJH2428SLfjwjzJb7tzO_2TGfJj64dlSA5lcb3Gue6KlAn1_9jb0eMcRjCXGkbDerLcYIiIgwj_8J6bisCkMPRLJ9PT6ALJ7nuPWU1QQv73kA5cB2wJowMggS9Ov7WW1XSA1iDMdTq9jIyS8Fghcu8n9ozk0QCBCPiaxYvTNDyq80kbZ1ChBCtrohflxho_4n5KS8prAcPFrNALeltZJ9ur676uMPvwCn-3abZKqB5bvQ");
    //     // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
    //     //BearerToken.setApiKeyPrefix("Token");
    //
    //     CoreApi apiInstance = new CoreApi(defaultClient);
    //     try {
    //         V1APIVersions result = apiInstance.getAPIVersions();
    //         System.out.println(result);
    //     } catch (ApiException e) {
    //         System.err.println("Exception when calling CoreApi#getAPIVersions");
    //         System.err.println("Status code: " + e.getCode());
    //         System.err.println("Reason: " + e.getResponseBody());
    //         System.err.println("Response headers: " + e.getResponseHeaders());
    //         e.printStackTrace();
    //     }
    // }

    public static void main(String[] args) {
        // DateUtil.format()
        // Integer[] a = new Integer[]{1,2 ,3};
        // System.out.println(a[-1]);
        // List<Integer> a = new ArrayList<>();
        // a.add(1);
        // a.add(2);
        // System.out.println(a.get(-1));
    }

}
