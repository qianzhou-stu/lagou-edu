package com.lagou.course.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.common.constant.CacheDefine;
import com.lagou.common.date.DateUtil;
import com.lagou.common.response.ResponseDTO;
import com.lagou.course.api.dto.*;
import com.lagou.course.api.enums.CourseLessonStatus;
import com.lagou.course.api.enums.CourseStatus;
import com.lagou.course.api.param.CourseQueryParam;
import com.lagou.course.entity.Course;
import com.lagou.course.entity.CourseLesson;
import com.lagou.course.entity.CourseSection;
import com.lagou.course.entity.Teacher;
import com.lagou.course.mapper.CourseLessonMapper;
import com.lagou.course.mapper.CourseMapper;
import com.lagou.course.mapper.CourseSectionMapper;
import com.lagou.course.mapper.TeacherMapper;
import com.lagou.course.service.ICoursePlayHistoryService;
import com.lagou.course.service.ICourseService;
import com.lagou.course.service.ILessonService;
import com.lagou.course.util.DateUtils;
import com.lagou.order.api.UserCourseOrderRemoteService;
import com.lagou.order.api.dto.UserCourseOrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName CourseServiceImpl
 * @Description CourseServiceImpl实现类
 * @Author zhouqian
 * @Date 2022/6/7 21:00
 * @Version 1.0
 */
@Service
@Slf4j
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private CourseLessonMapper courseLessonMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private UserCourseOrderRemoteService userCourseOrderRemoteService;
    @Autowired
    private ICoursePlayHistoryService coursePlayHistoryService;
    @Autowired
    private ILessonService lessonService;
    @Autowired
    private CourseSectionMapper courseSectionMapper;
    //显示课时数
    private static final int SHOW_LESSON_NUM = 2;
    @Override
    public List<CourseDTO> getAllCourses(Integer userId) {
        log.info("getAllCourses - userId:{}",userId);
        // 获取有效课程
        List<Course> courses = getInvalidCourses();
        if (CollectionUtils.isEmpty(courses)){
            log.info("[选课列表] 获取选课列表信息为空");
            return Collections.emptyList();
        }
        // 根据用户获取对应的课程map信息
        Map<Integer, UserCourseOrderDTO> orderMap = getUserCourseOrderMapForCourseList(userId);
        List<CourseDTO> courseDTOS = new ArrayList<>();
        for (Course course : courses) {
            CourseDTO courseDTO = new CourseDTO();
            BeanUtils.copyProperties(course, courseDTO);
            courseDTOS.add(courseDTO);
        }
        // 未购上新的课程
        List<CourseDTO> newCourseList = new LinkedList<>();
        // 未购课程
        List<CourseDTO> notPayCourseList = new LinkedList<>();
        // 已购课程
        List<CourseDTO> payedCourseList = new LinkedList<>();
        for (CourseDTO courseDTO : courseDTOS) {
            Integer courseId = courseDTO.getId();
            // 设置购买标识
            setBuyFlag(orderMap, courseDTO, courseId);
            // 设置老师的信息
            setTeacher(courseDTO);
            // 设置topN课时
            setTopNCourseLesson(courseDTO);
            // 保存上新且未购买的课程
            if(!courseDTO.getIsBuy() && StringUtils.isNotBlank(courseDTO.getDiscountsTag())){
                hasActivityCourse(courseDTO);
                newCourseList.add(courseDTO);
                continue;
            }
            // 未购买课程
            if(!courseDTO.getIsBuy()){
                hasActivityCourse(courseDTO);
                notPayCourseList.add(courseDTO);
            }
            // 已购买的课程列表
            payedCourseList.add(courseDTO);
        }
        // 遍历组装courseList信息
        List<CourseDTO> courseList = new LinkedList<CourseDTO>();
        courseList.addAll(newCourseList);
        courseList.addAll(notPayCourseList);
        courseList.addAll(payedCourseList);
        return courseList;
    }
    // 获取有效课程信息
    private List<Course> getInvalidCourses(){
        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.eq("status", CourseStatus.PUTAWAY.getCode());
        courseQueryWrapper.eq("is_del", Boolean.FALSE);
        courseQueryWrapper.orderByDesc("sort_num", "create_time");
        return courseMapper.selectList(courseQueryWrapper);
    }

    // 根据用户id获取对应的课程map信息
    private Map<Integer, UserCourseOrderDTO> getUserCourseOrderMapForCourseList(Integer userId){
        if (userId == null){
            return Collections.emptyMap();
        }
        ResponseDTO<List<UserCourseOrderDTO>> orderResult = userCourseOrderRemoteService.getUserCourseOrderByUserId(userId);
        List<UserCourseOrderDTO> userOrders = orderResult.getContent();
        if (CollectionUtils.isEmpty(userOrders)){
            return Collections.emptyMap();
        }
        return userOrders.stream().collect(Collectors.toMap(UserCourseOrderDTO::getCourseId, Function.identity()));
    }
    // 设置购买标志
    private void setBuyFlag(Map<Integer, UserCourseOrderDTO> orderMap, CourseDTO courseDTO, Integer courseId){
        if(orderMap == null){
            log.info("用户没有购买课程，订单为空");
            return;
        }
        UserCourseOrderDTO order = orderMap.get(courseId);
        if(order != null){
            courseDTO.setIsBuy(Boolean.TRUE);
        }
    }

    private void setTeacher(CourseDTO courseDTO) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseDTO.getId());
        queryWrapper.eq("is_del",Boolean.FALSE);
        List<Teacher> teachers = teacherMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(teachers)){
            return;
        }
        TeacherDTO teacherDTO = new TeacherDTO();
        BeanUtils.copyProperties(teachers.get(0),teacherDTO);
        courseDTO.setTeacherDTO(teacherDTO);
    }

    private void setTopNCourseLesson(CourseDTO courseDTO) {
        QueryWrapper<CourseLesson> lessonQueryWrapper = new QueryWrapper<>();
        // 对lesson信息进行筛选，筛选出非隐藏的数据
        lessonQueryWrapper.ne("status", CourseLessonStatus.HIDE.getCode());
        lessonQueryWrapper.eq("course_id",courseDTO.getId());
        lessonQueryWrapper.eq("is_del", Boolean.FALSE);
        lessonQueryWrapper.orderByAsc("section_id" ,"order_num");
        lessonQueryWrapper.last("limit 0 , " + SHOW_LESSON_NUM);
        List<CourseLesson> courseLessons = courseLessonMapper.selectList(lessonQueryWrapper);
        List<LessonDTO> lessonDTOS = new ArrayList<>();
        courseLessons.forEach((item)->{
            LessonDTO lessonDTO = new LessonDTO();
            BeanUtils.copyProperties(item, lessonDTO);
            lessonDTOS.add(lessonDTO);
        });
        courseDTO.setTopNCourseLesson(lessonDTOS);
    }

    private void hasActivityCourse(CourseDTO courseDTO){
        String activityCourseStr = redisTemplate.opsForValue().get(CacheDefine.ActivityCourse.getKey(courseDTO.getId()));
        log.info("hasActivityCourse - activityCourseStr:{} courseId:{}",activityCourseStr,courseDTO.getId());
        if (null == activityCourseStr){
            return;
        }
        ActivityCourseDTO activityCourseCache = JSON.parseObject(activityCourseStr, ActivityCourseDTO.class);
        if (!DateUtil.isBefore(new Date(), activityCourseCache.getBeginTime())){
            return;
        }
        String stock = redisTemplate.opsForValue().get(CacheDefine.ActivityCourse.getStockKey(courseDTO.getId()));
        if (null == stock || Long.parseLong(stock) <= 0){
            return;
        }
        long time = DateUtil.getMillisecond(new Date(), activityCourseCache.getEndTime());
        if (time <= 0) {
            return;
        }
        courseDTO.setActivityCourse(true);
        courseDTO.setDiscounts(activityCourseCache.getAmount());
        courseDTO.setActivityTime(time);
    }

    @Override
    public List<CourseDTO> getPurchasedCourse(Integer userId) {
        if (userId == null){
            return Collections.emptyList();
        }
        // 1.获取订单消息 根据用户的id获取对应的订单消息
        ResponseDTO<List<UserCourseOrderDTO>> orderResult = userCourseOrderRemoteService.getUserCourseOrderByUserId(userId);
        List<UserCourseOrderDTO> userCourseOrderDTOS = orderResult.getContent();
        if (CollectionUtils.isEmpty(userCourseOrderDTOS)){
            log.info("[获取已购课程] 用户课程订单不存在，userId:{}", userId);
            return Collections.emptyList();
        }
        // 2.通过订单获取购买的课程ids
        List<Integer> courseIds = userCourseOrderDTOS.stream().map(UserCourseOrderDTO::getCourseId).collect(Collectors.toList());
        // 3.根据课程ids获取对应的课程信息
        List<Course> courseList = getCourseByIds(courseIds);
        if (CollectionUtils.isEmpty(courseList)){
            return Collections.emptyList();
        }
        // 4.获取已播放和未播放的课程
        List<CourseDTO> finalResultList = new ArrayList<>();
        for (Course course : courseList) {
            Integer courseId = course.getId();
            CourseDTO courseDTO = new CourseDTO();
            CoursePlayHistoryDTO recordLearn = coursePlayHistoryService.getRecordLearn(courseId, userId);
            if (recordLearn != null){
                Integer lessonId = recordLearn.getLessonId();
                LessonDTO lessonDTO = lessonService.getById(lessonId);
                if (lessonDTO != null){
                    courseDTO.setLastLearnLessonName(lessonDTO.getTheme());
                }
            }
            BeanUtils.copyProperties(course, courseDTO);
            // 4.1 设置课程更新数量
            setLessonUpdateCount(courseId, courseDTO);
            // 4.2 设置课程的最近播放时间
            setCourseLastPlayTimeAndLessonName(userId, course.getId(), courseDTO);
            // 4.3 根据支付时间和播放时间，重设待排序比较时间
            resetCompareDate(courseDTO, userCourseOrderDTOS);
            finalResultList.add(courseDTO);
        }
        // 5. 按播放时间排序
        sortByCompareTime(finalResultList);
        return finalResultList;
    }

    private void setLessonUpdateCount(Integer courseId, CourseDTO courseDTO){
        Integer releaseCourse = lessonService.getReleaseCourse(courseId);
        courseDTO.setLessonUpdateCount(releaseCourse);
    }

    private List<Course> getCourseByIds(List<Integer> courseIds){
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", CourseStatus.PUTAWAY.getCode());
        queryWrapper.eq("is_del", Boolean.FALSE);
        queryWrapper.in("id", courseIds);
        queryWrapper.orderByDesc("create_time");
        return courseMapper.selectList(queryWrapper);
    }

    private void setCourseLastPlayTimeAndLessonName(Integer userId, Integer courseId, CourseDTO courseDTO){
        CoursePlayHistoryDTO coursePlayHistoryDTO = coursePlayHistoryService.getByUserIdAndCourseId(userId, courseId);
        if (Objects.isNull(coursePlayHistoryDTO)){
            return;
        }
        courseDTO.setCompareTime(DateUtils.asDate(coursePlayHistoryDTO.getUpdateTime()));
        LessonDTO lessonDTO = lessonService.getById(coursePlayHistoryDTO.getLessonId());
        courseDTO.setLastLearnLessonName(lessonDTO == null ? null : lessonDTO.getTheme());
    }

    private void resetCompareDate(CourseDTO courseDTO, List<UserCourseOrderDTO> userCourseOrderDTOS){
        Map<Integer, UserCourseOrderDTO> orderMap = userCourseOrderDTOS.stream().collect(Collectors.toMap(UserCourseOrderDTO::getCourseId, Function.identity()));
        // 不存在购买时间，直接返回
        UserCourseOrderDTO userCourseOrderDTO = orderMap.get(courseDTO.getId());
        if(userCourseOrderDTO == null || userCourseOrderDTO.getCreateTime() == null){
            if (courseDTO.getCompareTime() == null){
                courseDTO.setCompareTime(new Date(0));
            }
            return;
        }
        // 当前面未设置播放时间时，将支付时间设置为待比较时间
        Date playTime = userCourseOrderDTO.getCreateTime();
        if (courseDTO.getCompareTime() == null){
            courseDTO.setCompareTime(playTime);
            return;
        }
        // 当播放时间和支付时间都不为空时，比较这两个时间，选出最新的时间
        if(playTime.compareTo(courseDTO.getCompareTime()) <= 0){
            return;
        }
        courseDTO.setCompareTime(playTime);
    }

    private void sortByCompareTime(List<CourseDTO> courseDTOList){
        if (CollectionUtils.isEmpty(courseDTOList) || courseDTOList.size() < 1){
            return;
        }
        Collections.sort(courseDTOList, new Comparator<CourseDTO>() {
            @Override
            public int compare(CourseDTO o1, CourseDTO o2) {
                Date o1Time = o1.getCompareTime();
                Date o2Time = o2.getCompareTime();
                if (o1Time == null && o2Time == null){
                    return 0;
                }
                if (o2Time == null && o1Time != null){
                    return -1;
                }
                if (o2Time != null && o1Time == null){
                    return 1;
                }
                return o2Time.compareTo(o1Time);
            }
        });
    }

    @Override
    public CourseDTO getCourseById(Integer courseId, Integer userId) {
        // 根据courseId和userId查询课程的详细信息
        CourseDTO courseDTO = new CourseDTO();
        // 分为用户是否登陆的情况
        if (userId == null){
            courseInfoRes(courseDTO, courseId);
        }else{
            // 如果为userId和courseId的时候
            List<Integer> courseIds = new ArrayList<>(); courseIds.add(courseId);
            ResponseDTO<Integer> responseDTO = userCourseOrderRemoteService.countUserCourseOrderByCourseIds(userId, courseIds);
            Integer count = responseDTO.getContent();
            if (count == 0){
                // 表示没有购买课程信息
                courseInfoRes(courseDTO, courseId);
            }else {
                // 表示购买了课程信息
                courseInfoRes(courseDTO, courseId);
            }
        }
        return courseDTO;
    }

    private void courseInfoRes(CourseDTO courseDTO, Integer courseId){
        Course course = courseMapper.selectById(courseId);
        BeanUtils.copyProperties(course, courseDTO);
        LambdaQueryWrapper<CourseSection> sectionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sectionLambdaQueryWrapper.eq(CourseSection::getCourseId, courseId);
        List<CourseSection> courseSections = courseSectionMapper.selectList(sectionLambdaQueryWrapper);
        List<SectionDTO> sectionDTOS = new ArrayList<>();
        courseSections.forEach(
                (item) ->{
                    SectionDTO sectionDTO = new SectionDTO();
                    BeanUtils.copyProperties(item, sectionDTO);
                    // 对每一个courseSection做处理，找对应的lesson，然后将对应的lesson放入对应的section中
                    // 根据课程id和章节id获取对应的lesson
                    LambdaQueryWrapper<CourseLesson> lessonLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    lessonLambdaQueryWrapper.eq(CourseLesson::getCourseId, courseId);
                    lessonLambdaQueryWrapper.eq(CourseLesson::getSectionId, item.getId());
                    List<CourseLesson> courseLessons = courseLessonMapper.selectList(lessonLambdaQueryWrapper);
                    List<LessonDTO> lessonDTOS = new ArrayList<>();
                    for (CourseLesson courseLesson : courseLessons) {
                        LessonDTO lessonDTO = new LessonDTO();
                        BeanUtils.copyProperties(courseLesson, lessonDTO);
                        lessonDTOS.add(lessonDTO);
                    }
                    sectionDTO.setLessonDTOS(lessonDTOS);
                }
        );
        courseDTO.setSectionDTOS(sectionDTOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateCourse(CourseDTO courseDTO) {
        Course course = new Course();
        LocalDateTime now = LocalDateTime.now();
        BeanUtils.copyProperties(courseDTO, course);
        course.setUpdateTime(now);
        TeacherDTO teacherDTO = courseDTO.getTeacherDTO();
        if (courseDTO.getId() == null){
            course.setCreateTime(now);
            log.info("保存的课程信息：{}", JSON.toJSONString(course));
            courseMapper.insert(course);
            if (teacherDTO == null){
                return Boolean.TRUE;
            }else {
                Teacher teacher = new Teacher();
                BeanUtils.copyProperties(teacherDTO, teacher);
                teacher.setCourseId(courseDTO.getId());
                teacher.setUpdateTime(now);
                teacher.setCreateTime(now);
                log.info("保存老师的信息为 teacher:{}",JSON.toJSONString(teacher));
                teacherMapper.insert(teacher);
            }
            return true;
        }else{
            log.info("更新的课程信息：{}", JSON.toJSONString(course));
            courseMapper.updateById(course);
            if (teacherDTO == null){
                return Boolean.TRUE;
            }else{
                Teacher teacher = teacherMapper.selectByCourseId(courseDTO.getId());
                if(teacher == null){
                    Teacher te = new Teacher();
                    BeanUtils.copyProperties(teacherDTO, te);
                    te.setCreateTime(LocalDateTime.now());
                    te.setUpdateTime(LocalDateTime.now());
                    teacherMapper.insert(te);
                }else{
                    BeanUtils.copyProperties(teacherDTO, teacher);
                    teacher.setUpdateTime(LocalDateTime.now());
                    teacherMapper.updateById(teacher);
                }
            }
            return true;
        }
    }

    @Override
    public PageResultDTO<CourseDTO> getQueryCourses(CourseQueryParam courseQueryParam) {
        LambdaQueryWrapper<Course> courseLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (courseQueryParam.getCourseName() != null){
            courseLambdaQueryWrapper.eq(Course::getCourseName, courseQueryParam.getCourseName());
        }
        if (courseQueryParam.getStatus() != null){
            courseLambdaQueryWrapper.eq(Course::getStatus, courseQueryParam.getStatus());
        }
        IPage<Course> page = new Page<>(courseQueryParam.getCurrentPage(), courseQueryParam.getPageSize());
        IPage<Course> courseIPage = courseMapper.selectPage(page, courseLambdaQueryWrapper);
        if (courseIPage != null){
            PageResultDTO<CourseDTO> pageResultDTO = new PageResultDTO<>();
            pageResultDTO.setCurrent(courseIPage.getCurrent());
            pageResultDTO.setSize(courseIPage.getSize());
            pageResultDTO.setPages(courseIPage.getPages());
            pageResultDTO.setTotal(courseIPage.getTotal());
            List<Course> courses = courseIPage.getRecords();
            List<CourseDTO> courseDTOList = new ArrayList<>();
            courses.forEach((item)->{
                CourseDTO courseDTO = new CourseDTO();
                BeanUtils.copyProperties(item, courseDTO);
                courseDTOList.add(courseDTO);
            });
            pageResultDTO.setRecords(courseDTOList);
            return pageResultDTO;
        }
        return null;
    }

    @Override
    public Boolean changeState(Integer courseId, Integer status) {
        // 根据课程id查询课程信息并且修改状态的值
        LambdaQueryWrapper<Course> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Course::getId, courseId);
        Course course = courseMapper.selectOne(lambdaQueryWrapper);
        course.setStatus(status);
        int i = courseMapper.updateById(course);
        return i > 0;
    }

    @Override
    @Transactional(value = "courseAutoOnline", rollbackFor = Exception.class)
    public void courseAutoOnline() {
        UpdateWrapper<Course> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", 1).set("update_time", LocalDateTime.now());
        updateWrapper.eq("status",0).isNotNull("auto_online_time")
                .le("auto_online_time", LocalDateTime.now());
        this.update(updateWrapper);
    }
}
