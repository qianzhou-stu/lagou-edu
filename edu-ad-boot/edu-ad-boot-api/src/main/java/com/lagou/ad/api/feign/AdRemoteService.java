package com.lagou.ad.api.feign;

import com.lagou.ad.api.dto.PromotionAdDTO;
import com.lagou.ad.api.dto.PromotionSpaceDTO;
import com.lagou.common.response.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName AdRemoteService
 * @Description AdRemoteService接口
 * @Author zhouqian
 * @Date 2022/4/4 10:17
 * @Version 1.0
 */
@FeignClient(name = "${remote.feign.edu-ad-boot.name:edu-ad-boot}", path = "/ad")
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
    // 更新状态的值
    @RequestMapping("/updateStatus")
    ResponseDTO updateStatus(@RequestParam("id") Integer id, @RequestParam("status") Integer status);
}
