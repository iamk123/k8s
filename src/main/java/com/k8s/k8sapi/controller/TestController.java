package com.k8s.k8sapi.controller;

import com.k8s.k8sapi.common.config.K8sInit;
import com.sun.jna.Platform;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1NamespaceList;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("api/v1/test")
public class TestController {

    @Resource
    K8sInit k8sInit;


    @RequestMapping("/getPods")
    public V1PodList getPods() throws ApiException, IOException {
        //读取k8s集群配置文件
        String kubeConfigPath = "src/main/resources/config/config";

        //加载k8s,confg
        //加载群集内配置，包括：
        //    1.服务帐户CA.
        //    2. service-account bearer-token
        //    3.服务帐户命名空间
        //    4.来自预设环境变量的主端点（ip，端口）
        ApiClient client =
                ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();

        //将加载confi的client设置为默认的client
        Configuration.setDefaultApiClient(client);

        //创建一个api对象
        CoreV1Api api = new CoreV1Api();
        //打印所有的pod
        V1PodList list = api.listPodForAllNamespaces(null, null, null,
                null, null, null, null, null, null);
        // for (V1Pod item : list.getItems()) {
        //     System.out.println(item.getMetadata().getName());
        //
        // }
        return list;
    }

    @GetMapping("/getNamespace")
    public V1NamespaceList getNamespace() {
        ApiClient client = k8sInit.getConnection();
        CoreV1Api api = new CoreV1Api();
        V1NamespaceList namespaceList = new V1NamespaceList();
        try {
            namespaceList = api.listNamespace(null, true, null, null,
                    null, null, null, null, null);
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#listNamespace");
            e.printStackTrace();
        }
        return namespaceList;
    }

    /**
     * 无需认证调用k8s api
     * success
     * @return
     */
    @GetMapping("/getNamespace2")
    public V1NamespaceList getNamespace2() {
        ApiClient client = k8sInit.getConnectionWithoutAuth();
        CoreV1Api api = new CoreV1Api();
        String pretty = "true";
        V1NamespaceList namespaceList = new V1NamespaceList();
        try {
            namespaceList = api.listNamespace(null, true, null, null,
                    null, null, null, null, null);
        } catch (ApiException e) {
            System.err.println("Exception when calling CoreV1Api#listNamespace");
            e.printStackTrace();
        }
        return namespaceList;
    }

    @GetMapping("/")
    public void test() throws PcapNativeException, UnknownHostException {
        System.out.println("============");
        test1();
        System.out.println("============");
        List<PcapNetworkInterface> inters = Pcaps.findAllDevs();
        System.out.println(inters.size());
        System.out.println(inters);
        System.out.println("============");
        InetAddress addr = InetAddress.getByName("docker0");
        PcapNetworkInterface nif1 = Pcaps.getDevByAddress(addr);
        System.out.println(nif1);
        System.out.println(nif1.getAddresses());
        System.out.println(nif1.getName());
    }


    public static void test1() throws UnknownHostException {

        Enumeration<NetworkInterface> netInterfaces;
        try {
            // 拿到所有网卡
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            // 遍历每个网卡，拿到ip
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(':') == -1) {
                        System.out.println(ni.getName() + " " + ip.getHostAddress());
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) throws Exception {





        
        // if (args.length < 2) {
        //     System.err.println("netool <ethName> <count>");
        //     return;
        // }
        test1();
        // System.out.println(InetAddress.getLocalHost().getHostAddress().toString());
        // String nifName = args[0];
        // int count = Integer.parseInt(args[1]);


        List<PcapNetworkInterface> inters = Pcaps.findAllDevs();
        System.out.println(inters.size());
        System.out.println(inters);

        InetAddress addr = InetAddress.getByName("127.0.0.1");
        PcapNetworkInterface nif1 = Pcaps.getDevByAddress(addr);
        System.out.println(nif1);
        System.out.println(nif1.getAddresses());
        System.out.println(nif1.getName());

        String nifName = "eth0";
        int count = 6;

        // 1. get network interface
        PcapNetworkInterface nif = Pcaps.getDevByName(nifName);
        if (nif == null)  {
            System.err.println("Cannot get interfance - " + nifName);
            return;
        }
        // 2. open handle
        PcapNetworkInterface.PromiscuousMode mode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;
        int timeout = 10;
        int snapLen = 65536;
        PcapHandle handle = nif.openLive(snapLen, mode, timeout);

        // 4. set pcap dumper
        final PcapDumper dumper = handle.dumpOpen("dump.pcap");

        final AtomicLong dumped = new AtomicLong(0);
        try {
            // 5. set filter
            handle.setFilter("tcp port 443", BpfProgram.BpfCompileMode.OPTIMIZE);

            // 6. prepare listener
            PacketListener listener = new PacketListener() {

                @Override
                public void gotPacket(Packet packet) {
                    try {
                        dumper.dump(packet);
                        dumped.incrementAndGet();
                    }
                    catch (NotOpenException ignore) {
                    }
                }
            };

            // 7. start looper
            try {
                handle.loop(count, listener);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Print out handle statistics
            PcapStat stats = handle.getStats();
            System.out.println("Pakcets dumped: " + dumped.get());
            System.out.println("Packets received: " + stats.getNumPacketsReceived());
            System.out.println("Packets dropped: " + stats.getNumPacketsDropped());
            System.out.println("Packets dropped by interface: " + stats.getNumPacketsDroppedByIf());
            // Supported by WinPcap only
            if (Platform.isWindows()) {
                System.out.println("Packets captured: " +stats.getNumPacketsCaptured());
            }
        }
        finally {
            dumper.close();
            handle.close();
        }
    }
}
