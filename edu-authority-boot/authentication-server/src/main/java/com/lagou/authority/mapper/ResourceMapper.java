package com.lagou.authority.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lagou.authority.entity.bo.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


/**
* @author zhouqian
* @description 针对表【resource(资源表)】的数据库操作Mapper
* @createDate 2022-06-29 15:17:34
* @Entity generator.domain.Resource
*/
@Repository
public interface ResourceMapper extends BaseMapper<Resource> {

    List<Resource> queryByRoleIds(@Param("roleIds") Set<Integer> roleIds);
}




