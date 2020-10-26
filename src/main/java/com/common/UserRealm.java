package com.common;

import com.domain.Permission;
import com.domain.User;
import com.service.PermissionService;
import com.service.RoleService;
import com.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义realm
 */
public class UserRealm extends AuthorizingRealm {
    @Autowired
    @Lazy
    UserService userService;

    @Autowired
    @Lazy
    RoleService roleService;

    @Autowired
    @Lazy
    PermissionService permissionService;
    /**
     * 登陆认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("loginname", token.getPrincipal().toString());
        User user = userService.getOne(queryWrapper);
        if (null!=user){
            ActiverUser activerUser = new ActiverUser();
            activerUser.setUser(user);
            //根据用户ID查询percode 权限
            //查询所有菜单
            QueryWrapper<Permission> qw=new QueryWrapper<>();
            //只查询权限
            qw.eq("type","permission");
            //查询可用
            qw.eq("available",1);

            Integer userId=user.getId();
            //根据用户ID查询当前用户拥有的角色id集合
            List<Integer> ridS = roleService.queryRoleUserIdsByUid(userId);
            Set<Integer> pids = new HashSet<>();
            for (Integer rid: ridS){
                //根据角色ID查询当前角色拥有的所有的权限或菜单ID
                List<Integer> permissionIds = roleService.queryRolePermissionIdsByRid(rid);
                pids.addAll(permissionIds);
            }
            List<Permission> list=new ArrayList<>();
            //查询对应的权限
            if (pids.size()>0) {
                qw.in("id", pids);
                list = permissionService.list(qw);
            }

            // 获取权限
            List<String> percodes=new ArrayList<>();
            for (Permission permission : list) {
                percodes.add(permission.getPercode());
                System.out.println("权限"+permission.getPercode());
            }
            // 存入权限
            activerUser.setPermissions(percodes);

            // 取盐
            ByteSource credentialsSalt = ByteSource.Util.bytes(user.getSalt());
            System.out.println(credentialsSalt);
            System.out.println("认证成功，登陆成功");
            return new SimpleAuthenticationInfo(activerUser, user.getPwd(), credentialsSalt,this.getClass().getName());
        }
        System.out.println("认证失败，登陆失败");
        return null;
    }

    /**
     * 身份授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        // 获取activerUser
        ActiverUser activerUser = (ActiverUser)principalCollection.getPrimaryPrincipal();
        User user = activerUser.getUser();
        List<String> permissions = activerUser.getPermissions();
        //是否可用
        if (user.getType()==1) {
            simpleAuthorizationInfo.addStringPermission("*:*");
        }else {
            if (null!=permissions&&permissions.size()>0){
                simpleAuthorizationInfo.addStringPermissions(permissions);
            }
        }
        return simpleAuthorizationInfo;
    }
}
