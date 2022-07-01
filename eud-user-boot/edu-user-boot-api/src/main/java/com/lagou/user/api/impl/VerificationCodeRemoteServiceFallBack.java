package com.lagou.user.api.impl;

import com.lagou.common.entity.vo.Result;
import com.lagou.user.api.feign.VerificationCodeRemoteService;

public class VerificationCodeRemoteServiceFallBack implements VerificationCodeRemoteService {
    @Override
    public Result sendCode(String telephone) {
        return null;
    }

    @Override
    public Result checkCode(String telephone, String code) {
        return null;
    }
}
