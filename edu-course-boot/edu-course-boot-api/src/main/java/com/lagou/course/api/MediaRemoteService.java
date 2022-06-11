package com.lagou.course.api;

import com.lagou.course.api.dto.MediaDTO;
import com.lagou.course.api.dto.VideoPlayDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName MediaRemoteService
 * @Description 媒体Feign调用
 * @Author zhouqian
 * @Date 2022/6/6 18:28
 * @Version 1.0
 */
//@FeignClient(name = "${remote.feign.edu-course-boot.name:edu-course-boot}", path = "/course/media")
@FeignClient(name = "edu-course-boot.MediaRemoteService", path = "/course/media")
public interface MediaRemoteService {

    /**
     *
     * @param lessonId
     * @return MediaDTO
     */
    @GetMapping("/getByLessonId")
    MediaDTO getByLessonId(@RequestParam("lessonId") Integer lessonId);

    /**
     * 获取DK数据
     * @param fileId
     * @param edk
     * @param userId
     * @return byte[]
     */
    @GetMapping("/alikey")
    byte[] getCourseMediaDKByFileId(@RequestParam("fileId") String fileId,
                                    @RequestParam("edk") String edk,
                                    @RequestParam("userId") Integer userId);

    /**
     * 更新或者保存媒体
     * @param mediaDTO
     */
    @PostMapping("/updateOrSaveMedia")
    void updateOrSaveMedia(@RequestBody MediaDTO mediaDTO);

    /**
     * 获取播放媒体信息
     * @param lessonId
     * @param userId
     * @return VideoPlayDto
     */
    @GetMapping("/getVideoPlayInfo")
    VideoPlayDto getVideoPlayInfo(@RequestParam("lessonId") Integer lessonId,
                                  @RequestParam("userId") Integer userId);

}
