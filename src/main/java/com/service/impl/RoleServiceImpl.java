package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.domain.Role;
import com.mapper.RoleMapper;
import com.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  服务实现类
 */
@Service
@Transactional
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    RoleMapper roleMapper;
    /**
     * 根据角色ID查询当前角色拥有的所有的权限或菜单ID
     * @param roleId
     * @return
     */
    @Override
    public ArrayList<Integer> queryRolePermissionIdsByRid(Integer roleId) {
        return getBaseMapper().queryRolePermissionIdsByRid(roleId);
    }

    /**
     * 保存角色和权限之间的关系
     * @param rid
     * @param ids
     */
    @Override
    public void saveRoleAndPermission(Integer rid, Integer[] ids) {
        //根据角色id删除角色和权限关系表数据
        roleMapper.deleteRoleAndPermissionById(rid);
        for (Integer pid : ids) {
            getBaseMapper().saveRoleAndPermission(rid,pid);
        }
    }

    /**
     * //根据用户ID查询当前用户拥有的角色id集合
     * @param uid
     */
    @Override
    public List<Integer> queryRoleUserIdsByUid(Integer uid) {
        return getBaseMapper().queryRoleUserIdsByUid(uid);
    }

    @Override
    public boolean removeById(Serializable id) {
        RoleMapper baseMapper = this.getBaseMapper();
        //通过角色id删除权限跟角色关系表
        baseMapper.deleteRoleAndPermissionById(id);
        //通过角色id删除用户跟角色关系表
        baseMapper.deleteRoleAndUserById(id);
        return super.removeById(id);
    }
}
