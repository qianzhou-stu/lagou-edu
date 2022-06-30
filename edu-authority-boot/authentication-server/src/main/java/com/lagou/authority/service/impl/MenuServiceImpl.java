package com.lagou.authority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lagou.auth.client.dto.AllocateRoleMenuDTO;
import com.lagou.auth.client.dto.MenuNodeDTO;
import com.lagou.auth.client.param.MenuQueryParam;
import com.lagou.authority.common.contant.PubContants;
import com.lagou.authority.entity.bo.Menu;
import com.lagou.authority.entity.bo.RoleMenuRelation;
import com.lagou.authority.mapper.MenuMapper;
import com.lagou.authority.service.IMenuService;
import com.lagou.authority.service.IRoleMenuService;
import com.lagou.common.util.ConvertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private IRoleMenuService roleMenuService;

    /**
     * 删除菜单  删除菜单和角色关系
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithAssociation(Integer id) {
        deleteMenuCascade(id);
        return true;
    }

    /**
     * 级联删除所有子菜单，及子菜单绑定的角色关系
     *
     * @param id
     */
    private void deleteMenuCascade(Integer id) {
        List<Menu> menus = this.queryByParentId(id);
        if (CollectionUtils.isNotEmpty(menus)){
            menus.stream().forEach(menu -> deleteMenuCascade(menu.getId()));
        }
        roleMenuService.removeByMenuId(id);
        this.removeById(id);
    }

    /**
     * 根据父id删除菜单
     * @param parentId
     * @return
     */
    @Override
    public List<Menu> queryByParentId(Integer parentId) {
        return this.list(new QueryWrapper<Menu>().eq("parent_id", parentId));
    }

    /**
     * 分页查询菜单
     *
     * @param menuQueryParam
     * @return
     */
    @Override
    public Page<Menu> getMenuPages(MenuQueryParam menuQueryParam) {
        Page<Menu> page = new Page<>(menuQueryParam.getCurrent(), menuQueryParam.getSize());
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        // 判断是否要查询子菜单
        if (Objects.nonNull(menuQueryParam.getId()) && menuQueryParam.isQuerySubLevel()) {
            queryWrapper.eq("parent_id", menuQueryParam.getId());
        }else if (Objects.nonNull(menuQueryParam.getId()) && !menuQueryParam.isQuerySubLevel()){
            queryWrapper.eq("id", menuQueryParam.getId());
        }
        queryWrapper
                .like(StringUtils.isNotBlank(menuQueryParam.getName()), "name", menuQueryParam.getName())
                .eq(Objects.nonNull(menuQueryParam.getShown()), "shown", menuQueryParam.getShown())
                .ge(Objects.nonNull(menuQueryParam.getStartCreateTime()), "created_time", menuQueryParam.getStartCreateTime())
                .le(Objects.nonNull(menuQueryParam.getEndCreateTime()), "created_time", menuQueryParam.getEndCreateTime())
                .orderByDesc("id");
        return this.page(page, queryWrapper);
    }

    /**
     * 是否显示开关
     *
     * @param menu
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean switchShown(Menu menu) {
        if (Objects.isNull(menu)){
            return false;
        }
        UpdateWrapper<Menu> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("shown", menu.getShown());
        updateWrapper.set("updated_by", menu.getUpdatedBy());
        updateWrapper.set("updated_time", menu.getUpdatedTime());
        updateWrapper.eq("id", menu.getId());
        return this.update(updateWrapper);
    }

    /**
     * 获取树型结构的菜单列表，每个菜单如果有子菜单，则列出子菜单
     *
     * @return
     */
    @Override
    public List<MenuNodeDTO> getMenuNodeList() {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("level", PubContants.MENU_TOP_LEVEL);
        List<Menu> topLevelMenuList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(topLevelMenuList)){
             return Lists.newArrayList();
        }
        List<MenuNodeDTO> menuNodeList = topLevelMenuList.stream().map(menu -> fillMenuNode(menu)).collect(Collectors.toList());
        Collections.sort(menuNodeList);
        return menuNodeList;
    }
    /**
     * 查询子菜单
     *
     * @param menu
     */
    @Override
    public MenuNodeDTO fillMenuNode(Menu menu) {
        MenuNodeDTO menuNodeDTO = ConvertUtil.convert(menu, MenuNodeDTO.class);
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", Objects.requireNonNull(menuNodeDTO).getId());
        List<Menu> list = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return menuNodeDTO;
        }
        List<MenuNodeDTO> subMenuList = list.stream().map(subMenu -> fillMenuNode(subMenu)).collect(Collectors.toList());
        Collections.sort(subMenuList);
        menuNodeDTO.setSubMenuList(subMenuList);
        return menuNodeDTO;
    }

    @Override
    public List<Menu> getByRoleIdIgnoreIsShown(Integer roleId) {
        return menuMapper.getByRoleIdIgnoreIsShown(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void allocateRoleMenus(AllocateRoleMenuDTO allocateRoleMenuDTO) {
        if (CollectionUtils.isEmpty(allocateRoleMenuDTO.getMenuIdList())) {
            // 表示删除所有角色菜单
            allocateRoleMenuDTO.setMenuIdList(Lists.newArrayList());
        }
        // 角色已拥有的菜单
        Set<Integer> roleMenuIds = roleMenuService.queryByRoleIds(Sets.newHashSet(allocateRoleMenuDTO.getRoleId()));
        // 准备分配给角色的菜单，排除掉 -1(-1是不存在的id，是顶级菜单的父ID)
        Set<Integer> allocateRoleMenuIds = allocateRoleMenuDTO.getMenuIdList().stream().filter(menuId -> !Objects.equals(menuId, PubContants.MENU_TOP_LEVEL_PARENT_ID)).collect(Collectors.toSet());
        // 找出本次删除
        Set<Integer> needToDelMenus = roleMenuIds.stream().filter(id -> !allocateRoleMenuIds.contains(id)).collect(Collectors.toSet());
        // 找出本次新增的
        Set<Integer> needToInsertMenus = allocateRoleMenuIds.stream().filter(id -> !roleMenuIds.contains(id)).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(needToDelMenus)){
            roleMenuService.removeByRoleIdAndMenuIds(allocateRoleMenuDTO.getRoleId(), needToDelMenus);
        }
        if (CollectionUtils.isNotEmpty(needToInsertMenus)){
            List<RoleMenuRelation> roleMenus = needToInsertMenus.stream().map(menuId -> {
                RoleMenuRelation roleMenuRelation = new RoleMenuRelation();
                roleMenuRelation.setRoleId(allocateRoleMenuDTO.getRoleId());
                roleMenuRelation.setMenuId(menuId);
                Date date = new Date();
                roleMenuRelation.setCreatedTime(date);
                roleMenuRelation.setUpdatedTime(date);
                roleMenuRelation.setCreatedBy(allocateRoleMenuDTO.getCreatedBy());
                roleMenuRelation.setUpdatedBy(allocateRoleMenuDTO.getUpdatedBy());
                return roleMenuRelation;
            }).collect(Collectors.toList());
            roleMenuService.saveBatch(roleMenus);
        }
    }

    @Override
    public List<Menu> queryByRoleIds(Set<Integer> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Lists.newArrayList();
        }
        return menuMapper.queryByRoleIds(roleIds);
    }
}
