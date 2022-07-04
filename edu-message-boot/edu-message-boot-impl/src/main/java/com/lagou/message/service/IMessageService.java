package com.lagou.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.common.page.DataGrid;
import com.lagou.message.api.dto.MessageDTO;
import com.lagou.message.api.dto.MessageQueryDTO;
import com.lagou.message.entity.Message;

import java.util.List;

public interface IMessageService extends IService<Message> {
    DataGrid<MessageDTO> getMessageByUserId(MessageQueryDTO messageQueryDTO);

    Boolean updateReadStatus(Integer userId);

    Boolean getUnReadMessageFlag(Integer userId);


    /**
     * @author: ma wei long
     * @date:   2020年6月30日 上午10:28:06
     */
    List<Integer> saveMessage(Integer lessonId);

    /**
     * @author: ma wei long
     * @date:   2020年6月30日 下午4:57:36
     */
    void sendMessage(com.lagou.message.api.dto.Message message);
}
