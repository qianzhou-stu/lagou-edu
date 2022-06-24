package com.lagou.boss.rest;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lagou.common.entity.vo.Result;
import com.lagou.user.api.dto.UserDTO;
import com.lagou.user.api.feign.UserRemoteService;
import com.lagou.user.api.param.UserQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/")
@Api(tags = "用户接口")
@Slf4j
public class UserController {
    @Autowired
    private UserRemoteService userRemoteService;

    @ApiOperation(value = "分页查询用户信息")
    @GetMapping("getUserPages")
    public Result getUserPages(@RequestBody UserQueryParam userQueryParam) {
        log.info("分页查询用户信息：{}", JSON.toJSONString(userQueryParam));
        Integer pageNum = userQueryParam.getPageNum();
        Integer pageSize = userQueryParam.getPageSize();
        if (null == pageNum || pageNum <= 0) {
            userQueryParam.setPageNum(1);
        }
        if (null == pageSize || pageSize <= 0) {
            userQueryParam.setPageSize(10);
        }
        Page<UserDTO> userPages =
                userRemoteService.getUserPages(userQueryParam);
        return Result.success(userPages);
    }

    @ApiOperation(value = "封禁用户")
    @GetMapping("forbidUser")
    public Result forbidUser(@RequestParam("userId") Integer userId) {
        log.info("封禁用户:{}", userId);
        boolean result = userRemoteService.forbidUser(userId);
        return Result.success(result);
    }
}
