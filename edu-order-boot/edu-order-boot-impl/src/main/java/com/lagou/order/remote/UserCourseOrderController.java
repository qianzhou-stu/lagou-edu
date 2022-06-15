package com.lagou.order.remote;

import com.lagou.common.response.ResponseDTO;
import com.lagou.order.api.UserCourseOrderRemoteService;
import com.lagou.order.api.dto.CreateShopGoodsOrderReqDTO;
import com.lagou.order.api.dto.UserCourseOrderDTO;
import com.lagou.order.api.dto.UserCourseOrderResDTO;
import com.lagou.order.service.UserCourseOrderService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName UserCourseOrderController
 * @Description UserCourseOrderController控制器 实现远程统一协议接口
 * @Author zhouqian
 * @Date 2022/6/8 18:08
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/userCourseOrder")
public class UserCourseOrderController implements UserCourseOrderRemoteService {
    @Autowired
    private UserCourseOrderService userCourseOrderService;

    @Override
    @PostMapping("/saveOrder")
    public ResponseDTO<UserCourseOrderResDTO> saveOrder(@RequestBody CreateShopGoodsOrderReqDTO reqDTO) {
        UserCourseOrderResDTO userCourseOrderResDTO = userCourseOrderService.saveOrder(reqDTO);
        return ResponseDTO.success(userCourseOrderResDTO);
    }

    @Override
    @GetMapping("/getCourseOrderByOrderNo")
    public ResponseDTO<UserCourseOrderDTO> getCourseOrderByOrderNo(@RequestParam("orderNo") String orderNo) {
        UserCourseOrderDTO userCourseOrderDTO = userCourseOrderService.getCourseOrderByOrderNo(orderNo);
        return ResponseDTO.success(userCourseOrderDTO);
    }

    @Override
    @PostMapping("/updateOrderStatus")
    public ResponseDTO<?> updateOrderStatus(@RequestParam("orderNo") String orderNo, @RequestParam("status") Integer status) {
        Boolean updateBoolean = userCourseOrderService.updateOrderStatus(orderNo, status);
        return ResponseDTO.success(updateBoolean);
    }

    @Override
    @GetMapping("/getUserCourseOrderByUserId")
    public ResponseDTO<List<UserCourseOrderDTO>> getUserCourseOrderByUserId(@RequestParam("userId") Integer userId) {
        List<UserCourseOrderDTO> userCourseOrderDTOS = userCourseOrderService.getUserCourseOrderByUserId(userId);
        return ResponseDTO.success(userCourseOrderDTOS);
    }

    @Override
    @GetMapping("/countUserCourseOrderByCourseIds")
    public ResponseDTO<Integer> countUserCourseOrderByCourseIds(@RequestParam("userId") Integer userId, @RequestParam("courseIds") List<Integer> courseIds) {
        Integer count = userCourseOrderService.countUserCourseOrderByCourseIds(userId, courseIds);
        return ResponseDTO.success(count);
    }

    @Override
    @GetMapping("/countUserCourseOrderByCourseId")
    public ResponseDTO<Integer> countUserCourseOrderByCourseId(@RequestParam("courseId") Integer courseId) {
        Integer count = userCourseOrderService.countUserCourseOrderByCourseId(courseId);
        return ResponseDTO.success(count);
    }

    @Override
    @GetMapping("/getOrderListByCourseId")
    public ResponseDTO<List<UserCourseOrderDTO>> getOrderListByCourseId(@RequestParam("courseId") Integer courseId) {
        List<UserCourseOrderDTO> courseOrderDTOS = userCourseOrderService.getOrderListByCourseId(courseId);
        return ResponseDTO.success(courseOrderDTOS);
    }
}
