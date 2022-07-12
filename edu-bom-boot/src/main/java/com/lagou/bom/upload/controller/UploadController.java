package com.lagou.bom.upload.controller;

import com.lagou.bom.upload.service.OssService;
import com.lagou.bom.upload.vo.UpLoadResult;
import com.lagou.common.response.ResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/upload/")
@Api(value = "上传", tags = "上传接口")
public class UploadController {

    @Autowired
    private OssService ossService;

    @PostMapping("img")
    @ApiOperation(value = "上传图片")
    public ResponseDTO<UpLoadResult> upload(@RequestParam("file") MultipartFile multipartFile){
        UpLoadResult upLoadResult = ossService.upload(multipartFile);
        return ResponseDTO.success(upLoadResult);
    }

}
