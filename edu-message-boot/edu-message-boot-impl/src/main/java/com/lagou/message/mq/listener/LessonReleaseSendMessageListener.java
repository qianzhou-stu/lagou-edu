package com.lagou.message.mq.listener;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.lagou.common.constant.MQConstant;
import com.lagou.common.mq.dto.BaseMqDTO;
import com.lagou.common.mq.listener.AbstractMqListener;
import com.lagou.common.util.ValidateUtils;
import com.lagou.message.api.dto.Message;
import com.lagou.message.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Component
@RocketMQMessageListener(topic = MQConstant.Topic.LESSON_RELEASE_SEND_MESSAGE, consumerGroup = "${rocketmq.producer.group}" + "_" + MQConstant.Topic.LESSON_RELEASE_SEND_MESSAGE)
public class LessonReleaseSendMessageListener extends AbstractMqListener<BaseMqDTO<List<Integer>>> implements RocketMQListener<BaseMqDTO<List<Integer>>> {


    @Autowired
    private IMessageService messageService;

    @Override
    public void onMessage(BaseMqDTO<List<Integer>> data) {
        log.info("onMessage - data:{}", JSON.toJSONString(data));
        ValidateUtils.notNullParam(data);
        ValidateUtils.notNullParam(data.getMessageId());

        if (this.checkMessageId(data.getMessageId())){
            return;
        }
        List<Integer> userIdList = data.getData();
        if (CollectionUtils.isEmpty(userIdList)){
            return;
        }
        Message message = null;
        for (Integer userId : userIdList) {
            message = new Message();
            message.setUserId(userId);
            messageService.sendMessage(message);
        }
        this.updateMessageId(data.getMessageId());
    }
}
