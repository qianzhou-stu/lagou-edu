package com.lagou.message.api;

import com.lagou.common.page.DataGrid;
import com.lagou.common.response.ResponseDTO;
import com.lagou.message.api.dto.MessageDTO;
import com.lagou.message.api.dto.MessageQueryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${remote.feign.edu-message-boot.name:edu-message-boot}", path = "/message")
public interface MessageRemoteService {
    /**
     * 查询消息模块的列表
     * @param messageQueryDTO
     * @return
     */
    @PostMapping("/getMessageList")
    ResponseDTO<DataGrid<MessageDTO>> getMessageList(@RequestBody MessageQueryDTO messageQueryDTO);

    /**
     * 更新阅读状态
     * @param userId
     * @return
     */
    @PostMapping("/updateReadStatus")
    ResponseDTO<Boolean> updateReadStatus(@RequestParam("userId") Integer userId);

    /**
     * 获取未读标识按钮
     * @param userId
     * @return
     */
    @GetMapping("/getUnReadMessageFlag")
    ResponseDTO<Boolean> getUnReadMessageFlag(@RequestParam("userId") Integer userId);

}
