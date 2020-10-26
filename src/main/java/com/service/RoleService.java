package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.domain.Role;

import java.util.ArrayList;
import java.util.List;

/**
 *  服务类
 */
public interface RoleService extends IService<Role> {
    //根据角色ID查询当前角色拥有的所有的权限或菜单ID
    ArrayList<Integer> queryRolePermissionIdsByRid(Integer roleId);

    //保存角色和权限之间的关系
    void saveRoleAndPermission(Integer rid, Integer[] ids);

    //根据用户ID查询当前用户拥有的角色id集合
    List<Integer> queryRoleUserIdsByUid(Integer uid);

    //根据角色id删除角色和权限关系表数据
}
