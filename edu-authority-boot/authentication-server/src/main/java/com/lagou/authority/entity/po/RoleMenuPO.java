package com.lagou.authority.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("role_menu_relation")
public class RoleMenuPO extends AuthorityBasePO {

    private Integer menuId;

    private Integer roleId;
}
