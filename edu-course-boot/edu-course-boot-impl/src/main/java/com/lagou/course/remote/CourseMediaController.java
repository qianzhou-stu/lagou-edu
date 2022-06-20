package com.lagou.course.remote;

import com.lagou.course.api.MediaRemoteService;
import com.lagou.course.api.dto.MediaDTO;
import com.lagou.course.api.dto.VideoPlayDto;
import com.lagou.course.service.ICourseMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName CourseMediaController
 * @Description CourseMediaController
 * @Author zhouqian
 * @Date 2022/6/8 11:03
 * @Version 1.0
 */
@RestController
@RequestMapping("/course/media")
public class CourseMediaController implements MediaRemoteService {

    @Autowired
    private ICourseMediaService courseMediaService;
    @Override
    @GetMapping("/getByLessonId")
    public MediaDTO getByLessonId(@RequestParam("lessonId") Integer lessonId) {
        return courseMediaService.getByLessonId(lessonId);
    }

    @Override
    @GetMapping("/alikey")
    public byte[] getCourseMediaDKByFileId(@RequestParam("fileId") String fileId,
                                           @RequestParam("edk") String edk,
                                           @RequestParam("userId") Integer userId) {
        return courseMediaService.getCourseMediaDKByFileId(fileId, edk, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrSaveMedia(@RequestBody MediaDTO mediaDTO) {
        courseMediaService.updateOrSaveMedia(mediaDTO);
    }

    @Override
    public VideoPlayDto getVideoPlayInfo(@RequestParam("lessonId") Integer lessonId,
                                         @RequestParam("userId") Integer userId) {
        return null;
    }
}
