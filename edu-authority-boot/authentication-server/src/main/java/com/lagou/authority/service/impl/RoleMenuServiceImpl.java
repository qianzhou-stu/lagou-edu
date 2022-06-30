package com.lagou.authority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.authority.entity.bo.Menu;
import com.lagou.authority.entity.bo.RoleMenuRelation;
import com.lagou.authority.mapper.RoleMenuRelationMapper;
import com.lagou.authority.service.IRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuRelationMapper, RoleMenuRelation> implements IRoleMenuService {
    @Override
    public Set<Integer> queryByRoleIds(Set<Integer> roleIds) {
        return null;
    }

    @Override
    public boolean removeByRoleId(Integer roleId) {
        return false;
    }

    @Override
    public boolean removeByMenuId(Integer menuId) {
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByRoleIdAndMenuIds(Integer roleId, Set<Integer> needToDelMenus) {
        QueryWrapper<RoleMenuRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleMenuRelation::getRoleId, roleId);
        queryWrapper.lambda().in(RoleMenuRelation::getMenuId, needToDelMenus);
        return this.remove(queryWrapper);
    }
}
