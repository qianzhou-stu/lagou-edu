package com.lagou.authority.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lagou.authority.entity.bo.Menu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


/**
* @author zhouqian
* @description 针对表【menu(菜单表)】的数据库操作Mapper
* @createDate 2022-06-29 15:17:34
* @Entity generator.domain.Menu
*/
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据角色id获取到菜单信息
     * @param roleId
     * @return
     */
    List<Menu> getByRoleIdIgnoreIsShown(@Param("roleId") Integer roleId);

    /**
     * 根据角色id查找对应的菜单信息
     * @param roleIds
     * @return
     */
    List<Menu> queryByRoleIds(@Param("roleIds") Set<Integer> roleIds);
}




