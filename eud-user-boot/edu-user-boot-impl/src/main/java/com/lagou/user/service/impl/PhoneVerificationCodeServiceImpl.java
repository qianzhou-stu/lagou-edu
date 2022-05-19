package com.lagou.user.service.impl;

import com.lagou.user.entity.PhoneVerificationCode;
import com.lagou.user.mapper.PhoneVerificationCodeMapper;
import com.lagou.user.service.IPhoneVerificationCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhouqian
 * @since 2022-04-05
 */
@Service
public class PhoneVerificationCodeServiceImpl extends ServiceImpl<PhoneVerificationCodeMapper, PhoneVerificationCode> implements IPhoneVerificationCodeService {

}
