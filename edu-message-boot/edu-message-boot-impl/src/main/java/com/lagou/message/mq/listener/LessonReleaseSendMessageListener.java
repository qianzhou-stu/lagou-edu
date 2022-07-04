package com.lagou.message.mq.listener;

import com.lagou.common.constant.MQConstant;
import com.lagou.common.mq.dto.BaseMqDTO;
import com.lagou.common.mq.listener.AbstractMqListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RocketMQMessageListener(topic = MQConstant.Topic.LESSON_RELEASE_SEND_MESSAGE, consumerGroup = "${rocketmq.producer.group}" + "_" + MQConstant.Topic.LESSON_RELEASE_SEND_MESSAGE)
public class LessonReleaseSendMessageListener extends AbstractMqListener<BaseMqDTO<List<Integer>>> implements RocketMQListener<BaseMqDTO<List<Integer>>> {


    @Override
    public void onMessage(BaseMqDTO<List<Integer>> data) {

    }
}
