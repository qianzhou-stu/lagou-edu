package com.lagou.user.api.feign;

import com.lagou.common.entity.vo.Result;
import com.lagou.common.response.ResponseDTO;
import com.lagou.user.api.impl.VerificationCodeRemoteServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${remote.feign.edu-user-boot.name:edu-user-boot}", path = "/user/vfcode", fallback = VerificationCodeRemoteServiceFallBack.class)
public interface VerificationCodeRemoteService {
    @RequestMapping("sendCode")
    public ResponseDTO sendCode(@RequestParam("telephone") String telephone);
    @RequestMapping("checkCode")
    public ResponseDTO checkCode(@RequestParam("telephone") String telephone,
                            @RequestParam("code") String code);

}
