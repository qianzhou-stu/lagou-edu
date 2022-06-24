package com.lagou.user.api.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lagou.user.api.dto.UserDTO;
import com.lagou.user.api.feign.UserRemoteService;
import com.lagou.user.api.param.UserQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserRemoteServiceFallBack implements UserRemoteService {
    @Override
    public UserDTO getUserById(Integer id) {
        return null;
    }

    @Override
    public Page<UserDTO> getUserPages(UserQueryParam userQueryParam) {
        return null;
    }

    @Override
    public UserDTO getUserByPhone(String phone) {
        return null;
    }

    @Override
    public boolean isRegister(String phone) {
        return false;
    }

    @Override
    public Page<UserDTO> getPagesCourses(Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        return null;
    }

    @Override
    public boolean updateUser(UserDTO userDTO) {
        return false;
    }

    @Override
    public boolean isUpdatedPassword(Integer userId) {
        return false;
    }

    @Override
    public boolean setPassword(Integer userId, String password, String configPassword) {
        return false;
    }

    @Override
    public boolean updatePassword(Integer userId, String oldPassword, String newPassword, String configPassword) {
        return false;
    }

    @Override
    public boolean forbidUser(Integer userId) {
        return false;
    }
}
