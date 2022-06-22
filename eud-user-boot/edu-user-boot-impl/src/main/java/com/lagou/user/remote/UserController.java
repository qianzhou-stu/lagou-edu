package com.lagou.user.remote;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lagou.user.api.dto.UserDTO;
import com.lagou.user.api.feign.UserRemoteService;
import com.lagou.user.api.param.UserQueryParam;
import com.lagou.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhouqian
 * @since 2022-04-05
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController implements UserRemoteService {

    @Autowired
    private IUserService userService;

    @Override
    @GetMapping("/getUserById")
    public UserDTO getUserById(@RequestParam("id") Integer id) {
        UserDTO userDTO = userService.getUserId(id);
        return userDTO;
    }

    @Override
    @PostMapping(value = "/getUserPages")
    public Page<UserDTO> getUserPages(@RequestBody UserQueryParam userQueryParam) {
        Page<UserDTO> userDTOPage = userService.getUserPages(userQueryParam);
        return userDTOPage;
    }

    @Override
    @GetMapping("/getUserByPhone")
    public UserDTO getUserByPhone(@RequestParam("phone") String phone) {
        UserDTO userDTO = userService.getUserByPhone(phone);
        return userDTO;
    }

    @Override
    @GetMapping("/isRegister")
    public boolean isRegister(@RequestParam("phone") String phone) {
        UserDTO userByPhone = getUserByPhone(phone);
        return userByPhone != null && !Boolean.TRUE.equals(userByPhone.getIsDel());
    }

    @Override
    @GetMapping("/getPagesCourses")
    public Page<UserDTO> getPagesCourses(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        return null;
    }

    @Override
    @PostMapping("/saveUser")
    public UserDTO saveUser(@RequestBody UserDTO userDTO) {
        return userService.saveUser(userDTO);
    }

    @Override
    @PostMapping("updateUser")
    public boolean updateUser(@RequestBody UserDTO userDTO) {
        return userService.updateUser(userDTO);
    }

    @Override
    @GetMapping("/isUpdatedPassword")
    public boolean isUpdatedPassword(@RequestParam("userId") Integer userId) {
        return userService.isUpdatedPassword(userId);
    }

    @Override
    @PostMapping("/setPassword")
    public boolean setPassword(@RequestParam("userId") Integer userId, @RequestParam("password") String password, @RequestParam("configPassword") String configPassword) {
        return userService.setPassword(userId, password, configPassword);
    }

    @Override
    @PostMapping("/updatePassword")
    public boolean updatePassword(@RequestParam("userId") Integer userId, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, @RequestParam("configPassword") String configPassword) {
        return userService.updatePassword(userId, oldPassword, newPassword, configPassword);
    }

    @Override
    @PostMapping("/forbidUser")
    public boolean forbidUser(@RequestParam("userId") Integer userId) {
        return userService.forbidUser(userId);
    }
}
