package com.lagou.ad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.ad.api.dto.PromotionAdDTO;
import com.lagou.ad.entity.PromotionAd;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouqian
 * @since 2022-04-03
 */
public interface IPromotionAdService extends IService<PromotionAd> {

    List<PromotionAdDTO> getAllAds();

    PromotionAdDTO getAdById(Integer id);

    void saveOrUpdateAd(PromotionAdDTO promotionAdDTO);
}
