package com.lagou.oauth.service;


import com.lagou.user.api.dto.UserDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public interface IUserService {

    /**
     * 根据用户手机号获取用户信息
     *
     * @return
     */
    @Cacheable(value = "#phone")
    UserDTO getByPhone(@RequestParam("phone") String phone);

    /**
     * 根据用户唯一标识获取用户信息
     *
     * @return
     */
    @Cacheable(value = "#userId")
    UserDTO getByUserId(@RequestParam("userId") Integer userId);

    Boolean save(@RequestParam("name") String name, @RequestParam("phone") String phone,
                 @RequestParam("portrait") String portrait, @RequestParam("password") String password);
}
