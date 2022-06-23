package com.lagou.user.service;

import com.lagou.common.entity.vo.Result;
import com.lagou.user.api.dto.WeixinDTO;
import com.lagou.user.entity.Weixin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouqian
 * @since 2022-04-05
 */
public interface IWeixinService extends IService<Weixin> {

    WeixinDTO getUserWeixinByUserId(Integer userId);

    WeixinDTO getUserWeixinByOpenId(String openId);


    WeixinDTO getUserWeixinByUnionId(String unionId);

    Boolean updateUserWeixin(WeixinDTO weixinDTO);

    WeixinDTO saveUserWeixin(WeixinDTO weixinDTO);

    Result<WeixinDTO> bindUserWeixin(WeixinDTO weixinDTO);

    boolean unBindUserWeixin(Integer userId);
}
