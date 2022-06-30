package com.lagou.authority.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.authority.entity.bo.Resource;
import com.lagou.authority.entity.bo.ResourceCategory;
import com.lagou.authority.mapper.ResourceCategoryMapper;
import com.lagou.authority.service.IResourceCategoryService;
import com.lagou.authority.service.IResourceService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResourceCategoryServiceImpl extends ServiceImpl<ResourceCategoryMapper, ResourceCategory> implements IResourceCategoryService {
    @Autowired
    private IResourceService resourceService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Integer id) {
        List<Resource> resources = resourceService.getByCategoryId(id);
        if (CollectionUtils.isNotEmpty(resources)) {
            throw new RuntimeException("资源分类下有资源信息，不允许删除!");
        }
        return this.removeById(id);
    }
}
