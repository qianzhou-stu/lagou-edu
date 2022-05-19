package com.lagou.ad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.ad.api.dto.PromotionSpaceDTO;
import com.lagou.ad.entity.PromotionSpace;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouqian
 * @since 2022-04-03
 */
public interface IPromotionSpaceService extends IService<PromotionSpace> {

    List<PromotionSpace> getAllSpace();

    List<PromotionSpaceDTO> getAdBySpaceKey(String[] spaceKey);

    PromotionSpaceDTO getSpaceById(Integer id);

    void saveOrUpdateSpace(PromotionSpaceDTO spaceDTO);
}
