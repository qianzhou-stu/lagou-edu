package com.lagou.bom.message.service.impl;

import com.alibaba.fastjson.JSON;
import com.lagou.bom.message.service.MessageService;
import com.lagou.bom.message.vo.request.MessageQueryVo;
import com.lagou.bom.message.vo.response.MessageVo;
import com.lagou.common.page.DataGrid;
import com.lagou.common.response.ResponseDTO;
import com.lagou.common.util.ConvertUtil;
import com.lagou.common.util.ValidateUtils;
import com.lagou.message.api.MessageRemoteService;
import com.lagou.message.api.dto.MessageDTO;
import com.lagou.message.api.dto.MessageQueryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * @author: ma wei long
 * @date:   2020年6月28日 下午2:38:53
 */
@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageRemoteService messageRemoteService;

	/**
	 * @Description: (创建商品订单)   
	 * @author: ma wei long
	 * @date:   2020年6月18日 下午7:40:30   
	*/
	@Override
	public DataGrid<MessageVo> getMessageList(MessageQueryVo param) {
		ResponseDTO<DataGrid<MessageDTO>> resp = messageRemoteService.getMessageList(ConvertUtil.convert(param, MessageQueryDTO.class));
		log.info("getMessageList - messageRemoteService.getMessageList - param:{} resp：{}",JSON.toJSONString(param),JSON.toJSONString(resp));
		ValidateUtils.isTrue(resp.isSuccess(), resp.getState(),resp.getMessage());
		
		DataGrid<MessageVo> res = new DataGrid<MessageVo>();
		res.setRows(ConvertUtil.convertList(resp.getContent().getRows(), MessageVo.class));
		res.setTotal(resp.getContent().getTotal());
		return res;
	}

	/**
	 * @author: ma wei long
	 * @date:   2020年6月29日 上午11:19:55   
	*/
	@Override
	public Boolean updateReadStatus(Integer userId) {
		ResponseDTO<Boolean> resp = messageRemoteService.updateReadStatus(userId);
		log.info("updateReadStatus - messageRemoteService.updateReadStatus - param:{} resp：{}",userId,JSON.toJSONString(resp));
		ValidateUtils.isTrue(resp.isSuccess(), resp.getState(),resp.getMessage());
		return resp.getContent();
	}

	/**
	 * @author: ma wei long
	 * @date:   2020年7月13日 下午8:00:37   
	*/
	@Override
	public Boolean getUnReadMessageFlag(Integer userId) {
		ResponseDTO<Boolean> resp = messageRemoteService.getUnReadMessageFlag(userId);
		log.info("updateReadStatus - messageRemoteService.getUnReadMessageFlag - param:{} resp：{}",userId,JSON.toJSONString(resp));
		ValidateUtils.isTrue(resp.isSuccess(), resp.getState(),resp.getMessage());
		return resp.getContent();
	}
}
