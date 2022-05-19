package com.lagou.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.common.util.ConvertUtil;
import com.lagou.user.api.dto.UserDTO;
import com.lagou.user.api.param.UserQueryParam;
import com.lagou.user.entity.User;
import com.lagou.user.mapper.UserMapper;
import com.lagou.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhouqian
 * @since 2022-04-05
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @Autowired
    private UserMapper userMapper;
    @Override
    public Page<UserDTO> getUserPages(UserQueryParam userQueryParam) {
        Page<User> page = new Page<>();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 根据电话号码查询
        if (StringUtils.isNotBlank(userQueryParam.getPhone())) {
            queryWrapper.like("phone", userQueryParam.getPhone());
        }
        // 根据时间查找
        if (null != userQueryParam.getStartCreateTime() && null != userQueryParam.getEndCreateTime()) {
            queryWrapper.le("create_time", userQueryParam.getEndCreateTime());
            queryWrapper.gt("create_time", userQueryParam.getStartCreateTime());
        }
        // 根据用户id查询
        if (null != userQueryParam.getUserId() && userQueryParam.getUserId() > 0){
            queryWrapper.eq("id", userQueryParam.getUserId());
        }
        // 统计数量
        Integer count = userMapper.selectCount(queryWrapper);
        queryWrapper.orderByDesc("id");
        Page<User> userPage = this.userMapper.selectPage(page, queryWrapper);
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : userPage.getRecords()) {
            UserDTO userDTO = ConvertUtil.convert(user, UserDTO.class);
            userDTOS.add(userDTO);
        }
        Page<UserDTO> userDTOPage = new Page<>();
        // 分页查询结果对象属性结果的拷贝
        ConvertUtil.convert(userPage,userDTOPage);
        // 设置分页结果对象record属性
        userDTOPage.setRecords(userDTOS);
        userDTOPage.setTotal(count);
        return userDTOPage;
    }

    @Override
    public UserDTO getUserId(Integer id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        User user = userMapper.selectOne(queryWrapper);
        UserDTO userDTO = ConvertUtil.convert(user, UserDTO.class);
        return userDTO;
    }

    @Override
    public UserDTO getUserByPhone(String phone) {
        User user = userMapper.getUserByPhone(phone);
        UserDTO userDTO = ConvertUtil.convert(user, UserDTO.class);
        return userDTO;
    }

    @Override
    public Boolean saveUser(UserDTO userDTO) {
        if (userDTO.getPhone() == null){
            log.info("手机号为空，无法注册");
            return false;
        }
        User user = ConvertUtil.convert(userDTO, User.class);
        user.setPassword(encoder.encode(user.getPassword()));
        // 重新设置用户昵称
        if (StringUtils.isNotBlank(user.getPhone())){
            String phone = userDTO.getPhone();
            user.setName("用户" + phone.substring(phone.length() - 4));
        }
        Date date = new Date();
        user.setCreateTime(date);
        user.setUpdateTime(date);
        int count = userMapper.insert(user);
        return count == 1;
    }

    @Override
    public boolean updateUser(UserDTO userDTO) {
        if (userDTO.getId() != null || userDTO.getId() <= 0){
            log.info("用户id为空，无法更新");
            return false;
        }
        User user = ConvertUtil.convert(userDTO, User.class);
        if (StringUtils.isNotBlank(userDTO.getPassword())){
            user.setPassword(encoder.encode(user.getPassword()));
        }
        user.setUpdateTime(new Date());
        int num = userMapper.updateById(user);
        return num == 1;
    }

    @Override
    public boolean isUpdatedPassword(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user == null){
            return false;
        }
        boolean matches = encoder.matches(user.getPhone(), user.getPassword());
        log.info("用户[{}]是否有修改过初始密码[{}]", userId, matches);
        return matches;
    }

    @Override
    public boolean setPassword(Integer userId, String password, String configPassword) {
        User user = userMapper.selectById(userId);
        if (user == null){
            log.info("用户不存在");
            return false;
        }
        if (password == null || configPassword == null){
            log.info("password或configPassword不能为空");
            return false;
        }
        if (!StringUtils.equals(password,configPassword)){
            log.info("password和configPassword两次输入的不一致");
            return false;
        }
        user.setPassword(encoder.encode(password));
        user.setUpdateTime(new Date());
        int count = userMapper.updateById(user);
        if (count < 1){
            log.info("用户设置密码失败");
            return false;
        }
        return true;
    }

    @Override
    public boolean updatePassword(Integer userId, String oldPassword, String newPassword, String configPassword) {
        if (oldPassword == null || newPassword == null || configPassword == null){
            log.info("oldPassword、password或configPassword不能为空");
            return false;
        }
        User user = userMapper.selectById(userId);
        if (user == null){
            log.info("用户不存在");
            return false;
        }
        if (!encoder.matches(oldPassword, user.getPassword())){
            log.info("原始密码有误");
            return false;
        }
        if (!StringUtils.equals(newPassword, configPassword)){
            log.info("password和configPassword两次输入的不一致");
            return false;
        }
        user.setUpdateTime(new Date());
        int count = userMapper.updateById(user);
        if (count < 1){
            log.info("用户更新密码失败");
            return false;
        }
        return true;
    }

    @Override
    public boolean forbidUser(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user == null){
            log.info("用户不存在");
            return false;
        }
        user.setUpdateTime(new Date());
        user.setIsDel(true);
        user.setStatus("DISABLE");
        int count = userMapper.updateById(user);
        if (count < 1){
            log.info("forbidUser失败");
        }
        if (count == 1){
            // TODO 发送mq消息，让用户登录失败

        }
        return count == 1;
    }
}
