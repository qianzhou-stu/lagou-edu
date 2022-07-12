package com.lagou.bom.message.controller;

import com.alibaba.fastjson.JSON;
import com.lagou.bom.common.UserManager;
import com.lagou.bom.message.service.MessageService;
import com.lagou.bom.message.vo.request.MessageQueryVo;
import com.lagou.bom.message.vo.response.MessageVo;
import com.lagou.common.page.DataGrid;
import com.lagou.common.response.ResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: ma wei long
 * @date:   2020年6月28日 下午2:04:45
 */
@Slf4j
@RestController
@RequestMapping("/message")
@Api(description = "消息通知相关接口", tags = "消息通知接口")
public class MessageController {

	@Autowired
	private MessageService messageService;
	
	/**
     * @author: ma wei long
     * @date:   2020年6月17日 下午3:33:18   
    */
    @ApiOperation("查询消息通知列表")
    @PostMapping("/getMessageList")
    public ResponseDTO<DataGrid<MessageVo>> getMessageList(@RequestBody MessageQueryVo param) {
    	log.info("getMessageList - param:{} userId:{}",JSON.toJSONString(param), UserManager.getUserId());
    	param.setUserId(UserManager.getUserId());
        return ResponseDTO.success(messageService.getMessageList(param));
    }
    
    /**
     * @author: ma wei long
     * @date:   2020年6月29日 上午11:19:16   
     */
    @ApiOperation("更新消息已读")
    @GetMapping("/updateReadStatus")
    public ResponseDTO<Boolean> updateReadStatus() {
    	log.info("getMessageList - updateReadStatus - userId:{}",UserManager.getUserId());
        return ResponseDTO.success(messageService.updateReadStatus(UserManager.getUserId()));
    }
    
    /**
     * @author: ma wei long
     * @date:   2020年7月13日 下午7:59:45   
    */
    @ApiOperation("获取用户未读消息标识")
    @GetMapping("/getUnReadMessage")
    public ResponseDTO<Boolean> getUnReadMessage(){
        return ResponseDTO.success(messageService.getUnReadMessageFlag(UserManager.getUserId()));
    }
}
