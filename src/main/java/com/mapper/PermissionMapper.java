package com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.domain.Permission;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;

/**
 * Mapper接口
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    // 根据权限id 删除 角色与权限关系的数据
    void deleteRolePermissionById(@Param("id")Serializable id);
}