package com.lagou.boss.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.lagou.auth.client.dto.AllocateRoleResourceDTO;
import com.lagou.auth.client.dto.ResourceCategoryDTO;
import com.lagou.auth.client.dto.ResourceCategoryNodeDTO;
import com.lagou.auth.client.dto.ResourceDTO;
import com.lagou.auth.client.param.ResourceQueryParam;
import com.lagou.auth.client.provider.ResourceProvider;
import com.lagou.boss.common.UserManager;
import com.lagou.boss.entity.form.AllocateRoleResourceForm;
import com.lagou.boss.entity.form.ResourceCategoryForm;
import com.lagou.boss.entity.form.ResourceForm;
import com.lagou.common.entity.vo.Result;
import com.lagou.common.util.ConvertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Api(tags = "资源管理", produces = "application/json")
@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Autowired
    private ResourceProvider resourceProvider;

    @ApiOperation(value = "保存或者更新资源")
    @PostMapping("/saveOrUpdate")
    public Result<Boolean> saveOrUpdate(@RequestBody ResourceForm resourceForm){
        ResourceDTO resourceDTO = ConvertUtil.convert(resourceForm, ResourceDTO.class);
        if (Objects.isNull(Objects.requireNonNull(resourceDTO).getId())){
            resourceDTO.setCreatedBy(UserManager.getUserName());
            resourceDTO.setCreatedTime(new Date());
        }
        resourceDTO.setUpdatedBy(UserManager.getUserName());
        resourceDTO.setUpdatedTime(new Date());
        return resourceProvider.saveOrUpdate(resourceDTO);
    }

    @ApiOperation(value = "获取资源")
    @GetMapping(value = "/{id}")
    public Result<ResourceDTO> getById(@PathVariable("id") Integer id){
        return resourceProvider.getById(id);
    }

    @ApiOperation(value = "删除资源")
    @DeleteMapping(value = "/{id}")
    public Result<Boolean> delete(@PathVariable("id") Integer id){
        return resourceProvider.delete(id);
    }

    @ApiOperation(value = "获取所有资源")
    @GetMapping("/getAll")
    public Result<Page<ResourceDTO>> getResourcePages(@RequestBody ResourceQueryParam resourceQueryParam){
        return resourceProvider.getResourcePages(resourceQueryParam);
    }

    @ApiOperation(value = "查询资源分类列表", notes = "如果不传资源id值，则不标记是否选中该分类。传了id，会根据资源分类将分类列表中对应的分类标记为选中")
    @ApiImplicitParams({
            @ApiImplicitParam(type = "form", name = "resourceId", value = "资源ID", required = false, paramType = "Integer")
    })
    @GetMapping("/category/getAll")
    public Result<List<ResourceCategoryDTO>> getCategories(@RequestParam(value = "resourceId", required = false) Integer resourceId) {
        Result<List<ResourceCategoryDTO>> result = resourceProvider.getAllCategories();
        if (!result.isSuccess() || CollectionUtils.isEmpty(result.getData())){
            return Result.success(Lists.newArrayList());
        }
        List<ResourceCategoryDTO> categoryDTOList = result.getData();
        if (Objects.nonNull(resourceId)){
            Result<ResourceDTO> res = resourceProvider.getById(resourceId);
            if (res.isSuccess() && Objects.nonNull(res.getData())){
                categoryDTOList.forEach(resourceCategoryDTO -> {
                    if (Objects.equals(resourceCategoryDTO.getId(), res.getData().getCategoryId())){
                        resourceCategoryDTO.setSelected(true);
                    }
                });
            }
        }
        return Result.success(categoryDTOList);
    }

    @ApiOperation(value = "保存或更新资源分类")
    @PostMapping("/category/saveOrderUpdate")
    public Result<Boolean> saveOrUpdateCategory(@RequestBody ResourceCategoryForm resourceCategoryForm) {
        ResourceCategoryDTO resourceCategoryDTO = ConvertUtil.convert(resourceCategoryForm, ResourceCategoryDTO.class);
        if (Objects.isNull(Objects.requireNonNull(resourceCategoryDTO).getId())) {
            resourceCategoryDTO.setCreatedBy(UserManager.getUserName());
            resourceCategoryDTO.setCreatedTime(new Date());
        }
        resourceCategoryDTO.setUpdatedBy(UserManager.getUserName());
        resourceCategoryDTO.setUpdatedTime(new Date());
        return resourceProvider.saveOrUpdateCategory(resourceCategoryDTO);
    }

    @ApiOperation(value = "删除资源分类，如果资源分类下有资源，不允许删除")
    @DeleteMapping("/category/{id}")
    public Result<Boolean> deleteById(@PathVariable("id") Integer id) {
        return resourceProvider.deleteCategoryById(id);
    }

    @ApiOperation(value = "给角色分配资源")
    @PostMapping("/allocateRoleResources")
    public Result<Boolean> allocateRoleResources(@RequestBody AllocateRoleResourceForm allocateRoleResourceForm) {
        log.info("Allocate role resources, params:{}", allocateRoleResourceForm);
        if (Objects.isNull(allocateRoleResourceForm.getRoleId())) {
            return Result.fail("角色ID不能为空");
        }
        AllocateRoleResourceDTO allocateRoleMenuDTO = ConvertUtil.convert(allocateRoleResourceForm, AllocateRoleResourceDTO.class);
        Objects.requireNonNull(allocateRoleMenuDTO).setCreatedBy(UserManager.getUserName());
        Objects.requireNonNull(allocateRoleMenuDTO).setUpdatedBy(UserManager.getUserName());
        return resourceProvider.allocateRoleResources(allocateRoleMenuDTO);
    }

    @ApiOperation(value = "获取角色拥有的资源列表", notes = "在给角色分配资源时，跳转到角色-资源列表页，并标记哪些资源已分配给该角色")
    @GetMapping("/getRoleResources")
    public Result<List<ResourceCategoryNodeDTO>> getRoleResources(@RequestParam Integer roleId) {
        return resourceProvider.getRoleResources(roleId);
    }
}
