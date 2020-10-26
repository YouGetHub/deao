package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.domain.User;
import com.mapper.RoleMapper;
import com.mapper.UserMapper;
import com.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * 服务实现类
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    RoleMapper roleMapper;
    @Override
    public boolean removeById(Serializable id) {
        // 根据用户id删除用户和角色关系表数据
        roleMapper.deleteUserAndRoleByUserId(id);
        return super.removeById(id);
    }

    /**
     * 保存用户和角色表数据
     * @param id
     * @param rids
     */
    @Override
    public void addUserRole(@Param("id") Integer id, @Param("rids")Integer[] rids) {
        //删除用户和角色关系表数据
        roleMapper.deleteUserAndRoleByUserId(id);
        for (Integer rid:rids){
            roleMapper.addUserRole(id,rid);
        }
    }
}
