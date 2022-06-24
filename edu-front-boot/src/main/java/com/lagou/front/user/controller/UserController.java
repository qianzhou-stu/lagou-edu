package com.lagou.front.user.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lagou.oauth.api.feign.OAuthRemoteService;
import com.lagou.common.regex.RegexUtil;
import com.lagou.common.response.EduEnum;
import com.lagou.common.response.ResponseDTO;
import com.lagou.front.common.UserManager;
import com.lagou.front.user.service.UserService;
import com.lagou.user.api.dto.UserDTO;
import com.lagou.user.api.feign.UserRemoteService;
import com.lagou.user.api.param.UserQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @ClassName UserController
 * @Description UserController
 * @Author zhouqian
 * @Date 2022/4/5 10:33
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserRemoteService userRemoteService;
    @Value("${spring.oauth.client_id}")
    private String clientId;
    @Value("${spring.oauth.client_secret}")
    private String clientSecret;
    @Value("${spring.oauth.scope}")
    private String scope;
    @Value("${spring.oauth.grant_type}")
    private String grantType;
    @Value("${spring.oauth.refresh_grant_type}")
    private String refreshGrantType;
    @Autowired
    private UserService userService;
    @Autowired
    private OAuthRemoteService oAuthRemoteService;

    @PostMapping(value = "/getUserPages")
    public ResponseDTO getUserPages(@RequestBody UserQueryParam userQueryParam) {
        Page<UserDTO> userDTOPage = userRemoteService.getUserPages(userQueryParam);
        return ResponseDTO.success(userDTOPage);
    }

    @GetMapping("/getUserById")
    public ResponseDTO getUserById(@RequestParam("id") Integer id) {
        UserDTO userDTO = userRemoteService.getUserById(id);
        return ResponseDTO.success(userDTO);
    }

    @GetMapping("/getUserByPhone")
    public ResponseDTO getUserByPhone(@RequestParam("phone") String phone) {
        UserDTO userByPhone = userRemoteService.getUserByPhone(phone);
        return ResponseDTO.success(userByPhone);
    }

    @GetMapping("/isRegister")
    public ResponseDTO isRegister(@RequestParam("phone") String phone) {
        boolean register = userRemoteService.isRegister(phone);
        if (!register) {
            return ResponseDTO.success();
        } else {
            return ResponseDTO.ofError(EduEnum.PHONE_EXISTS.getCode(), EduEnum.PHONE_EXISTS.getMsg());
        }
    }

    @GetMapping("/getPagesCourses")
    public ResponseDTO getPagesCourses(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        Page<UserDTO> pagesCourses = userRemoteService.getPagesCourses(pageNum, pageSize);
        return ResponseDTO.success(pagesCourses);
    }

    @PostMapping("/saveUser")
    public ResponseDTO saveUser(@RequestBody UserDTO userDTO) {
        UserDTO user = userRemoteService.saveUser(userDTO);
        if (user != null) {
            return ResponseDTO.success();
        } else {
            return ResponseDTO.ofError(EduEnum.INSERT_FAILURE.getCode(), EduEnum.INSERT_FAILURE.getMsg());
        }
    }

    @PostMapping("updateUser")
    public ResponseDTO updateUser(@RequestBody UserDTO userDTO) {
        boolean b = userRemoteService.updateUser(userDTO);
        if (b) {
            return ResponseDTO.success();
        } else {
            return ResponseDTO.ofError(EduEnum.UPDATE_FAILURE.getCode(), EduEnum.UPDATE_FAILURE.getMsg());
        }
    }

    @GetMapping("/isUpdatedPassword")
    public ResponseDTO isUpdatedPassword(@RequestParam("userId") Integer userId) {
        boolean b = userRemoteService.isUpdatedPassword(userId);
        if (b) {
            return ResponseDTO.success();
        } else {
            return ResponseDTO.ofError(EduEnum.IS_UPDATE_PASSWORD.getCode(), EduEnum.IS_UPDATE_PASSWORD.getMsg());
        }
    }

    @PostMapping("/setPassword")
    public ResponseDTO setPassword(@RequestParam("userId") Integer userId, @RequestParam("password") String password, @RequestParam("configPassword") String configPassword) {
        boolean b = userRemoteService.setPassword(userId, password, configPassword);
        if (b) {
            return ResponseDTO.success();
        } else {
            return ResponseDTO.ofError(EduEnum.UPDATE_PASSWORD_FAILURE.getCode(), EduEnum.UPDATE_PASSWORD_FAILURE.getMsg());
        }
    }

    @PostMapping("/updatePassword")
    public ResponseDTO updatePassword(@RequestParam("userId") Integer userId, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, @RequestParam("configPassword") String configPassword) {
        boolean b = userRemoteService.updatePassword(userId, oldPassword, newPassword, configPassword);
        if (b) {
            return ResponseDTO.success();
        } else {
            return ResponseDTO.ofError(EduEnum.UPDATE_PASSWORD_FAILURE.getCode(), EduEnum.UPDATE_PASSWORD_FAILURE.getMsg());
        }
    }

    @PostMapping("/forbidUser")
    public ResponseDTO forbidUser(@RequestParam("userId") Integer userId) {
        boolean b = userRemoteService.forbidUser(userId);
        if (b) {
            return ResponseDTO.success();
        } else {
            return ResponseDTO.ofError(EduEnum.FORBID_USER_FAILURE.getCode(), EduEnum.FORBID_USER_FAILURE.getMsg());
        }
    }


    @PostMapping("login")
    public ResponseDTO login(@RequestParam("phone") String phone, @RequestParam("password") String password, @RequestParam("code") String code) {
        if (StringUtils.isEmpty(phone)) {
            return ResponseDTO.ofError(EduEnum.NOTNULL_PHONE.getCode(), EduEnum.NOTNULL_PHONE.getMsg());
        }
        if (!RegexUtil.isPhone(phone)) {
            return ResponseDTO.ofError(EduEnum.ERROR_PHONE.getCode(), EduEnum.ERROR_PHONE.getMsg());
        }
        if (StringUtils.isEmpty(password) && StringUtils.isEmpty(code)) {
            return ResponseDTO.ofError(EduEnum.NOTNULL_PASSWORD_CODE.getCode(), EduEnum.NOTNULL_PASSWORD_CODE.getMsg());
        }
        // 默认是账号密码登录
        Integer type = 0;
        if (StringUtils.isEmpty(password) && StringUtils.isNotEmpty(code)) {
            type = 1;
        }
        // 判断手机号是不是注册了
        boolean isRegister = userRemoteService.isRegister(phone);
        if (!Boolean.TRUE.equals(isRegister)) {
            // 不允许直接手机号+密码注册并登录
            if (type == 0) {
                log.info("该手机号[{}]未注册,不允许手机号+密码注册", phone);
                return ResponseDTO.ofError(EduEnum.ERROR_PHONE_PASSWORD.getCode(), EduEnum.ERROR_PHONE_PASSWORD.getMsg());
            }
            UserDTO dto = new UserDTO();
            dto.setAccountNonExpired(true);
            dto.setAccountNonLocked(true);
            dto.setCredentialsNonExpired(true);
            dto.setStatus("ENABLE");
            dto.setRegIp(UserManager.getUserIP());
            dto.setName(phone);
            dto.setPassword(type == 0 ? password : phone);
            dto.setCreateTime(new Date());
            dto.setIsDel(false);
            dto.setPhone(phone);
            dto.setUpdateTime(new Date());
            dto.setPortrait(null);
            UserDTO newUser = userRemoteService.saveUser(dto);
            if (newUser == null) {
                log.info("用户注册失败");
                return ResponseDTO.ofError(EduEnum.INSERT_FAILURE.getCode(), EduEnum.INSERT_FAILURE.getMsg());
            }
            log.info("用户注册成功");
        }
        return userService.createAuthToken(phone, password, code, type);
    }


    @PostMapping("/logout")
    public ResponseDTO<String> logout() {
        // TODO 删除refresh_token, access_token
        Integer userId = UserManager.getUserId();
        log.info("============用户[{}]退出登录================", userId);
        return ResponseDTO.success();
    }

    @PostMapping("/refreshToken")
    public ResponseDTO<String> refreshToken(String refresh_token) {
        if (StringUtils.isEmpty(refresh_token)) {
            return ResponseDTO.ofError(EduEnum.ISNULL_REFRESH_TOKEN.getCode(), EduEnum.ISNULL_REFRESH_TOKEN.getMsg());
        }
        String token = this.oAuthRemoteService.refreshToken(refresh_token, refreshGrantType, clientId, clientSecret);
        if (StringUtils.isBlank(token)) {
            return ResponseDTO.ofError(EduEnum.LOGIN_FAILURE.getCode(), EduEnum.LOGIN_FAILURE.getMsg());
        }
        String userId = JSON.parseObject(token).getString("user_id");
        if (StringUtils.isBlank(userId)) {
            return ResponseDTO.ofError(EduEnum.LOGIN_FAILURE.getCode(), EduEnum.LOGIN_FAILURE.getMsg());
        }
        return ResponseDTO.success(token);
    }

}
