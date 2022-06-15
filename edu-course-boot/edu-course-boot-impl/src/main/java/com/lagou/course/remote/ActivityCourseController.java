package com.lagou.course.remote;


import com.lagou.common.response.ResponseDTO;
import com.lagou.course.api.ActivityCourseRemoteService;
import com.lagou.course.api.dto.ActivityCourseDTO;
import com.lagou.course.entity.ActivityCourse;
import com.lagou.course.service.IActivityCourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName ActivityCourseController
 * @Description ActivityCourseController
 * @Author zhouqian
 * @Date 2022/6/8 11:05
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/activityCourse")
public class ActivityCourseController implements ActivityCourseRemoteService {

    @Autowired
    private IActivityCourseService activityCourseService;
    /**
     * 保存课程活动
     * @param reqDTO
     * @return ResponseDTO<?>
     */
    @PostMapping("/saveActivityCourse")
    @Override
    public ResponseDTO<?> saveActivityCourse(@RequestBody ActivityCourseDTO reqDTO) {
        Boolean saveBoolean = activityCourseService.saveActivityCourse(reqDTO);
        return ResponseDTO.success(saveBoolean);
    }

    /**
     * 更新课程活动状态
     * @param reqDTO
     * @return ResponseDTO<?>
     */
    @PostMapping("/updateActivityCourseStatus")
    @Override
    public ResponseDTO<?> updateActivityCourseStatus(@RequestBody ActivityCourseDTO reqDTO) {
        Boolean updateBoolean = activityCourseService.updateActivityCourseStatus(reqDTO);
        return ResponseDTO.success(updateBoolean);
    }

    /**
     * 根据id获取课程活动
     * @param id
     * @return ResponseDTO<ActivityCourseDTO>
     */
    @GetMapping("/getById")
    @Override
    public ResponseDTO<ActivityCourseDTO> getById(@RequestParam("id") Integer id) {
        ActivityCourse activityCourse = activityCourseService.getById(id);
        ActivityCourseDTO activityCourseDTO = new ActivityCourseDTO();
        BeanUtils.copyProperties(activityCourse, activityCourseDTO);
        return ResponseDTO.success(activityCourseDTO);
    }

    /**
     * 根据课程id获取课程活动
     * @param courseId
     * @return ResponseDTO<ActivityCourseDTO>
     */
    @GetMapping("/getByCourseId")
    @Override
    public ResponseDTO<ActivityCourseDTO> getByCourseId(@RequestParam("courseId") Integer courseId) {
        ActivityCourseDTO activityCourseDTO = activityCourseService.getByCourseId(courseId);
        return ResponseDTO.success(activityCourseDTO);
    }

    /**
     * 根绝订单号和课程id更新课程活动库存
     * @param courseId
     * @param orderNo
     * @return ResponseDTO<?>
     */
    @PostMapping("/updateActivityCourseStock")
    @Override
    public ResponseDTO<?> updateActivityCourseStock(@RequestParam("courseId")Integer courseId,@RequestParam("orderNo")String orderNo) {
        Boolean updateActivityCourseStockBoolean = activityCourseService.updateActivityCourseStock(courseId, orderNo);
        return ResponseDTO.success(updateActivityCourseStockBoolean);
    }
}
