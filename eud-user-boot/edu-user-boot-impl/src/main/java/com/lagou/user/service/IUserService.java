package com.lagou.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lagou.user.api.dto.UserDTO;
import com.lagou.user.api.param.UserQueryParam;
import com.lagou.user.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouqian
 * @since 2022-04-05
 */
public interface IUserService extends IService<User> {

    Page<UserDTO> getUserPages(UserQueryParam userQueryParam);

    UserDTO getUserId(Integer id);

    UserDTO getUserByPhone(String phone);

    UserDTO saveUser(UserDTO userDTO);

    boolean updateUser(UserDTO userDTO);

    boolean isUpdatedPassword(Integer userId);

    boolean setPassword(Integer userId, String password, String configPassword);

    boolean updatePassword(Integer userId, String oldPassword, String newPassword, String configPassword);

    boolean forbidUser(Integer userId);
}
