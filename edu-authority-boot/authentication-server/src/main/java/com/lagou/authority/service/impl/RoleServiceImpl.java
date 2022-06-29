package com.lagou.authority.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.authority.entity.po.Roles;
import com.lagou.authority.mapper.RolesMapper;
import com.lagou.authority.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RolesMapper, Roles> implements IRoleService {


}
