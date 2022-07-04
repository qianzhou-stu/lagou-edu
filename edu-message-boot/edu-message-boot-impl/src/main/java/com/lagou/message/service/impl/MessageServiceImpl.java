package com.lagou.message.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.common.page.DataGrid;
import com.lagou.common.util.ConvertUtil;
import com.lagou.common.util.ValidateUtils;
import com.lagou.course.api.CourseRemoteService;
import com.lagou.course.api.LessonRemoteService;

import com.lagou.message.api.dto.MessageDTO;
import com.lagou.message.api.dto.MessageQueryDTO;
import com.lagou.message.entity.Message;
import com.lagou.message.mapper.MessageMapper;
import com.lagou.message.service.IMessageService;
import com.lagou.order.api.UserCourseOrderRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {
    @Autowired
    private LessonRemoteService lessonRemoteService;
    @Autowired
    private UserCourseOrderRemoteService userCourseOrderRemoteService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Override
    public DataGrid<MessageDTO> getMessageByUserId(MessageQueryDTO messageQueryDTO) {
        log.info("getMessageByUserId - messageQueryDTO:{}", JSON.toJSONString(messageQueryDTO));
        ValidateUtils.notNullParam(messageQueryDTO.getUserId());
        ValidateUtils.isTrue(messageQueryDTO.getUserId() > 0, "用户id参数错误");
        QueryWrapper<Message> wrapper = new QueryWrapper<Message>();
        wrapper.eq("user_id", messageQueryDTO.getUserId()).orderByDesc("id");
        IPage<Message> page = new Page<Message>(messageQueryDTO.getPage(), messageQueryDTO.getRows());
        IPage<Message> pageDate = this.page(page, wrapper);
        DataGrid<MessageDTO> dataGrid = new DataGrid<>();
        dataGrid.setRows(ConvertUtil.convertList(pageDate.getRecords(), MessageDTO.class));
        dataGrid.setTotal(pageDate.getTotal());
        return dataGrid;
    }

    @Override
    public Boolean updateReadStatus(Integer userId) {
        log.info("updateReadStatus - userId:{}",userId);
        ValidateUtils.notNullParam(userId);
        ValidateUtils.isTrue(userId > 0, "用户id参数错误");
        Message updateMessage = new Message();
        updateMessage.setHasRead(1);
        return this.update(updateMessage, new QueryWrapper<Message>().eq("user_id", userId));
    }

    @Override
    public Boolean getUnReadMessageFlag(Integer userId) {
        return this.count(new QueryWrapper<Message>().eq("user_id", userId).eq("has_read", 0)) > 0;
    }
}
