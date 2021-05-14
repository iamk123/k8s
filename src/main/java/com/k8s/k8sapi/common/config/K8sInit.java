package com.k8s.k8sapi.common.config;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.util.credentials.AccessTokenAuthentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Component
public class K8sInit {
    // token授权方式
    @Value("${k8s.way1.api-http}")
    private String httpApi1;

    @Value("${k8s.way1.token}")
    private String token;

    // 不授权方式
    @Value("${k8s.way2.api-http}")
    private String httpApi2;

    public ApiClient getConnection() {
        ApiClient client = new ClientBuilder().
                setBasePath(httpApi1).
                setVerifyingSsl(false).
                setAuthentication(new AccessTokenAuthentication(token)).
                build();
        Configuration.setDefaultApiClient(client);
        return client;
    }

    public ApiClient getConnectionWithoutAuth() {
        ApiClient client = new ClientBuilder().
                setBasePath(httpApi2).
                setVerifyingSsl(false).
                build();
        Configuration.setDefaultApiClient(client);
        return client;
    }

    public ApiClient getConnectionWithConfig() throws IOException {
        String kubeConfigPath = "src/main/resources/config/config";

        ApiClient client =
                ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
        Configuration.setDefaultApiClient(client);
        return client;
    }
}
