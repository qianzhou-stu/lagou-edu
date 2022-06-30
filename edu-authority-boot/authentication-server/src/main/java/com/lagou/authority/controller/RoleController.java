package com.lagou.authority.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lagou.auth.client.dto.AllocateUserRoleDTO;
import com.lagou.auth.client.dto.RoleDTO;
import com.lagou.auth.client.param.RoleQueryParam;
import com.lagou.auth.client.provider.RoleProvider;
import com.lagou.authority.entity.bo.Role;
import com.lagou.authority.entity.po.RolePO;
import com.lagou.authority.mapper.RoleMapper;
import com.lagou.authority.service.IRoleService;
import com.lagou.common.entity.vo.Result;
import com.lagou.common.response.EduEnum;
import com.lagou.common.util.ConvertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色接口
 *
 * @author : zhouqian
 * @create 2020/7/9 11:16
 **/
@Api("角色管理")
@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController implements RoleProvider {
    @Autowired
    private IRoleService roleService;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    @ApiOperation(value = "获取用户角色", notes = "根据用户userId查询用户角色")
    @GetMapping(value = "/getUserRoles")
    public Result<Set<RoleDTO>> getUserRoles(@RequestParam("userId") Integer userId) {
        List<Role> roles = roleService.queryByUserId(userId);
        Set<RoleDTO> roleSet = Sets.newHashSet();
        if (CollectionUtils.isNotEmpty(roles)){
            roles.stream().forEach(role -> {
                roleSet.add(ConvertUtil.convert(role, RoleDTO.class));
            });
        }
        return Result.success(roleSet);
    }

    @Override
    @ApiOperation(value = "创建或更新角色", notes = "创建或更新角色")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody RoleDTO roleDTO) {
        Role role = ConvertUtil.convert(roleDTO, Role.class);
        if (role == null){
            return Result.fail(EduEnum.INSERT_OR_UPDATE_FAILURE);
        }
        Date date = new Date();
        if (Objects.isNull(roleDTO.getId())){
            // 保存操作
           role.setUpdatedTime(date);
           role.setCreatedTime(date);
           roleMapper.insert(role);
        }else {
            // 更新操作
           role.setUpdatedTime(date);
           roleMapper.updateById(role);
        }
        return Result.success();
    }

    @Override
    @ApiOperation(value = "删除角色", notes = "根据ID删除角色")
    @DeleteMapping(value = "/{id}")
    public Result<Boolean> delete(@PathVariable("id") Integer id) {
        return Result.success(roleService.deleteWithAssociation(id));
    }

    @Override
    @ApiOperation(value = "查询角色", notes = "根据ID查询角色")
    @GetMapping("/{id}")
    public Result<RoleDTO> getById(@PathVariable Integer id) {
        RolePO role = roleService.get(id);
        return Result.success(ConvertUtil.convert(role, RoleDTO.class));
    }

    @Override
    @ApiOperation(value = "查询所有角色", notes = "查询所有角色")
    @GetMapping("/getAll")
    public Result<List<RoleDTO>> getAll() {
        List<Role> roles = roleService.getAll();
        List<RoleDTO> roleDTOList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(roleDTOList)){
            roles.stream().forEach(
                    role -> {
                        roleDTOList.add(ConvertUtil.convert(role, RoleDTO.class));
                    }
            );
        }
        return Result.success(roleDTOList);
    }

    @Override
    @ApiOperation(value = "给用户分配角色", notes = "给用户分配角色，可同时分配多个角色")
    @PostMapping("/allocateUserRoles")
    public Result<Boolean> allocateUserRoles(@RequestBody AllocateUserRoleDTO allocateUserRoleDTO) {
        log.info("Allocate user roles with params:{}", allocateUserRoleDTO);
        roleService.allocateUserRoles(allocateUserRoleDTO);
        return Result.success(Boolean.TRUE);
    }

    @Override
    @ApiOperation(value = "分布查询角色列表")
    @PostMapping("/getRolePages")
    public Result<Page<RoleDTO>> getRolePages(@RequestBody RoleQueryParam roleQueryParam) {
        Page<Role> rolePages = roleService.getRolePages(roleQueryParam);
        List<Role> roleList = rolePages.getRecords();
        Page<RoleDTO> roleDTOPages = new Page<>();
        BeanUtils.copyProperties(rolePages, roleDTOPages);
        if (CollectionUtils.isNotEmpty(rolePages.getRecords())) {
            List<RoleDTO> records =roleList.stream()
                    .map(role -> ConvertUtil.convert(role, RoleDTO.class)).collect(Collectors.toList());
            roleDTOPages.setRecords(records);
        }
        return Result.success(roleDTOPages);
    }
}
