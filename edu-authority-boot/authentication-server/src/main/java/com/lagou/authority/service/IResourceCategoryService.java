package com.lagou.authority.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.authority.entity.bo.ResourceCategory;

public interface IResourceCategoryService extends IService<ResourceCategory> {

    Boolean deleteById(Integer id);
}
