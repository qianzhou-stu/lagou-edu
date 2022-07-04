package com.lagou.message.server.store;

import com.corundumstudio.socketio.store.RedissonStoreFactory;
import com.lagou.message.util.ServerConfigUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class StoreFactoryProvider {
    /**
     * 基于Redisson的StoreFactory。
     */
    public static RedissonStoreFactory getRedissonStoreFactory(){
        Config redissonConfig = new Config();
        // 高版本需求 redis:// 前缀
        redissonConfig.useSingleServer().setAddress(StringUtils.join("redis://", ServerConfigUtils.instance.getRedisHost(),":", ServerConfigUtils.instance.getRedisPort()));
        RedissonClient redisson = Redisson.create(redissonConfig);
        return new RedissonStoreFactory(redisson);
    }
}
