package com.lagou.order.api;

import com.lagou.common.response.ResponseDTO;
import com.lagou.order.api.dto.CreateShopGoodsOrderReqDTO;
import com.lagou.order.api.dto.UserCourseOrderDTO;
import com.lagou.order.api.dto.UserCourseOrderResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "${remote.feign.edu-order-boot.name:edu-order-boot}", path = "/userCourseOrder")
public interface UserCourseOrderRemoteService {
    /**
     * 创建订单 保存支付订单
     *
     * @param reqDTO
     * @return ResponseDTO<UserCourseOrderResDTO>
     */
    @PostMapping("/saveOrder")
    public ResponseDTO<UserCourseOrderResDTO> saveOrder(@RequestBody CreateShopGoodsOrderReqDTO reqDTO);

    /**
     * 根据订单号码获取订单信息
     *
     * @param orderNo
     * @return ResponseDTO<UserCourseOrderDTO>
     */
    @GetMapping("/getCourseOrderByOrderNo")
    public ResponseDTO<UserCourseOrderDTO> getCourseOrderByOrderNo(@RequestParam("orderNo") String orderNo);

    /**
     * 更新商品订单状态
     *
     * @param orderNo
     * @param status
     * @return ResponseDTO<?>
     */
    @PostMapping("/updateOrderStatus")
    public ResponseDTO<?> updateOrderStatus(@RequestParam("orderNo") String orderNo, @RequestParam("status") Integer status);

    /**
     * 根据用户的id查询商品订单
     *
     * @param userId
     * @return ResponseDTO<List<UserCourseOrderDTO>>
     */
    @GetMapping("/getUserCourseOrderByUserId")
    public ResponseDTO<List<UserCourseOrderDTO>> getUserCourseOrderByUserId(@RequestParam("userId") Integer userId);

    /**
     * 根据用户&课程id统计订单数量
     *
     * @param userId
     * @param courseIds
     * @return
     */
    @GetMapping("/countUserCourseOrderByCourseIds")
    public ResponseDTO<Integer> countUserCourseOrderByCourseIds(@RequestParam("userId") Integer userId, @RequestParam("courseIds") List<Integer> courseIds);


    /**
     * 根据课程id统计支付成功订单数量
     *
     * @param courseId
     * @return
     */
    @GetMapping("/countUserCourseOrderByCourseId")
    public ResponseDTO<Integer> countUserCourseOrderByCourseId(@RequestParam("courseId") Integer courseId);

    /**
     * 根据课程id查询支付成功订单集合
     *
     * @param courseId
     * @return
     */
    @GetMapping("/getOrderListByCourseId")
    public ResponseDTO<List<UserCourseOrderDTO>> getOrderListByCourseId(@RequestParam("courseId") Integer courseId);
}
