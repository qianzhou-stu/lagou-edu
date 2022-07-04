package com.lagou.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.common.page.DataGrid;

import com.lagou.message.api.dto.MessageDTO;
import com.lagou.message.api.dto.MessageQueryDTO;
import com.lagou.message.entity.Message;

public interface IMessageService extends IService<Message> {
    DataGrid<MessageDTO> getMessageByUserId(MessageQueryDTO messageQueryDTO);

    Boolean updateReadStatus(Integer userId);

    Boolean getUnReadMessageFlag(Integer userId);
}
