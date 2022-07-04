package com.lagou.message.controller;

import com.lagou.common.page.DataGrid;
import com.lagou.common.response.ResponseDTO;
import com.lagou.message.api.dto.Message;
import com.lagou.message.api.dto.MessageDTO;
import com.lagou.message.api.dto.MessageQueryDTO;
import com.lagou.message.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private IMessageService messageService;

    @PostMapping("/getMessageList")
    public ResponseDTO<DataGrid<MessageDTO>> getCourseOrderByOrderNo(@RequestBody MessageQueryDTO messageQueryDTO) {
        return ResponseDTO.success(messageService.getMessageByUserId(messageQueryDTO));
    }

    @PostMapping("/updateReadStatus")
    ResponseDTO<Boolean> updateReadStatus(@RequestParam("userId") Integer userId){
        return ResponseDTO.success(messageService.updateReadStatus(userId));
    }

    @GetMapping("/getUnReadMessageFlag")
    ResponseDTO<Boolean> getUnReadMessageFlag(@RequestParam("userId") Integer userId){
        return ResponseDTO.success(messageService.getUnReadMessageFlag(userId));
    }

}
