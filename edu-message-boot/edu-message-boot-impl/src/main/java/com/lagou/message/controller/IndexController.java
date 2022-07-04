package com.lagou.message.controller;

import com.lagou.common.response.ResponseDTO;
import com.lagou.message.api.dto.Message;
import com.lagou.message.server.PushServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    @GetMapping("/index")
    public String getCourseOrderByOrderNo() {
        return "index";
    }

    @GetMapping("/sendMessage")
    @ResponseBody
    public ResponseDTO<?> sendMessage() {
        Message message = new Message();
        message.setContent("test");
        message.setUserId(100029966);
        PushServer.pushServer.push(message);
        return ResponseDTO.success();
    }
}
