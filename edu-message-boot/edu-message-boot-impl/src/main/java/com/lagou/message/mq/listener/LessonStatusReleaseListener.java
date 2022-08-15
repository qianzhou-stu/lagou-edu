package com.lagou.message.mq.listener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.lagou.common.constant.MQConstant;
import com.lagou.common.mq.RocketMqService;
import com.lagou.common.mq.dto.BaseMqDTO;
import com.lagou.common.mq.listener.AbstractMqListener;
import com.lagou.common.util.ValidateUtils;
import com.lagou.message.api.dto.LessonStatusReleaseDTO;
import com.lagou.message.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RocketMQMessageListener(topic = MQConstant.Topic.LESSON_STATUS_RELEASE, consumerGroup = "${rocketmq.producer.group}" + "_" + MQConstant.Topic.LESSON_STATUS_RELEASE)
public class LessonStatusReleaseListener extends AbstractMqListener<BaseMqDTO<LessonStatusReleaseDTO>> implements RocketMQListener<BaseMqDTO<LessonStatusReleaseDTO>> {


    @Autowired
    private IMessageService messageService;
    @Autowired
    private RocketMqService rocketMqService;

    @Override
    public void onMessage(BaseMqDTO<LessonStatusReleaseDTO> data) {
        log.info("onMessage - data:{}", JSON.toJSONString(data));
        ValidateUtils.notNullParam(data);
        ValidateUtils.notNullParam(data.getMessageId());
        if(this.checkMessageId(data.getMessageId())){
            return;
        }
        List<Integer> userIdList = messageService.saveMessage(data.getData().getLessonId());
        if (!CollectionUtils.isEmpty(userIdList)){
            rocketMqService.convertAndSend(MQConstant.Topic.LESSON_RELEASE_SEND_MESSAGE, new BaseMqDTO<List<Integer>>(userIdList, UUID.randomUUID().toString()));
        }
        this.updateMessageId(data.getMessageId());
    }
}
