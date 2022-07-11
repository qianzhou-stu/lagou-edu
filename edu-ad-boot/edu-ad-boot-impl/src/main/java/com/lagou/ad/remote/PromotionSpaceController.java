package com.lagou.ad.remote;

import com.lagou.ad.api.dto.PromotionAdDTO;
import com.lagou.ad.api.dto.PromotionSpaceDTO;
import com.lagou.ad.api.feign.AdRemoteService;
import com.lagou.ad.entity.PromotionSpace;
import com.lagou.ad.service.IPromotionAdService;
import com.lagou.ad.service.IPromotionSpaceService;
import com.lagou.common.response.ResponseDTO;
import com.lagou.common.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName PromotionSpaceController
 * @Description TODO
 * @Author zhouqian
 * @Date 2022/4/4 10:23
 * @Version 1.0
 */
@RestController
@RequestMapping("/ad")
public class PromotionSpaceController implements AdRemoteService {
    @Autowired
    private IPromotionSpaceService promotionSpaceService;
    @Autowired
    private IPromotionAdService promotionAdService;

    @Override
    @GetMapping("/space/getAllSpaces")
    public List<PromotionSpaceDTO> getAllSpaces() {
        List<PromotionSpace> promotionSpaces = promotionSpaceService.getAllSpace();
        return ConvertUtil.convertList(promotionSpaces,PromotionSpaceDTO.class);
    }

    @Override
    @GetMapping("/space/getAdBySpaceKey")
    public List<PromotionSpaceDTO> getAdBySpaceKey(@RequestBody String[] spaceKey) {
        List<PromotionSpaceDTO> promotionSpaceDTOS =  promotionSpaceService.getAdBySpaceKey(spaceKey);
        return promotionSpaceDTOS;
    }

    @Override
    @GetMapping("/space/getSpaceById")
    public PromotionSpaceDTO getSpaceById(@RequestParam("id") Integer id) {
        PromotionSpaceDTO promotionSpaceDTO = promotionSpaceService.getSpaceById(id);
        return promotionSpaceDTO;
    }

    @Override
    @PostMapping("/space/saveOrUpdate")
    public void saveOrUpdateSpace(@RequestBody PromotionSpaceDTO spaceDTO) {
        promotionSpaceService.saveOrUpdateSpace(spaceDTO);
        return;
    }

    @Override
    @GetMapping("/getAllAds")
    public List<PromotionAdDTO> getAllAds() {
        List<PromotionAdDTO> promotionAdDTOS = promotionAdService.getAllAds();
        return promotionAdDTOS;
    }

    @Override
    @GetMapping("/getAdById")
    public PromotionAdDTO getAdById(@RequestParam("id") Integer id) {
        PromotionAdDTO promotionAdDTO = promotionAdService.getAdById(id);
        return promotionAdDTO;
    }

    @Override
    @PostMapping("/saveOrUpdate")
    public void saveOrUpdateAd(@RequestBody PromotionAdDTO promotionAdDTO) {
        promotionAdService.saveOrUpdateAd(promotionAdDTO);
        return;
    }

    @Override
    public ResponseDTO updateStatus(Integer id, Integer status) {
        // 更新广告状态
        promotionAdService.updateStatus(id, status);
        return ResponseDTO.success("更新状态成功");
    }
}
