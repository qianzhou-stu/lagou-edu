package com.lagou.message.mq.listener;

import com.lagou.message.api.dto.LessonStatusReleaseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(topic = MQConstant.Topic.LESSON_STATUS_RELEASE, consumerGroup = "${rocketmq.producer.group}" + "_" + MQConstant.Topic.LESSON_STATUS_RELEASE)
public class LessonStatusReleaseListener extends AbstractMqListener<BaseMqDTO<LessonStatusReleaseDTO>> implements RocketMQListener<BaseMqDTO<LessonStatusReleaseDTO>>{



    @Override
    public void onMessage(BaseMqDTO<LessonStatusReleaseDTO> data) {

    }
}
