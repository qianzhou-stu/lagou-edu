package com.lagou.user.api.impl;

import com.lagou.common.entity.vo.Result;
import com.lagou.common.response.ResponseDTO;
import com.lagou.user.api.feign.VerificationCodeRemoteService;

public class VerificationCodeRemoteServiceFallBack implements VerificationCodeRemoteService {
    @Override
    public ResponseDTO sendCode(String telephone) {
        return null;
    }

    @Override
    public ResponseDTO checkCode(String telephone, String code) {
        return null;
    }
}
