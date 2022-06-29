package com.lagou.authority.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lagou.auth.client.dto.AllocateUserRoleDTO;
import com.lagou.auth.client.dto.RoleDTO;
import com.lagou.auth.client.param.RoleQueryParam;
import com.lagou.auth.client.provider.RoleProvider;
import com.lagou.authority.service.IRoleService;
import com.lagou.common.entity.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

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

    @Override
    @ApiOperation(value = "获取用户角色", notes = "根据用户userId查询用户角色")
    @GetMapping(value = "/getUserRoles")
    public Result<Set<RoleDTO>> getUserRoles(Integer userId) {

        return null;
    }

    @Override
    public Result<Boolean> saveOrUpdate(RoleDTO roleDTO) {
        return null;
    }

    @Override
    public Result<Boolean> delete(Integer id) {
        return null;
    }

    @Override
    public Result<RoleDTO> getById(Integer id) {
        return null;
    }

    @Override
    public Result<List<RoleDTO>> getAll() {
        return null;
    }

    @Override
    public Result<Boolean> allocateUserRoles(AllocateUserRoleDTO allocateUserRoleDTO) {
        return null;
    }

    @Override
    public Result<Page<RoleDTO>> getRolePages(RoleQueryParam roleQueryParam) {
        return null;
    }
}
