package com.lagou.authority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.authority.entity.bo.UserRoleRelation;
import com.lagou.authority.mapper.UserRoleRelationMapper;
import com.lagou.authority.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleRelationMapper, UserRoleRelation> implements IUserRoleService {

    @Autowired
    private UserRoleRelationMapper userRoleRelationMapper;

    @Override
    public Set<Integer> queryByUserId(Integer userId) {
        QueryWrapper<UserRoleRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<UserRoleRelation> userRoleRelations = userRoleRelationMapper.selectList(queryWrapper);
        return userRoleRelations.stream().map(UserRoleRelation::getRoleId).collect(Collectors.toSet());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByUserId(Integer userId) {
        QueryWrapper<UserRoleRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserRoleRelation::getUserId, userId);
        return remove(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByRoleId(Integer roleId) {
        QueryWrapper<UserRoleRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserRoleRelation::getRoleId, roleId);
        return remove(queryWrapper);
    }

    @Override
    public boolean removeByRoleIds(Integer userId, Set<Integer> roleIds) {
        QueryWrapper<UserRoleRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).in("role_id", roleIds);
        return this.remove(queryWrapper);
    }
}
