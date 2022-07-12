package com.lagou.bom.order.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSON;
import com.lagou.bom.common.UserManager;
import com.lagou.bom.order.service.OrderService;
import com.lagou.bom.order.vo.request.CreateShopGoodsOrderReqVo;
import com.lagou.bom.order.vo.response.CreateShopGoodsOrderResVo;
import com.lagou.bom.utils.ExceptionUtil;
import com.lagou.common.response.ResponseDTO;
import com.lagou.common.util.ValidateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/order")
@Api(description = "商品下单相关接口", tags = "订单接口")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @ApiOperation("创建商品订单")
    @PostMapping("/saveOrder")
    @SentinelResource(value = "saveOrder", blockHandler = "saveOrderHandleException", blockHandlerClass = {ExceptionUtil.class})
    public ResponseDTO<CreateShopGoodsOrderResVo> saveOrder(@RequestBody CreateShopGoodsOrderReqVo reqVo, HttpServletRequest request){
        log.info("saveOrder - reqVo:{}", JSON.toJSONString(reqVo));
        ValidateUtils.notNullParam(reqVo);
        reqVo.setUserId(UserManager.getUserId());
        return ResponseDTO.success(orderService.saveOrder(reqVo));
    }

}
