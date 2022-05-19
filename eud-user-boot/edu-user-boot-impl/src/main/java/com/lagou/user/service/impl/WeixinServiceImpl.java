package com.lagou.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lagou.common.util.ConvertUtil;
import com.lagou.user.api.dto.WeixinDTO;
import com.lagou.user.entity.Weixin;
import com.lagou.user.mapper.WeixinMapper;
import com.lagou.user.service.IWeixinService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhouqian
 * @since 2022-04-05
 */
@Service
public class WeixinServiceImpl extends ServiceImpl<WeixinMapper, Weixin> implements IWeixinService {


    @Autowired
    private WeixinMapper weixinMapper;

    @Override
    public WeixinDTO getUserWeixinByUserId(Integer userId) {
        LambdaQueryWrapper<Weixin> wrapper = new LambdaQueryWrapper<Weixin>().eq(Weixin::getUserId, userId).eq(Weixin::getIsDel, false);
        List<Weixin> weixins = weixinMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(weixins)) {
            return null;
        }
        return ConvertUtil.convert(weixins.get(0), WeixinDTO.class);
    }


}
