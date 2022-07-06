package com.lagou.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.order.api.dto.CreateShopGoodsOrderReqDTO;
import com.lagou.order.api.dto.UserCourseOrderDTO;
import com.lagou.order.api.dto.UserCourseOrderResDTO;
import com.lagou.order.entity.UserCourseOrder;

import java.util.List;


/**
 * @ClassName UserCourseOrderService
 * @Description TODO
 * @Author zhouqian
 * @Date 2022/6/10 20:24
 * @Version 1.0
 */

public interface UserCourseOrderService extends IService<UserCourseOrder> {
    Integer countUserCourseOrderByCourseIds(Integer userId, List<Integer> courseIds);

    UserCourseOrderResDTO saveOrder(CreateShopGoodsOrderReqDTO reqDTO);

    UserCourseOrderDTO getCourseOrderByOrderNo(String orderNo);

    Boolean updateOrderStatus(String orderNo, Integer status);

    List<UserCourseOrderDTO> getUserCourseOrderByUserId(Integer userId);

    Integer countUserCourseOrderByCourseId(Integer courseId);

    List<UserCourseOrderDTO> getOrderListByCourseId(Integer courseId);

    /**
     * @Description: (保存订单信息)
     * @author: ma wei long
     * @date:   2020年6月21日 下午11:44:42
     */
    void saveOrder(UserCourseOrder order);
}
