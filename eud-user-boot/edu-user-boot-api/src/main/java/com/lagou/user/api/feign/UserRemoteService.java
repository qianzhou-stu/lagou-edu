package com.lagou.user.api.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lagou.user.api.dto.UserDTO;
import com.lagou.user.api.param.UserQueryParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "edu-user-boot", path = "/user")
public interface UserRemoteService {
    // 根据id查询用户
    @GetMapping("/getUserById")
    UserDTO getUserById(@RequestParam("id") Integer id);

    // 分页查询用户
    @PostMapping(value = "/getUserPages")
    Page<UserDTO> getUserPages(@RequestBody UserQueryParam userQueryParam);

    // 根据电话查询用户
    @GetMapping("/getUserByPhone")
    UserDTO getUserByPhone(@RequestParam("phone") String phone);

    // 是否注册
    @GetMapping("/isRegister")
    boolean isRegister(@RequestParam("phone") String phone);

    // 根据用户查询对应的课程分页
    @GetMapping("/getPagesCourses")
    Page<UserDTO> getPagesCourses(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize);

    // 注册用户
    @PostMapping("/saveUser")
    Boolean saveUser(@RequestBody UserDTO userDTO);

    // 更新用户
    @PostMapping("updateUser")
    boolean updateUser(@RequestBody UserDTO userDTO);

    // 是否更新密码
    @GetMapping("/isUpdatedPassword")
    boolean isUpdatedPassword(@RequestParam("userId") Integer userId);

    // 设置密码
    @PostMapping("/setPassword")
    boolean setPassword(@RequestParam("userId") Integer userId, @RequestParam("password") String password, @RequestParam("configPassword") String configPassword);

    // 更新密码
    @PostMapping("/updatePassword")
    boolean updatePassword(@RequestParam("userId") Integer userId, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, @RequestParam("configPassword") String configPassword);

    // 是否禁用用户
    @PostMapping("/forbidUser")
    boolean forbidUser(@RequestParam("userId") Integer userId);
}
