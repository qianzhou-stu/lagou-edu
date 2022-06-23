package com.lagou.user.remote;

import com.lagou.common.entity.vo.Result;
import com.lagou.user.api.dto.WeixinDTO;
import com.lagou.user.api.feign.UserWeixinRemoteService;
import com.lagou.user.service.IWeixinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName WeixinController
 * @Description WeixinController
 * @Author zhouqian
 * @Date 2022/4/10 21:49
 * @Version 1.0
 */
@RestController
@RequestMapping("/user/weixin")
public class WeixinController implements UserWeixinRemoteService {

    @Autowired
    private IWeixinService weixinService;
    @Override
    public WeixinDTO getUserWeixinByUserId(@RequestParam("userId") Integer userId) {
        return weixinService.getUserWeixinByUserId(userId);
    }

    @Override
    public WeixinDTO getUserWeixinByOpenId(@RequestParam("openId") String openId) {
        return weixinService.getUserWeixinByOpenId(openId);
    }

    @Override
    public WeixinDTO getUserWeixinByUnionId(@RequestParam("unionId") String unionId) {
        return weixinService.getUserWeixinByUnionId(unionId);
    }

    @Override
    public Boolean updateUserWeixin(WeixinDTO weixinDTO) {
        return weixinService.updateUserWeixin(weixinDTO);
    }

    @Override
    public WeixinDTO saveUserWeixin(@RequestBody WeixinDTO weixinDTO) {
        return weixinService.saveUserWeixin(weixinDTO);
    }



    @Override
    public Result<WeixinDTO> bindUserWeixin(@RequestBody WeixinDTO weixinDTO) {
        return weixinService.bindUserWeixin(weixinDTO);
    }

    @Override
    public boolean unBindUserWeixin(@RequestParam("userId") Integer userId) {
        return weixinService.unBindUserWeixin(userId);
    }
}
