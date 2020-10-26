package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.domain.User;

import java.io.Serializable;

/**
 * 服务类
 */
public interface UserService extends IService<User> {
    /**
     * 保存用户和角色表数据
     * @param uid
     * @param rids
     */
    void addUserRole(Integer uid, Integer[] rids);
}
