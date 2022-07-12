package com.lagou.bom.order.service;

import com.alibaba.fastjson.JSON;
import com.lagou.bom.order.vo.request.CreateShopGoodsOrderReqVo;
import com.lagou.bom.order.vo.response.CreateShopGoodsOrderResVo;
import com.lagou.common.response.ResponseDTO;
import com.lagou.common.util.ConvertUtil;
import com.lagou.common.util.ValidateUtils;
import com.lagou.order.api.UserCourseOrderRemoteService;
import com.lagou.order.api.dto.CreateShopGoodsOrderReqDTO;
import com.lagou.order.api.dto.UserCourseOrderResDTO;
import com.lagou.order.api.enums.UserCourseOrderSourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.klock.annotation.Klock;
import org.springframework.boot.autoconfigure.klock.model.LockTimeoutStrategy;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@Slf4j
public class OrderServiceImpl implements OrderService{

    @Autowired
    private UserCourseOrderRemoteService userCourseOrderRemoteService;
    /**
     * 创建商品订单
     * @param reqVo
     * @return CreateShopGoodsOrderResVo
     */
    @Override
    @Klock(keys = {"#reqVo.userId", "#reqVo.goodsId"}, waitTime = 0, leaseTime = 120, lockTimeoutStrategy = LockTimeoutStrategy.FAIL_FAST)
    public CreateShopGoodsOrderResVo saveOrder(CreateShopGoodsOrderReqVo reqVo) {
        ValidateUtils.notNullParam(reqVo);
        ValidateUtils.notNullParam(reqVo.getGoodsId());
        CreateShopGoodsOrderReqDTO createShopGoodsOrderReqDTO = ConvertUtil.convert(reqVo, CreateShopGoodsOrderReqDTO.class);
        Objects.requireNonNull(createShopGoodsOrderReqDTO).setSourceType(UserCourseOrderSourceType.USER_BUY);
        ResponseDTO<UserCourseOrderResDTO> resp = userCourseOrderRemoteService.saveOrder(createShopGoodsOrderReqDTO);
        log.info("saveOrder - userCourseOrderRemoteService.saveOrder - req:{} resp:{}", JSON.toJSONString(reqVo), JSON.toJSONString(resp));
        ValidateUtils.isTrue(resp.isSuccess(), resp.getState(), resp.getMessage());
        return ConvertUtil.convert(resp.getContent(), CreateShopGoodsOrderResVo.class);
    }
}
