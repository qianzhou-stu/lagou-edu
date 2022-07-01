package com.lagou.authority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lagou.auth.client.dto.AllocateRoleResourceDTO;
import com.lagou.auth.client.param.ResourceQueryParam;
import com.lagou.authority.entity.bo.Resource;
import com.lagou.authority.entity.bo.RoleResourceRelation;
import com.lagou.authority.mapper.ResourceMapper;
import com.lagou.authority.service.IResourceService;
import com.lagou.authority.service.IRoleResourceService;
import com.lagou.authority.service.NewMvcRequestMatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {


    @Autowired
    private HandlerMappingIntrospector mvcHandlerMappingIntrospector;

    @Autowired
    private IRoleResourceService roleResourceService;

    @Autowired
    private ResourceMapper resourceMapper;

    /**
     * 系统中所有资源url转化成的RequestMatcher集合。用于匹配请求中的url
     */
    private static final Set<MvcRequestMatcher> resourceConfigAttributes = new HashSet<>();

    /**
     * 加载权限资源数据
     */
    @Override
    public synchronized void loadResource() {
        List<Resource> resources = resourceMapper.selectList(new QueryWrapper<>());
        resources.stream().forEach(resource -> resourceConfigAttributes.add(this.newMvcRequestMatcher(resource.getUrl())));
        log.debug("init resourceConfigAttributes:{}", resourceConfigAttributes);
    }

    /**
     * 根据请求url匹配资源url。能够在系统资源列表中匹配到资源的url，表明有权限访问
     *
     * @param authRequest
     * @return
     */
    @Override
    public boolean matchRequestUrl(HttpServletRequest authRequest) {
        // 能找到匹配的url就返回true。不比对method域
        return resourceConfigAttributes.stream().filter(requestMatcher -> requestMatcher.matches(authRequest)).count() > 0;
    }

    /**
     * 根据角色id列表关联查询资源表，找到角色拥有的资源
     *
     * @param roleIds
     * @return
     */
    @Override
    public List<Resource> queryByRoleIds(Set<Integer> roleIds) {
        return resourceMapper.queryByRoleIds(roleIds);
    }

    /**
     * 判断当前请求url是否存在资源池中，不存在直接返回false；
     * 再判断请求url是否匹配当前用户所拥有的资源，如果用户没有该资源访问权限，返回false;
     *
     * @param roleIds
     * @param request
     * @return
     */
    @Override
    public boolean matchUserResources(Set<Integer> roleIds, HttpServletRequest request) {
        boolean existInResources = this.matchRequestUrl(request);
        if (!existInResources) {
            log.info("url未在资源池中找到，拒绝访问: url:{}", request.getServletPath());
            return false;
        }
        List<Resource> resources = this.queryByRoleIds(roleIds);
        for (Resource resource : resources) {
            NewMvcRequestMatcher matcher = this.newMvcRequestMatcher(resource.getUrl());
            if (matcher.matches(request)){
                return true;
            }
        }
        return false;
    }


    /**
     * 保存和更新资源信息
     * @param resource
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(Resource resource){
        boolean success = super.saveOrUpdate(resource);
        if(success){
            // 更新缓存
            resourceConfigAttributes.add(this.newMvcRequestMatcher(resource.getUrl()));
        }
        return success;
    }

    /**
     * 根据id删除资源，同时删除角色-资源关联关系
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithAssociation(Integer id) {
        Resource resource = this.getById(id);
        roleResourceService.removeByResourceId(id);
        boolean success = this.removeById(id);
        if (success){
             resourceConfigAttributes.remove(this.newMvcRequestMatcher(resource.getUrl()));
        }
        return success;
    }

    /**
     * 分页查询资源列表
     *
     * @param resourceQueryParam
     * @return
     */
    @Override
    public Page<Resource> getResourcePages(ResourceQueryParam resourceQueryParam) {
        Page<Resource> page = new Page<>(resourceQueryParam.getCurrent(), resourceQueryParam.getSize());
        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(resourceQueryParam.getId()), "id", resourceQueryParam.getId())
                .like(StringUtils.isNotBlank(resourceQueryParam.getName()), "name", resourceQueryParam.getName())
                .like(StringUtils.isNotBlank(resourceQueryParam.getUrl()), "url", resourceQueryParam.getUrl())
                .eq(Objects.nonNull(resourceQueryParam.getCategoryId()), "category_id", resourceQueryParam.getCategoryId())
                .ge(Objects.nonNull(resourceQueryParam.getStartCreateTime()), "created_time", resourceQueryParam.getStartCreateTime())
                .le(Objects.nonNull(resourceQueryParam.getEndCreateTime()), "created_time", resourceQueryParam.getEndCreateTime())
                .orderByDesc("id");
        return this.page(page, queryWrapper);
    }

    /**
     * 根据分类id查询资源
     * @param categoryId
     * @return
     */
    @Override
    public List<Resource> getByCategoryId(Integer categoryId) {
        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        return this.list(queryWrapper);
    }

    /**
     * 给角色分配资源
     *
     * @param allocateRoleResourceDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void allocateRoleResources(AllocateRoleResourceDTO allocateRoleResourceDTO) {
        if (CollectionUtils.isEmpty(allocateRoleResourceDTO.getResourceIdList())){
            // 表示删除所有角色资源
            allocateRoleResourceDTO.setResourceIdList(Lists.newArrayList());
        }
        // 已分配的资源ID列表
        Set<Integer> roleResourceIds = roleResourceService.queryByRoleId(allocateRoleResourceDTO.getRoleId());
        // 当前准备分配的资源ID列表
        Set<Integer> allocateRoleResourceIds = Sets.newHashSet(allocateRoleResourceDTO.getResourceIdList());
        // 本次要删除的角色-资源关系
        Set<Integer> needToDel = roleResourceIds.stream().filter(resourceId -> !allocateRoleResourceIds.contains(resourceId)).collect(Collectors.toSet());
        // 本次要新增的角色-资源关系
        Set<Integer> needToInsert = allocateRoleResourceIds.stream().filter(resourceId -> !roleResourceIds.contains(resourceId)).collect(Collectors.toSet());

        if (CollectionUtils.isNotEmpty(needToDel)){
            roleResourceService.removeByRoleIdAndResourceIds(allocateRoleResourceDTO.getRoleId(), needToDel);
        }

        if (CollectionUtils.isNotEmpty(needToInsert)){
            List<RoleResourceRelation> roleResources = needToInsert.stream().map(resourceId -> {
                RoleResourceRelation roleResourceRelation = new RoleResourceRelation();
                roleResourceRelation.setRoleId(allocateRoleResourceDTO.getRoleId());
                roleResourceRelation.setResourceId(resourceId);
                roleResourceRelation.setCreatedBy(allocateRoleResourceDTO.getCreatedBy());
                roleResourceRelation.setUpdatedBy(allocateRoleResourceDTO.getUpdatedBy());
                Date date = new Date();
                roleResourceRelation.setCreatedTime(date);
                roleResourceRelation.setUpdatedTime(date);
                return roleResourceRelation;
            }).collect(Collectors.toList());
            roleResourceService.saveBatch(roleResources);
        }
    }


    private NewMvcRequestMatcher newMvcRequestMatcher(String url){
        return new NewMvcRequestMatcher(mvcHandlerMappingIntrospector, url, null);
    }
}
