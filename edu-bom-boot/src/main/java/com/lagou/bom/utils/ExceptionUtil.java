package com.lagou.bom.utils;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.lagou.common.response.ResponseDTO;
import com.lagou.common.result.ResultCode;
import com.lagou.order.api.dto.CreateShopGoodsOrderReqDTO;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public final class ExceptionUtil {

    public static ResponseDTO<String> saveOrderHandleException(CreateShopGoodsOrderReqDTO reqDTO, HttpServletRequest request, BlockException ex) {
        log.error("提交课程订单 saveOrderHandleException :{}", ex.getClass().getCanonicalName(),ex);
        return ResponseDTO.ofError(ResultCode.FLOW_SENTINEL_ERROR.getState(), ResultCode.FLOW_SENTINEL_ERROR.getMessage());
    }

    public static ResponseDTO<String> testHandlerException(BlockException ex){
        log.error("testHandleException :{}", ex.getClass().getCanonicalName(),ex);
        return ResponseDTO.ofError(ResultCode.FLOW_SENTINEL_ERROR.getState(), ResultCode.FLOW_SENTINEL_ERROR.getMessage());
    }
}
