package com.lagou.authority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.lagou.auth.client.dto.AllocateUserRoleDTO;
import com.lagou.auth.client.param.RoleQueryParam;
import com.lagou.authority.entity.bo.Role;
import com.lagou.authority.entity.bo.UserRoleRelation;
import com.lagou.authority.entity.po.RolePO;
import com.lagou.authority.mapper.RoleMapper;
import com.lagou.authority.service.IRoleMenuService;
import com.lagou.authority.service.IRoleResourceService;
import com.lagou.authority.service.IRoleService;
import com.lagou.authority.service.IUserRoleService;
import com.lagou.common.util.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private IRoleMenuService roleMenuService;
    @Autowired
    private IRoleResourceService resourceService;


    @Override
    public List<Role> queryByUserId(Integer userId) {
        Set<Integer> roleIds = userRoleService.queryByUserId(userId);
        if (CollectionUtils.isEmpty(roleIds)){
            return Lists.newArrayList();
        }
        return roleMapper.selectBatchIds(roleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithAssociation(Integer id) {
        roleMenuService.removeByMenuId(id);
        resourceService.removeByRoleId(id);
        resourceService.removeByRoleId(id);
        return roleMapper.deleteById(id) > 0;
    }

    @Override
    public RolePO get(Integer id) {
        Role role = this.getById(id);
        RolePO rolePO = ConvertUtil.convert(role, RolePO.class);
        if (Objects.isNull(role)){
            return null;
        }
        Objects.requireNonNull(rolePO).setResourceIds(resourceService.queryByRoleId(id));
        return rolePO;
    }

    @Override
    public List<Role> getAll() {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        return roleMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void allocateUserRoles(AllocateUserRoleDTO allocateUserRoleDTO) {
        if (CollectionUtils.isEmpty(allocateUserRoleDTO.getRoleIdList())){
            // 如果id列表为空，表示要删除所有角色
            allocateUserRoleDTO.setRoleIdList(Lists.newArrayList());
        }
        // 用户已拥有的角色(id列表）
        Set<Integer> userRoleIds = userRoleService.queryByUserId(allocateUserRoleDTO.getUserId());
        // 当前要分配给用户的角色(id列表)
        Set<Integer> allocatedRoleIds = allocateUserRoleDTO.getRoleIdList().stream().collect(Collectors.toSet());
        // 找出本次删除的
        Set<Integer> needToDelRoles = userRoleIds.stream().filter(id -> !allocatedRoleIds.contains(id)).collect(Collectors.toSet());
        // 找出本次新增的
        Set<Integer> needToInsertRoles = allocatedRoleIds.stream().filter(id -> !userRoleIds.contains(id)).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(needToDelRoles)){
            userRoleService.removeByRoleIds(allocateUserRoleDTO.getUserId(), needToDelRoles);
        }
        if (CollectionUtils.isNotEmpty(needToInsertRoles)){
            List<UserRoleRelation> newUserRoles = needToInsertRoles.stream().map(roleId -> {
                UserRoleRelation userRole = new UserRoleRelation();
                userRole.setUserId(allocateUserRoleDTO.getUserId());
                userRole.setRoleId(roleId);
                userRole.setCreatedBy(allocateUserRoleDTO.getCreatedBy());
                userRole.setUpdatedBy(allocateUserRoleDTO.getUpdatedBy());
                Date date = new Date();
                userRole.setCreatedTime(date);
                userRole.setUpdatedTime(date);
                return userRole;
            }).collect(Collectors.toList());
            userRoleService.saveBatch(newUserRoles);
        }
    }

    @Override
    public Page<Role> getRolePages(RoleQueryParam roleQueryParam) {
        Page<Role> page = new Page<>(roleQueryParam.getCurrent(), roleQueryParam.getSize());
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(roleQueryParam.getId()), "id", roleQueryParam.getId())
                .like(StringUtils.isNotBlank(roleQueryParam.getName()), "name", roleQueryParam.getName())
                .eq(StringUtils.isNotBlank(roleQueryParam.getCode()), "code", roleQueryParam.getCode())
                .ge(Objects.nonNull(roleQueryParam.getStartCreateTime()), "create_time", roleQueryParam.getStartCreateTime())
                .le(Objects.nonNull(roleQueryParam.getEndCreateTime()), "create_time", roleQueryParam.getEndCreateTime())
                .orderByDesc("id");
        return this.page(page, queryWrapper);
    }
}
