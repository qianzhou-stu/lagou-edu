package com.lagou.common.mq.impl;

import com.alibaba.fastjson.JSON;
import com.lagou.common.mq.dto.BaseMqDTO;
import com.lagou.common.util.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RocketMqServiceImpl extends AbstractMqService {

    @Lazy
    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    @Override
    public void convertAndSend(String topic, BaseMqDTO<?> data) {
        ValidateUtils.notNullParam(topic);
        ValidateUtils.notNullParam(data);
        ValidateUtils.notNullParam(data.getData());
        rocketMQTemplate.asyncSend(topic, data, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("convertAndSend - onSuccess - topic：{} data:{} sendResult:{}", topic, JSON.toJSONString(data), JSON.toJSONString(sendResult));
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("convertAndSend - onException - topic：{} data:{} e:", topic, JSON.toJSONString(data), throwable);
            }
        });
    }

    @Override
    public void sendDelayed(String topic, BaseMqDTO<?> data, int delayLevel) {
        ValidateUtils.notNullParam(topic);
        ValidateUtils.notNullParam(data);
        ValidateUtils.notNullParam(data.getData());
        ValidateUtils.isTrue(delayLevel >= 0, "延迟级别参数必须大于等于0");
        rocketMQTemplate.asyncSend(topic, MessageBuilder.withPayload(data).build(), new SendCallback() {
            public void onSuccess(SendResult res) {
                log.info("sendDelayed - onSuccess - topic：{} data:{} sendResult:{}",topic,JSON.toJSONString(data),JSON.toJSONString(res));
            }
            public void onException(Throwable e) {
                log.error("sendDelayed - onException - topic：{} data:{} e:",topic,JSON.toJSONString(data),e);
            }
        },5000, delayLevel);
    }
}
