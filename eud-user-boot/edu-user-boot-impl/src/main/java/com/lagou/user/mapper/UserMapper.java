package com.lagou.user.mapper;

import com.lagou.user.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhouqian
 * @since 2022-04-05
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    User getUserByPhone(String phone);
}
