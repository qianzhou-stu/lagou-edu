package com.lagou.authority.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.auth.client.dto.AllocateRoleMenuDTO;
import com.lagou.auth.client.dto.MenuNodeDTO;
import com.lagou.auth.client.param.MenuQueryParam;
import com.lagou.authority.entity.bo.Menu;

import java.util.List;
import java.util.Set;

public interface IMenuService extends IService<Menu> {

    /**
     * 删除菜单  删除菜单和角色关系
     * @param id
     * @return
     */
    Boolean deleteWithAssociation(Integer id);

    /**
     * 根据父id删除菜单
     * @param parentId
     * @return
     */
    List<Menu> queryByParentId(Integer parentId);

    /**
     * 分页查询菜单
     *
     * @param menuQueryParam
     * @return
     */
    Page<Menu> getMenuPages(MenuQueryParam menuQueryParam);

    /**
     * 是否显示开关
     *
     * @param menu
     * @return
     */
    Boolean switchShown(Menu menu);

    /**
     * 获取树型结构的菜单列表，每个菜单如果有子菜单，则列出子菜单
     *
     * @return
     */
    List<MenuNodeDTO> getMenuNodeList();

    /**
     * 查询子菜单
     *
     * @param menu
     */
    public MenuNodeDTO fillMenuNode(Menu menu);

    /**
     * 获取角色菜单列表，并忽略是否显示字段值
     *
     * @param roleId
     * @return
     */
    List<Menu> getByRoleIdIgnoreIsShown(Integer roleId);

    /**
     * 给角色分配菜单
     *
     * @param allocateRoleMenuDTO
     */
    void allocateRoleMenus(AllocateRoleMenuDTO allocateRoleMenuDTO);

    /**
     * 根据角色id查找菜单
     * @param roleIds
     * @return
     */
    List<Menu> queryByRoleIds(Set<Integer> roleIds);
}
