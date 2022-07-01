package com.lagou.front.ad.controller;

import com.lagou.ad.api.dto.PromotionAdDTO;
import com.lagou.ad.api.dto.PromotionSpaceDTO;
import com.lagou.ad.api.feign.AdRemoteService;
import com.lagou.common.response.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName AdController
 * @Description AdController前端控制器
 * @Author zhouqian
 * @Date 2022/4/4 10:37
 * @Version 1.0
 */
@RestController
@RequestMapping("/ad")
public class AdController {
    @Autowired
    private AdRemoteService adRemoteService;

    @RequestMapping("/space/getAllSpaces")
    public ResponseDTO getAllSpace() {
        List<PromotionSpaceDTO> dtoList = adRemoteService.getAllSpaces();
        return ResponseDTO.success(dtoList);
    }

    @RequestMapping("/space/getAdBySpaceKey")
    public ResponseDTO getAdBySpaceKey(@RequestParam("spaceKey") String[] spaceKey) {
        List<PromotionSpaceDTO> promotionSpaceDTOS = adRemoteService.getAdBySpaceKey(spaceKey);
        return ResponseDTO.success(promotionSpaceDTOS);
    }

    @RequestMapping("/space/getSpaceById")
    public ResponseDTO getSpaceById(@RequestParam("id") Integer id) {
        PromotionSpaceDTO promotionSpaceDTO = adRemoteService.getSpaceById(id);
        return ResponseDTO.success(promotionSpaceDTO);
    }

    @PostMapping("/space/saveOrUpdate")
    public ResponseDTO saveOrUpdateSpace(@RequestBody PromotionSpaceDTO spaceDTO) {
        adRemoteService.saveOrUpdateSpace(spaceDTO);
        return ResponseDTO.success();
    }

    @GetMapping("/getAllAds")
    public ResponseDTO getAllAds() {
        List<PromotionAdDTO> promotionAdDTOS = adRemoteService.getAllAds();
        return ResponseDTO.success(promotionAdDTOS);
    }

    @GetMapping("/getAdById")
    public ResponseDTO getAdById(@RequestParam("id") Integer id) {
        PromotionAdDTO promotionAdDTO = adRemoteService.getAdById(id);
        return ResponseDTO.success(promotionAdDTO);
    }

    @PostMapping("/saveOrUpdate")
    public ResponseDTO saveOrUpdateAd(@RequestBody PromotionAdDTO promotionAdDTO) {
        adRemoteService.saveOrUpdateAd(promotionAdDTO);
        return ResponseDTO.success();
    }
}
