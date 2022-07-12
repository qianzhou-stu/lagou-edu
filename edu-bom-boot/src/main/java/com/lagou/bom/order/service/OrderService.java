package com.lagou.bom.order.service;

import com.lagou.bom.order.vo.request.CreateShopGoodsOrderReqVo;
import com.lagou.bom.order.vo.response.CreateShopGoodsOrderResVo;

public interface OrderService {

    CreateShopGoodsOrderResVo saveOrder(CreateShopGoodsOrderReqVo reqVo);
}
