package com.lagou.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.common.entity.vo.Result;
import com.lagou.common.result.ResultCode;
import com.lagou.common.util.ConvertUtil;
import com.lagou.user.api.dto.WeixinDTO;
import com.lagou.user.entity.Weixin;
import com.lagou.user.mapper.WeixinMapper;
import com.lagou.user.service.IWeixinService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhouqian
 * @since 2022-04-05
 */
@Service
@Slf4j
public class WeixinServiceImpl extends ServiceImpl<WeixinMapper, Weixin> implements IWeixinService {


    @Autowired
    private WeixinMapper weixinMapper;
    @Autowired
    private IWeixinService weixinService;

    @Override
    public WeixinDTO getUserWeixinByUserId(Integer userId) {
        LambdaQueryWrapper<Weixin> wrapper = new LambdaQueryWrapper<Weixin>().eq(Weixin::getUserId, userId).eq(Weixin::getIsDel, false);
        List<Weixin> weixins = weixinMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(weixins)) {
            return null;
        }
        return ConvertUtil.convert(weixins.get(0), WeixinDTO.class);
    }

    @Override
    public WeixinDTO getUserWeixinByOpenId(String openId) {
        LambdaQueryWrapper<Weixin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Weixin::getOpenId, openId);
        lambdaQueryWrapper.eq(Weixin::getIsDel, Boolean.FALSE);
        List<Weixin> weixins = weixinMapper.selectList(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(weixins)){
            return null;
        }
        return ConvertUtil.convert(weixins.get(0), WeixinDTO.class);
    }

    @Override
    public WeixinDTO getUserWeixinByUnionId(String unionId) {
        LambdaQueryWrapper<Weixin> wrapper = new QueryWrapper<Weixin>().lambda()
                .eq(Weixin::getUnionId, unionId)
                .eq(Weixin::getIsDel, false);
        List<Weixin> weixins = weixinMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(weixins)) {
            return null;
        }
        return ConvertUtil.convert(weixins.get(0), WeixinDTO.class);
    }

    @Override
    public Boolean updateUserWeixin(WeixinDTO weixinDTO) {
        if (Objects.isNull(weixinDTO) || weixinDTO.getId() == null){
            return false;
        }
        Weixin weixin = ConvertUtil.convert(weixinDTO, Weixin.class);
        assert weixin != null;
        weixin.setUpdateTime(LocalDateTime.now());
        weixin.setIsDel(Boolean.FALSE);
        int result = this.weixinMapper.updateById(weixin);
        log.info("微信绑定成功,微信:{}, 结果:{}", weixin, result > 0 ? Boolean.TRUE: Boolean.FALSE);
        return result > 0;
    }

    @Override
    public WeixinDTO saveUserWeixin(WeixinDTO weixinDTO) {
        Weixin weixin = ConvertUtil.convert(weixinDTO, Weixin.class);
        assert weixin != null;
        weixin.setCreateTime(LocalDateTime.now());
        weixin.setUpdateTime(LocalDateTime.now());
        weixin.setIsDel(false);
        weixin.setId(null);
        int result = this.weixinMapper.insert(weixin);
        log.info("微信绑定成功,微信:{}, 结果:{}", weixin, result > 0 ? Boolean.TRUE: Boolean.FALSE);
        return weixinDTO;
    }

    @Override
    public Result bindUserWeixin(WeixinDTO weixinDTO) {
        LambdaQueryWrapper<Weixin> wrapper = new QueryWrapper<Weixin>().lambda()
                .eq(Weixin::getUserId, weixinDTO.getUserId())
                .eq(Weixin::getUnionId, weixinDTO.getUnionId())
                .eq(Weixin::getIsDel, false);
        List<Weixin> weixins = this.weixinMapper.selectList(wrapper);
        if (CollectionUtils.isNotEmpty(weixins)) {
            log.info("userId:{}, unionId:{}, openId:{} 已绑定，不用处理 ", weixinDTO.getUserId(), weixinDTO.getUnionId(), weixinDTO.getOpenId());
            return Result.fail(ResultCode.ALREADY_BIND);
        }
        // 该用户已绑定其他unionId
        Weixin userIdWeixin = this.weixinService.getBaseMapper().selectOne(new QueryWrapper<Weixin>().lambda()
                .eq(Weixin::getUserId, weixinDTO.getUserId()).eq(Weixin::getIsDel, false).orderByDesc(Weixin::getId));
        if (null != userIdWeixin && !StringUtils.equals(weixinDTO.getUnionId(), userIdWeixin.getUnionId())) {
            log.info("userId:{}, unionId:{}, openId:{} 该用户已绑定其他unionId ", weixinDTO.getUserId(), weixinDTO.getUnionId(), weixinDTO.getOpenId());
            return Result.fail(ResultCode.ALREADY_BIND_UNIONID);
        }
        // 该unionId已绑定其他userId
        Weixin unionIdWeixin = this.weixinService.getBaseMapper().selectOne(new QueryWrapper<Weixin>().lambda()
                .eq(Weixin::getUnionId, weixinDTO.getUnionId()).eq(Weixin::getIsDel, false).orderByDesc(Weixin::getId));
        if (null != unionIdWeixin && null != weixinDTO.getUnionId() && !weixinDTO.getUserId().equals(unionIdWeixin.getUserId())) {
            log.info("userId:{}, unionId:{}, openId:{} 该unionId已绑定其他用户 ", weixinDTO.getUserId(), weixinDTO.getUnionId(), weixinDTO.getOpenId());
            return Result.fail(ResultCode.ALREADY_BIND_USERID);
        }
        // 开始真正绑定
        Weixin weixin = ConvertUtil.convert(weixinDTO, Weixin.class);
        assert weixin != null;
        weixin.setCreateTime(LocalDateTime.now());
        weixin.setUpdateTime(LocalDateTime.now());
        weixin.setIsDel(false);
        weixin.setId(null);
        boolean result = this.weixinService.save(weixin);
        log.info("微信绑定成功,微信:{}, 结果:{}", weixins, result);
        WeixinDTO dto = ConvertUtil.convert(weixin, WeixinDTO.class);
        return Result.success(dto);
    }

    @Override
    public boolean unBindUserWeixin(Integer userId) {
        LambdaQueryWrapper<Weixin> wrapper = new QueryWrapper<Weixin>().lambda()
                .eq(Weixin::getUserId, userId)
                .eq(Weixin::getIsDel, false);
        List<Weixin> weixins = this.weixinService.getBaseMapper().selectList(wrapper);
        if (CollectionUtils.isEmpty(weixins)){
            return true;
        }
        weixins.forEach(weixin -> {
            weixin.setIsDel(true);
            weixin.setUpdateTime(LocalDateTime.now());
            weixinService.updateById(weixin);
            log.info("用户[{}]已解绑微信[{}]", userId, weixin.getOpenId());
        });
        return true;
    }
}
