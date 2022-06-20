package com.lagou.boss.service;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class AliyunTask {
    /**
     * 任务id
     * 开悟课程：课时id
     * 开悟大课：视频使用类型+ref_id
     */
    private String taskId;
    private String coverImageUrl;
    private String fileName;
    private Double fileSize;
    private String duration;
    private String fileId;
    private String fileUrl;
    private String fileEdk;
    private String fileDk;
    private Integer durationNum;

}
