package com.lagou.ad.api.feign;

import com.lagou.ad.api.dto.PromotionAdDTO;
import com.lagou.ad.api.dto.PromotionSpaceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName AdRemoteService
 * @Description AdRemoteService接口
 * @Author zhouqian
 * @Date 2022/4/4 10:17
 * @Version 1.0
 */
@FeignClient(name = "edu-ad-boot", path = "/ad")
public interface AdRemoteService {
    // 获取所有的广告位
    @GetMapping("/space/getAllSpaces")
    List<PromotionSpaceDTO> getAllSpaces();
    // 根据spaceKey获取广告
    @GetMapping("/space/getAdBySpaceKey")
    List<PromotionSpaceDTO> getAdBySpaceKey(@RequestBody String[] spaceKey);
    // 根据Id获取广告位
    @GetMapping("/space/getSpaceById")
    PromotionSpaceDTO getSpaceById(@RequestParam("id") Integer id);
    // 编辑或保存广告位
    @PostMapping("/space/saveOrUpdate")
    void saveOrUpdateSpace(@RequestBody PromotionSpaceDTO spaceDTO);
    // 获取广告位列表
    @GetMapping("/getAllAds")
    List<PromotionAdDTO> getAllAds();
    // 根据id获取
    @GetMapping("/getAdById")
    PromotionAdDTO getAdById(@RequestParam("id") Integer id);
    // 编辑或保存广告
    @PostMapping("/saveOrUpdate")
    void saveOrUpdateAd(@RequestBody PromotionAdDTO promotionAdDTO);
}
