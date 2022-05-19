package com.lagou.ad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.ad.api.dto.PromotionAdDTO;
import com.lagou.ad.api.dto.PromotionSpaceDTO;
import com.lagou.ad.entity.PromotionAd;
import com.lagou.ad.entity.PromotionSpace;
import com.lagou.ad.mapper.PromotionAdMapper;
import com.lagou.ad.mapper.PromotionSpaceMapper;
import com.lagou.ad.service.IPromotionSpaceService;
import com.lagou.common.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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
public class PromotionSpaceServiceImpl extends ServiceImpl<PromotionSpaceMapper, PromotionSpace> implements IPromotionSpaceService {
    @Autowired
    private PromotionSpaceMapper promotionSpaceMapper;
    @Autowired
    private PromotionAdMapper promotionAdMapper;

    @Override
    public List<PromotionSpace> getAllSpace() {
        QueryWrapper<PromotionSpace> queryWrapper = new QueryWrapper<>();
        List<PromotionSpace> promotionSpaces = promotionSpaceMapper.selectList(queryWrapper);
        return promotionSpaces;
    }

    @Override
    public List<PromotionSpaceDTO> getAdBySpaceKey(String[] spaceKey) {
        List<PromotionSpaceDTO> promotionSpaceDTOS = new ArrayList<>();
        for (String s : spaceKey) {
            // 获取spaceKey对应的广告位
            QueryWrapper<PromotionSpace> queryWrapper = new QueryWrapper();
            queryWrapper.eq("spaceKey",spaceKey);
            PromotionSpace promotionSpace = promotionSpaceMapper.selectOne(queryWrapper);
            // 获取该space对应的所有广告
            QueryWrapper<PromotionAd> adQueryWrapper = new QueryWrapper<>();
            adQueryWrapper.eq("spaceId", promotionSpace.getId());
            // 状态为上线状态
            adQueryWrapper.eq("status",1);
            // 有效期内
            Date date = new Date();
            adQueryWrapper.lt("startTime",date);
            adQueryWrapper.gt("endTime",date);
            List<PromotionAd> promotionAds = promotionAdMapper.selectList(adQueryWrapper);
            // 属性拷贝
            PromotionSpaceDTO promotionSpaceDTO = ConvertUtil.convert(promotionSpace, PromotionSpaceDTO.class);
            List<PromotionAdDTO> promotionAdDTOS = ConvertUtil.convertList(promotionAds, PromotionAdDTO.class);
            promotionSpaceDTO.setPromotionAdDTOS(promotionAdDTOS);
            promotionSpaceDTOS.add(promotionSpaceDTO);
        }
        return promotionSpaceDTOS;
    }

    @Override
    public PromotionSpaceDTO getSpaceById(Integer id) {
        QueryWrapper<PromotionSpace> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        PromotionSpace promotionSpace = promotionSpaceMapper.selectOne(queryWrapper);
        PromotionSpaceDTO promotionSpaceDTO = ConvertUtil.convert(promotionSpace, PromotionSpaceDTO.class);
        return promotionSpaceDTO;
    }

    @Override
    public void saveOrUpdateSpace(PromotionSpaceDTO spaceDTO) {
        PromotionSpace promotionSpace = ConvertUtil.convert(spaceDTO, PromotionSpace.class);
        if (promotionSpace.getId()== null){
            Date date = new Date();
            promotionSpace.setCreateTime(date);
            promotionSpace.setUpdateTime(date);
            promotionSpace.setIsDel(0);
            promotionSpaceMapper.insert(promotionSpace);
        }else{
            promotionSpace.setUpdateTime(new Date());
            promotionSpaceMapper.updateById(promotionSpace);
        }
        return;
    }
}
