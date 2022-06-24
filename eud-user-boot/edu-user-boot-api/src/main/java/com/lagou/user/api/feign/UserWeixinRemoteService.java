package com.lagou.user.api.feign;

import com.lagou.common.entity.vo.Result;
import com.lagou.user.api.dto.WeixinDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "edu-user-boot-userWeixinRemoteService", path = "/user/weixin")
public interface UserWeixinRemoteService {
    @GetMapping("/getUserWeixinByUserId")
    WeixinDTO getUserWeixinByUserId(@RequestParam("userId") Integer userId);

    @GetMapping("/getUserWeixinByOpenId")
    WeixinDTO getUserWeixinByOpenId(@RequestParam("openId") String openId);

    @GetMapping("/getUserWeixinByUnionId")
    WeixinDTO getUserWeixinByUnionId(@RequestParam("unionId") String unionId);

    @PostMapping("/saveUserWeixin")
    WeixinDTO saveUserWeixin(@RequestBody WeixinDTO weixinDTO);

    @PostMapping("/updateUserWeixin")
    Boolean updateUserWeixin(WeixinDTO weixinDTO);

    @PostMapping("/bindUserWeixin")
    Result<WeixinDTO> bindUserWeixin(@RequestBody WeixinDTO weixinDTO);

    @PostMapping("/unBindUserWeixin")
    boolean unBindUserWeixin(@RequestParam("userId") Integer userId);
}
