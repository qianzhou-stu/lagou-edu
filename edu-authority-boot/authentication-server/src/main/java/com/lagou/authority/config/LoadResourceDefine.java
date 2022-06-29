package com.lagou.authority.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoadResourceDefine implements InitializingBean {
    @Autowired
    //private IResourceService resourceService;
    @Override
    public void afterPropertiesSet() throws Exception {
       // resourceService.loadResource();
    }
}
