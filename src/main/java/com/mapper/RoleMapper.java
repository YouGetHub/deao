package com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.domain.Role;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper接口
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据角色ID查询当前角色拥有的所有的权限或菜单ID
     * @param roleId
     * @return
     */
    ArrayList<Integer> queryRolePermissionIdsByRid(@Param("roleId")Integer roleId);

    /**
     * 通过角色id删除权限跟角色关系表
     * @param id
     */
    void deleteRoleAndPermissionById(@Param("id") Serializable id);

    /**
     * 通过角色id删除用户跟角色关系表
     * @param id
     */
    void deleteRoleAndUserById(@Param("id")Serializable id);

    /**
     * 保存角色和权限之间的关系
     * @param roleId
     * @param pid
     */
    void saveRoleAndPermission(@Param("rid")Integer roleId, @Param("pid")Integer pid);

    /**
     * 根据用户id删除用户和角色关系表数据
     * @param id
     */
    void deleteUserAndRoleByUserId(@Param("id")Serializable id);

    /**
     * 根据用户ID查询当前用户拥有的角色id集合
     * @param uid
     * @return
     */
    List<Integer> queryRoleUserIdsByUid(Integer uid);

    /**
     *  保存用户和角色表数据
     * @param id
     * @param rid
     */
    void addUserRole(@Param("id")Integer id, @Param("rid")Integer rid);

}
