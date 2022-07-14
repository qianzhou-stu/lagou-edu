package com.lagou.boss.rest;

import com.google.common.collect.Lists;
import com.lagou.auth.client.dto.AllocateUserRoleDTO;
import com.lagou.auth.client.dto.RoleDTO;
import com.lagou.auth.client.dto.UserRoleDTO;
import com.lagou.auth.client.provider.RoleProvider;
import com.lagou.boss.common.UserManager;
import com.lagou.boss.entity.form.AllocateUserRoleForm;
import com.lagou.boss.entity.form.RoleForm;
import com.lagou.common.entity.vo.Result;
import com.lagou.common.util.ConvertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/role")
@Slf4j
@Api(tags = "角色管理", produces = "application/json")
public class RoleController {

    @Autowired
    private RoleProvider roleProvider;

    @ApiOperation(value = "查询用户角色")
    @GetMapping(value = "/user/{userId}")
    public Result<Set<RoleDTO>> getUserRoles(@PathVariable Integer userId){
        log.debug("getUserRoles with userId:{}", userId);
        return roleProvider.getUserRoles(userId);
    }

    @ApiOperation(value = "保存或者更新角色")
    @PostMapping("/saveOrUpdate")
    public Result<Boolean> saveOrUpdate(@RequestBody RoleForm roleForm){
        RoleDTO roleDTO = ConvertUtil.convert(roleForm, RoleDTO.class);
        if (Objects.isNull(Objects.requireNonNull(roleDTO).getId())){
            roleDTO.setCreatedBy(UserManager.getUserName());
            roleDTO.setCreatedTime(new Date());
        }
        roleDTO.setUpdatedBy(UserManager.getUserName());
        roleDTO.setUpdatedTime(new Date());
        return roleProvider.saveOrUpdate(roleDTO);
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping(value = "/{id}")
    public Result<Boolean> delete(@PathVariable("id") Integer id){
        return roleProvider.delete(id);
    }

    @ApiOperation(value = "获取角色")
    @GetMapping(value = "/{id}")
    public Result<RoleDTO> getById(@PathVariable("id") Integer id){
        return roleProvider.getById(id);
    }

    @ApiOperation(value = "获取所有的角色")
    @GetMapping(value = "/all")
    public Result<List<RoleDTO>> getAll(){
        return roleProvider.getAll();
    }

    @ApiOperation(value = "给用户分配角色")
    @PostMapping("/allocateUserRoles")
    public Result allocateUserRoles(@RequestBody AllocateUserRoleForm allocateUserRoleForm){
        log.info("Allocate user roles. params:{}", allocateUserRoleForm);
        if (Objects.isNull(allocateUserRoleForm.getUserId())){
            return Result.fail("用户ID不能为空");
        }
        AllocateUserRoleDTO allocateUserRoleDTO = ConvertUtil.convert(allocateUserRoleForm, AllocateUserRoleDTO.class);
        Objects.requireNonNull(allocateUserRoleDTO).setCreatedBy(UserManager.getUserName());
        Objects.requireNonNull(allocateUserRoleDTO).setUpdatedBy(UserManager.getUserName());
        return roleProvider.allocateUserRoles(allocateUserRoleDTO);
    }

    @ApiOperation(value = "列出所有角色，并且标记当前用户ID是否拥有该角色",
            notes = "在分配角色时，展示所有有效角色，并且标记出用户当前已拥有的角色")
    @GetMapping("/getRolesWithUserPermission")
    public Result<List<UserRoleDTO>> getRolesWithUserPermission(@RequestParam Integer userId) {
        Result<List<RoleDTO>> allRolesResult = roleProvider.getAll();
        Result<Set<RoleDTO>> userRolesResult = roleProvider.getUserRoles(userId);
        if (!allRolesResult.isSuccess() || !userRolesResult.isSuccess()) {
            return Result.fail();
        }
        List<RoleDTO> roles = allRolesResult.getData();
        if (CollectionUtils.isEmpty(roles)) {
            return Result.success(Lists.newArrayList());
        }
        Set<RoleDTO> userRoles = userRolesResult.getData();
        List<UserRoleDTO> rolesWithUserPermission = roles.stream().map(roleDTO -> {
            return new UserRoleDTO(roleDTO.getId(), roleDTO.getName(), userRoles.contains(roleDTO));
        }).collect(Collectors.toList());
        return Result.success(rolesWithUserPermission);
    }
}
