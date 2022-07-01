package com.lagou.authority.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.authority.entity.bo.RoleMenuRelation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface IRoleMenuService extends IService<RoleMenuRelation> {

    /**
     * 根据角色id列表查询菜单ID列表
     *
     * @param roleIds 角色id列表，如果为空，则返回空
     * @return
     */
    Set<Integer> queryByRoleIds(Set<Integer> roleIds);

    /**
     * 根据角色id删除角色-菜单关系
     *
     * @param roleId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    boolean removeByRoleId(Integer roleId);

    /**
     * 根据菜单id删除角色-菜单关系
     *
     * @param menuId
     * @return
     */
    boolean removeByMenuId(Integer menuId);


    boolean removeByRoleIdAndMenuIds(Integer roleId, Set<Integer> needToDelMenus);
}
