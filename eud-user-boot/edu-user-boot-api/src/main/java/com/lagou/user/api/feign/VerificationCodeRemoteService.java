package com.lagou.user.api.feign;

import com.lagou.common.entity.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "edu-user-boot", path = "/user/vfcode")
public interface VerificationCodeRemoteService {
    @RequestMapping("sendCode")
    public Result sendCode(@RequestParam("telephone") String telephone);
    @RequestMapping("checkCode")
    public Result checkCode(@RequestParam("telephone") String telephone,
                            @RequestParam("code") String code);

}
