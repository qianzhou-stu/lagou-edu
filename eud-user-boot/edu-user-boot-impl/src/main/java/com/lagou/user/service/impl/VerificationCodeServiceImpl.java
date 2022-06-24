package com.lagou.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.lagou.common.date.DateUtil;
import com.lagou.common.random.RandomUtil;
import com.lagou.user.entity.VerificationCode;
import com.lagou.user.exception.ExpireCodeRuntimeException;
import com.lagou.user.exception.IncorrectCodeRuntimteException;
import com.lagou.user.mapper.VerificationCodeMapper;
import com.lagou.user.service.IVerificationCodeService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author leo
 * @since 2020-06-28
 */
@Service
public class VerificationCodeServiceImpl extends ServiceImpl<VerificationCodeMapper, VerificationCode> implements IVerificationCodeService {

    @Override
    public boolean save(String telephone) {

        //获取动态验证码
        String randomNumber = RandomUtil.getRandomNumber(6);
        //发送验证码
        System.out.println("发送验证码");

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setPhone(telephone);   //设置电话号码
        verificationCode.setVerificationCode(randomNumber);
        verificationCode.setCreateTime(new Date());
        verificationCode.setIsCheck(false);
        verificationCode.setCheckTimes(0);

        //保存验证码
        boolean res = this.save(verificationCode);
        return res;
    }

    @Override
    public boolean checkCode(String telephone, String code) {

        //判断验证码是否存在
        QueryWrapper<VerificationCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",telephone);
        queryWrapper.eq("verification_code",code);
        VerificationCode verificationCode = this.getBaseMapper().selectOne(queryWrapper);

        if(verificationCode == null){
            throw new IncorrectCodeRuntimteException("验证码错误");
        }
        //判断验证码是否过期
        Date now = new Date();
        Date expireDate = DateUtil.rollByMinutes(verificationCode.getCreateTime(),5);
        if(now.getTime() > expireDate.getTime()){
            throw new ExpireCodeRuntimeException("验证码失效,重新发送");
        }

        verificationCode.setIsCheck(true);
        int checkTimes = verificationCode.getCheckTimes()+1;
        verificationCode.setCheckTimes(checkTimes);

        return this.updateById(verificationCode);
    }
}
