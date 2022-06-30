package com.lagou.authority.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.lagou.auth.client.dto.AllocateRoleResourceDTO;
import com.lagou.auth.client.dto.ResourceCategoryDTO;
import com.lagou.auth.client.dto.ResourceCategoryNodeDTO;
import com.lagou.auth.client.dto.ResourceDTO;
import com.lagou.auth.client.param.ResourceQueryParam;
import com.lagou.auth.client.provider.ResourceProvider;
import com.lagou.authority.entity.bo.Resource;
import com.lagou.authority.entity.bo.ResourceCategory;
import com.lagou.authority.service.IResourceCategoryService;
import com.lagou.authority.service.IResourceService;
import com.lagou.authority.service.IRoleResourceService;
import com.lagou.common.entity.vo.Result;
import com.lagou.common.util.ConvertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 资源管理
 *
 * @author : zhouqian
 * @create 2020/7/13 11:19
 **/
@Api("资源管理")
@Slf4j
@RestController
@RequestMapping("/resource")
public class ResourceController implements ResourceProvider {

    @Autowired
    private IResourceService resourceService;

    @Autowired
    private IRoleResourceService roleResourceService;

    @Autowired
    private IResourceCategoryService resourceCategoryService;

    @Override
    @ApiOperation(value = "保存或更新资源", notes = "保存或更新资源")
    @PostMapping("/saveOrUpdate")
    public Result<Boolean> saveOrUpdate(@RequestBody ResourceDTO resourceDTO) {
        Resource resource = ConvertUtil.convert(resourceDTO, Resource.class);
        return Result.success(resourceService.saveOrUpdate(resource));
    }

    @Override
    @ApiOperation(value = "获取资源", notes = "获取指定资源信息")
    @GetMapping(value = "/{id}")
    public Result<ResourceDTO> getById(@PathVariable Integer id) {
        Resource resource = resourceService.getById(id);
        return Result.success(ConvertUtil.convert(resource, ResourceDTO.class));
    }

    @Override
    @ApiOperation(value = "删除资源", notes = "根据url的id来指定删除对象")
    @DeleteMapping(value = "/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        return Result.success(resourceService.deleteWithAssociation(id));
    }

    @Override
    @ApiOperation(value = "查询所有资源", notes = "查询所有资源信息")
    @GetMapping(value = "/getAll")
    public Result<List<ResourceDTO>> getAll() {
        List<Resource> resources = resourceService.list();
        List<ResourceDTO> resourceDTOList = ConvertUtil.convertList(resources, ResourceDTO.class);
        return Result.success(resourceDTOList);
    }

    @Override
    @ApiOperation(value = "按条件分页查询资源")
    @PostMapping("/getResourcePages")
    public Result<Page<ResourceDTO>> getResourcePages(@RequestBody ResourceQueryParam resourceQueryParam) {
        Page<Resource> selectedPage = resourceService.getResourcePages(resourceQueryParam);
        Page<ResourceDTO> resourceDTOPage = new Page<>();
        BeanUtils.copyProperties(selectedPage, resourceDTOPage);
        List<ResourceDTO> resourceDTOS = selectedPage.getRecords().stream().map(resource -> ConvertUtil.convert(resource, ResourceDTO.class)).collect(Collectors.toList());
        resourceDTOPage.setRecords(resourceDTOS);
        return Result.success(resourceDTOS);
    }

    @Override
    @ApiOperation(value = "保存或更新资源分类")
    @PostMapping("/category/saveOrderUpdate")
    public Result<Boolean> saveOrUpdateCategory(ResourceCategoryDTO resourceCategoryDTO) {
        ResourceCategory category = ConvertUtil.convert(resourceCategoryDTO, ResourceCategory.class);
        return Result.success(resourceCategoryService.saveOrUpdate(category));
    }

    @Override
    @ApiOperation(value = "删除资源分类，如果资源分类下有资源，不允许删除")
    @DeleteMapping("/category/{id}")
    public Result<Boolean> deleteCategoryById(@PathVariable("id") Integer id) {
        try {
            return Result.success(resourceCategoryService.deleteById(id));
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @Override
    @ApiOperation(value = "查询所有资源分类列表")
    @GetMapping("/category/getAll")
    public Result<List<ResourceCategoryDTO>> getAllCategories() {
        List<ResourceCategory> list = resourceCategoryService.list();
        return Result.success(ConvertUtil.convertList(list, ResourceCategoryDTO.class));
    }

    @Override
    @ApiOperation(value = "给角色分配资源")
    @PostMapping("/allocateRoleResources")
    public Result<Boolean> allocateRoleResources(@RequestBody AllocateRoleResourceDTO allocateRoleResourceDTO) {
        log.info("Allocate role resources, params:{}", allocateRoleResourceDTO);
        resourceService.allocateRoleResources(allocateRoleResourceDTO);
        return Result.success(Boolean.TRUE);
    }

    @Override
    @ApiOperation(value = "查询角色所拥有的资源")
    @GetMapping("/getRoleResources")
    public Result<List<ResourceCategoryNodeDTO>> getRoleResources(@RequestParam Integer roleId) {
        List<ResourceCategory> categories = resourceCategoryService.list();
        if (CollectionUtils.isEmpty(categories)) {
            return Result.success(Lists.newArrayList());
        }
        // 角色已分配到的资源ID列表
        Set<Integer> roleResourceIds = roleResourceService.queryByRoleId(roleId);
        List<ResourceCategoryNodeDTO> categoryNodeDTOList = categories.stream().map(category -> buildResourceCategoryNode(category, roleResourceIds)).collect(Collectors.toList());
        Collections.sort(categoryNodeDTOList);
        return Result.success(categoryNodeDTOList);
    }

    /**
     * 填充资源分类信息
     *
     * @param category
     * @param roleResourceIds
     * @return
     */
    private ResourceCategoryNodeDTO buildResourceCategoryNode(ResourceCategory category, Set<Integer> roleResourceIds) {
        ResourceCategoryNodeDTO categoryNodeDTO = ConvertUtil.convert(category, ResourceCategoryNodeDTO.class);
        // 查询该分类下所有资源列表
        List<Resource> resources = resourceService.getByCategoryId(category.getId());
        if (CollectionUtils.isEmpty(resources)) {
            return categoryNodeDTO;
        }
        List<ResourceDTO> resourceDTOList = resources.stream().map(resource -> {
            ResourceDTO resourceDTO = ConvertUtil.convert(resource, ResourceDTO.class);
            Objects.requireNonNull(resourceDTO).setSelected(roleResourceIds.contains(resource.getId()));
            return resourceDTO;
        }).collect(Collectors.toList());
        Objects.requireNonNull(categoryNodeDTO).setResourceList(resourceDTOList);
        categoryNodeDTO.setSelected(resourceDTOList.stream().filter(resourceDTO -> resourceDTO.isSelected()).count() > 0);
        return categoryNodeDTO;
    }
}
