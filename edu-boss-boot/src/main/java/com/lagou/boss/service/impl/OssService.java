package com.lagou.boss.service.impl;

import com.aliyun.oss.OSSClient;
import com.lagou.boss.config.AliyunConfig;
import com.lagou.boss.entity.bo.UpLoadResult;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class OssService {
    @Autowired
    private AliyunConfig aliyunConfig;
    @Autowired
    private OSSClient ossClient;

    /**
     * 允许上传的图片
     */
    private static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg",
            ".jpeg", ".gif", ".png"};

    public UpLoadResult upload(MultipartFile uploadFile){
        // 校验图片格式
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(), type)){
                isLegal = true;
                break;
            }
        }
        UpLoadResult upLoadResult = new UpLoadResult();
        if (!isLegal){
            upLoadResult.setStatus("error");
            return upLoadResult;
        }
        String filename = uploadFile.getOriginalFilename();
        String filePath = getFilePath(filename);
        try {
            ossClient.putObject(aliyunConfig.getBucketName(),filePath, new ByteArrayInputStream(uploadFile.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
            // 上传失败
            upLoadResult.setStatus("error");
            return upLoadResult;
        }
        upLoadResult.setStatus("done");
        upLoadResult.setName(this.aliyunConfig.getUrlPrefix() + filePath);
        upLoadResult.setUid(String.valueOf(System.currentTimeMillis()));
        return upLoadResult;
    }
    private String getFilePath(String sourceFileName) {
        DateTime dateTime = new DateTime();
        return "images/" + dateTime.toString("yyyy")
                + "/" + dateTime.toString("MM") + "/"
                + dateTime.toString("dd") + "/" + System.currentTimeMillis() +
                RandomUtils.nextInt(100, 9999) + "." +
                StringUtils.substringAfterLast(sourceFileName, ".");
    }
}
