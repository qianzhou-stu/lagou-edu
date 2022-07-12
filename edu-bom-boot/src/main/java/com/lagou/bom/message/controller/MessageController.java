package com.lagou.bom.message.controller;

import com.lagou.bom.message.vo.request.MessageQueryVo;
import com.lagou.bom.message.vo.response.MessageVo;
import com.lagou.common.page.DataGrid;
import com.lagou.common.response.ResponseDTO;
import com.lagou.message.api.MessageRemoteService;
import com.lagou.message.api.dto.MessageQueryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/message")
@Api(description = "消息通知相关接口", tags = "消息通知接口")
public class MessageController {
    @Autowired
    private MessageRemoteService messageRemoteService;

    @ApiOperation("查询消息通知列表")
    @PostMapping("/getMessageList")
    public ResponseDTO<DataGrid<MessageVo>> getMessageList(@RequestBody MessageQueryDTO param) {
        log.info("getMessageList - param:{} userId:{}",JSON.toJSONString(param),UserManager.getUserId());
        param.setUserId(UserManager.getUserId());
        return ResponseDTO.success(messageRemoteService.getMessageList(param));
    }
}
