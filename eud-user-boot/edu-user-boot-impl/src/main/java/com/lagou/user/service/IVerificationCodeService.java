package com.lagou.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.user.entity.VerificationCode;

public interface IVerificationCodeService extends IService<VerificationCode> {

    boolean save(String telephone);

    boolean checkCode(String telephone, String code);
}
