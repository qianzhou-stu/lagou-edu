package com.lagou.bom.user.controller;

import com.lagou.common.entity.vo.Result;
import com.lagou.common.response.ResponseDTO;
import com.lagou.user.api.feign.VerificationCodeRemoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user/vfcode")
@Api(tags = "验证码接口", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
public class VerificationCodeController {
    @Autowired
    private VerificationCodeRemoteService verificationCodeRemoteService;

    /**
     * 发送验证码
     * @param telephone
     * @return
     */
    @ApiOperation(value = "发送验证码", produces = MimeTypeUtils.APPLICATION_JSON_VALUE, notes = "发送验证码接口")
    @PostMapping("sendCode")
    public Result sendCode(@ApiParam(value = "电话号码", name = "telephone") String telephone){
        return this.verificationCodeRemoteService.sendCode(telephone);
    }

    /**
     * 判断验证码是否正确
     *
     * @return
     */
    @ApiOperation(value = "判断验证码", produces = MimeTypeUtils.APPLICATION_JSON_VALUE, notes = "判断验证码是否正确接口")
    @PostMapping("checkCode")
    public Result checkCode(@ApiParam(value = "电话号码", name = "telephone") String telephone,
                                 @ApiParam(value = "验证码", name = "code") String code) {

        return this.verificationCodeRemoteService.checkCode(telephone, code);
    }
}
