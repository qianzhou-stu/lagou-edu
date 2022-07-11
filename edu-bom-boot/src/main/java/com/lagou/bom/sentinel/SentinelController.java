package com.lagou.bom.sentinel;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.lagou.bom.utils.ExceptionUtil;
import com.lagou.common.response.ResponseDTO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/sentinel")
@Api(tags = "sentinel接口")
public class SentinelController {
    @Value("${spring.application.name}")
    private String groupId;
    @Value("${zookeeper.address}")
    private String zookeeperAddress;

    // 重试次数
    private static final int RETRY_TIMES = 3;
    private static final int SLEEP_TIME = 1000;

    @GetMapping("/test1")
    @SentinelResource(value = "test1", blockHandler = "testHandleException", blockHandlerClass = {ExceptionUtil.class})
    public ResponseDTO<String> test(){
        log.info("SentinelController - test");
        return ResponseDTO.success();
    }

    @GetMapping("/addRule")
    public ResponseDTO<String> addRule() throws Exception{
        final String rule = "[\n"
                + "  {\n"
                + "    \"resource\": \"test\",\n"
                + "    \"controlBehavior\": 0,\n"
                + "    \"count\": 1.0,\n"
                + "    \"grade\": 1,\n"
                + "    \"limitApp\": \"default\",\n"
                + "    \"strategy\": 0\n"
                + "  }\n"
                + "]";
        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(zookeeperAddress, new ExponentialBackoffRetry(SLEEP_TIME, RETRY_TIMES));
        zkClient.start();
        String path = "/sentinel_rule_config/" + groupId + "front";
        Stat stat = zkClient.checkExists().forPath(path);
        if (stat == null){
            zkClient.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, null);
        }
        zkClient.setData().forPath(path, rule.getBytes());
        try {
            Thread.sleep(3000); // 休息3000毫秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            zkClient.close();
        }
        return ResponseDTO.success();
    }


    @GetMapping("/delRule")
    public ResponseDTO<String> delRule() throws Exception{
        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(zookeeperAddress, new ExponentialBackoffRetry
                (SLEEP_TIME, RETRY_TIMES));
        zkClient.start();
        String path = "/sentinel_rule_config/" + groupId + "/front";
        Stat stat = zkClient.checkExists().forPath(path);
        if (stat != null) {
            zkClient.delete().deletingChildrenIfNeeded().forPath(path);
        }
        try {
            Thread.sleep(3000); // 休息3000毫秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            zkClient.close();
        }
        return ResponseDTO.success();
    }
}
