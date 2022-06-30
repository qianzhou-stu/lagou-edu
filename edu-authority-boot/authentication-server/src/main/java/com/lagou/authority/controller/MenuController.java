package com.lagou.authority.controller;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lagou.auth.client.dto.AllocateRoleMenuDTO;
import com.lagou.auth.client.dto.MenuDTO;
import com.lagou.auth.client.dto.MenuNodeDTO;
import com.lagou.auth.client.param.MenuQueryParam;
import com.lagou.auth.client.provider.MenuProvider;
import com.lagou.authority.entity.bo.Menu;
import com.lagou.authority.service.IMenuService;
import com.lagou.common.entity.vo.Result;
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
import java.util.stream.Collectors;

/**
 * 菜单管理功能
 *
 * @author : zhouqian
 * @create 2020/7/13 11:18
 **/
@Api("菜单管理")
@Slf4j
@RestController
@RequestMapping("/menu")
public class MenuController implements MenuProvider {

    @Autowired
    private IMenuService menuService;

    @Override
    @ApiOperation(value = "保存或新增菜单", notes = "保存或新增菜单")
    @PostMapping(value = "/saveOrUpdate")
    public Result<Boolean> saveOrUpdate(@RequestBody MenuDTO menuDTO) {
        Menu menu = ConvertUtil.convert(menuDTO, Menu.class);
        Objects.requireNonNull(menu).setUpdatedTime(new Date());
        if (menu.getParentId() == -1){
            menu.setLevel(0);
        }else{
            Menu parentMenu = menuService.getById(menu.getParentId());
            menu.setLevel(parentMenu.getLevel() + 1);
        }
        return Result.success(menuService.saveOrUpdate(menu));
    }

    @Override
    @ApiOperation(value = "根据ID查询菜单", notes = "根据ID查询菜单")
    @GetMapping("/{id}")
    public Result<MenuDTO> getById(@PathVariable Integer id) {
        MenuDTO menuDTO = ConvertUtil.convert(menuService.getById(id), MenuDTO.class);
        return Result.success(menuDTO);
    }

    @Override
    @ApiOperation(value = "删除菜单", notes = "根据id删除菜单")
    @DeleteMapping(value = "/{id}")
    public Result<Boolean> deleteById(@PathVariable Integer id) {
        return Result.success(menuService.deleteWithAssociation(id));
    }

    @Override
    @ApiOperation(value = "获取所有菜单", notes = "获取所有菜单")
    @GetMapping("/getAll")
    public Result<List<MenuDTO>> getAll() {
        return Result.success(ConvertUtil.convertList(menuService.list(), MenuDTO.class));
    }

    @Override
    @ApiOperation(value = "根据查询条件分页查询菜单")
    @PostMapping("/getMenuPages")
    public Result<Page<MenuDTO>> getMenuPages(@RequestBody MenuQueryParam menuQueryParam) {
        log.info("Get menu list with paging. params:{}", menuQueryParam);
        Page<Menu> selectedPage = menuService.getMenuPages(menuQueryParam);
        Page<MenuDTO> menuDTOPage = new Page<>();
        BeanUtils.copyProperties(selectedPage, menuDTOPage);
        if (CollectionUtils.isNotEmpty(selectedPage.getRecords())){
            List<MenuDTO> menuDTOList = selectedPage.getRecords().stream().map(menu -> ConvertUtil.convert(menu, MenuDTO.class)).collect(Collectors.toList());
            menuDTOPage.setRecords(menuDTOList);
        }
        return Result.success(menuDTOPage);
    }

    @Override
    @ApiOperation("/是否显示开关")
    @PostMapping("/switchShown")
    public Result<Boolean> switchShown(@RequestBody MenuDTO menuDTO) {
        return Result.success(menuService.switchShown(ConvertUtil.convert(menuDTO, Menu.class)));
    }

    @Override
    @ApiOperation("返回菜单树")
    @GetMapping("/getMenuNodeList")
    public Result<List<MenuNodeDTO>> getMenuNodeList() {
        return Result.success(menuService.getMenuNodeList());
    }

    @Override
    @ApiOperation("获取角色分配的菜单列表")
    @GetMapping("/getByRoleId")
    public Result<List<MenuDTO>> getByRoleId(@RequestParam("roleId") Integer roleId) {
        return Result.success(ConvertUtil.convertList(menuService.getByRoleIdIgnoreIsShown(roleId), MenuDTO.class));
    }

    @Override
    @ApiOperation("给角色分配菜单")
    @PostMapping("/allocateRoleMenus")
    public Result<Boolean> allocateRoleMenus(@RequestBody AllocateRoleMenuDTO allocateRoleMenuDTO) {
        log.info("Allocate role menus with params:{}", allocateRoleMenuDTO);
        menuService.allocateRoleMenus(allocateRoleMenuDTO);
        return Result.success(Boolean.TRUE);
    }
}
