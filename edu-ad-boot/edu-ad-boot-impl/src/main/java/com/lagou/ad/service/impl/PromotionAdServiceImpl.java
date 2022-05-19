package com.lagou.ad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.ad.api.dto.PromotionAdDTO;
import com.lagou.ad.entity.PromotionAd;
import com.lagou.ad.mapper.PromotionAdMapper;
import com.lagou.ad.service.IPromotionAdService;
import com.lagou.common.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhouqian
 * @since 2022-04-03
 */
@Service
public class PromotionAdServiceImpl extends ServiceImpl<PromotionAdMapper, PromotionAd> implements IPromotionAdService {

    @Autowired
    private PromotionAdMapper promotionAdMapper;

    @Override
    public List<PromotionAdDTO> getAllAds() {
        QueryWrapper<PromotionAd> queryWrapper = new QueryWrapper<>();
        List<PromotionAd> promotionAds = promotionAdMapper.selectList(queryWrapper);
        List<PromotionAdDTO> promotionAdDTOS = ConvertUtil.convertList(promotionAds, PromotionAdDTO.class);
        return promotionAdDTOS;
    }

    @Override
    public PromotionAdDTO getAdById(Integer id) {
        QueryWrapper<PromotionAd> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        PromotionAd promotionAd = promotionAdMapper.selectOne(queryWrapper);
        PromotionAdDTO promotionAdDTO = ConvertUtil.convert(promotionAd, PromotionAdDTO.class);
        return promotionAdDTO;
    }

    @Override
    public void saveOrUpdateAd(PromotionAdDTO promotionAdDTO) {
        PromotionAd promotionAd = ConvertUtil.convert(promotionAdDTO, PromotionAd.class);
        if (promotionAd.getId() == null){
            promotionAd.setStatus(1);
            Date date = new Date();
            promotionAd.setCreateTime(date);
            promotionAd.setUpdateTime(date);
            promotionAdMapper.insert(promotionAd);
        }else {
            promotionAd.setUpdateTime(new Date());
            promotionAdMapper.updateById(promotionAd);
        }
        return;
    }
}
