package com.lagou.user.api.impl;

import com.lagou.common.entity.vo.Result;
import com.lagou.user.api.dto.WeixinDTO;
import com.lagou.user.api.feign.UserWeixinRemoteService;

public class UserWeixinRemoteServiceFallBack implements UserWeixinRemoteService {


    @Override
    public WeixinDTO getUserWeixinByUserId(Integer userId) {
        return null;
    }

    @Override
    public WeixinDTO getUserWeixinByOpenId(String openId) {
        return null;
    }

    @Override
    public WeixinDTO getUserWeixinByUnionId(String unionId) {
        return null;
    }

    @Override
    public WeixinDTO saveUserWeixin(WeixinDTO weixinDTO) {
        return null;
    }

    @Override
    public Boolean updateUserWeixin(WeixinDTO weixinDTO) {
        return null;
    }

    @Override
    public Result<WeixinDTO> bindUserWeixin(WeixinDTO weixinDTO) {
        return null;
    }

    @Override
    public boolean unBindUserWeixin(Integer userId) {
        return false;
    }
}
