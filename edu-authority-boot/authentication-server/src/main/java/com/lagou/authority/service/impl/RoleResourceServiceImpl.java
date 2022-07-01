package com.lagou.authority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.authority.entity.bo.RoleResourceRelation;
import com.lagou.authority.mapper.RoleResourceRelationMapper;
import com.lagou.authority.service.IRoleResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceRelationMapper, RoleResourceRelation> implements IRoleResourceService {

    @Autowired
    private RoleResourceRelationMapper roleResourceRelationMapper;

    @Override
    public boolean removeByRoleId(Integer roleId) {
        return false;
    }

    @Override
    public Set<Integer> queryByRoleId(Integer roleId) {
        QueryWrapper<RoleResourceRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        List<RoleResourceRelation> userRoleList = roleResourceRelationMapper.selectList(queryWrapper);
        return userRoleList.stream().map(RoleResourceRelation::getResourceId).collect(Collectors.toSet());
    }

    @Override
    public List<RoleResourceRelation> queryByRoleIds(Set<Integer> roleIds) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByResourceId(Integer resourceId) {
        QueryWrapper<RoleResourceRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleResourceRelation::getResourceId, resourceId);
        return this.remove(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByRoleIdAndResourceIds(Integer roleId, Set<Integer> resourceIds) {
        QueryWrapper<RoleResourceRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId).in("resource_id", resourceIds);
        this.remove(queryWrapper);
    }
}
