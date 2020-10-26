package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.JsonData;
import com.common.TreeNode;
import com.domain.Permission;
import com.domain.Role;
import com.service.PermissionService;
import com.service.RoleService;
import com.vo.RoleVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 角色
 */
@RestController
@RequestMapping("role")
public class RoleController {
    @Autowired
    RoleService roleService;

    @Autowired
    PermissionService permissionService;

    /**
     * 查询
     */
    @RequestMapping("loadAllRole")
    public JsonData loadAllRole(RoleVo roleVo){
        Page<Role> page = new Page<>(roleVo.getPage(),roleVo.getLimit());
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(roleVo.getName()),"name",roleVo.getName());
        queryWrapper.like(StringUtils.isNotBlank(roleVo.getRemark()),"remark",roleVo.getRemark());
        queryWrapper.eq(roleVo.getAvailable()!=null, "available", roleVo.getAvailable());
        queryWrapper.orderByDesc("createtime");
        roleService.page(page,queryWrapper);
        return new JsonData(page.getTotal(), page.getRecords());
    }

    /**
     * 添加
     */
    @RequestMapping("addRole")
    public JsonData addRole(RoleVo roleVo){
        try{
            roleVo.setCreatetime(new Date());
            roleService.save(roleVo);
            return new JsonData(200,"添加成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"添加失败");
        }
    }
    /**
     * 修改
     */
    @RequestMapping("updateRole")
    public JsonData updateRole(RoleVo roleVo){
        try{
            roleService.updateById(roleVo);
            return new JsonData(200,"修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"修改失败");
        }
    }
    /**
     * 删除
     */
    @RequestMapping("deleteRole")
    public JsonData deleteRole(Integer id){
        try{
            roleService.removeById(id);
            return new JsonData(200,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"删除失败");
        }
    }

    /**
     * 根据角色ID加载菜单和权限的树的json串
     * @param roleId
     * @return
     */
    @RequestMapping("selectPermissionJson")
    public JsonData selectPermissionJson(Integer roleId){
        //查询所有可用的菜单和权限
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("available",true);
        List<Permission> AllPermissionLists = permissionService.list(queryWrapper);

        //根据角色ID查询当前角色拥有的所有的权限或菜单ID
        ArrayList<Integer> RolePermissions = roleService.queryRolePermissionIdsByRid(roleId);

        //根据查询出来的菜单ID查询权限和菜单数据
        List<Permission> permissions=null;
        if(RolePermissions.size()>0) { //如果有ID就去查询
            queryWrapper.in("id", RolePermissions);
            permissions = permissionService.list(queryWrapper);
        }else {
            permissions=new ArrayList<>();
        }
        //构造 List<TreeNode>
        List<TreeNode> nodes=new ArrayList<>();
        for (Permission p1 : AllPermissionLists) {
            String checkArr="0";
            for (Permission p2 : permissions) {
                if(p1.getId()==p2.getId()) {
                    checkArr="1";
                    break;
                }
            }
            Boolean spread=(p1.getOpen()==null||p1.getOpen()==1)?true:false;
            nodes.add(new TreeNode(p1.getId(), p1.getPid(), p1.getTitle(), spread, checkArr));
        }
        return new JsonData(nodes);
    }

    /**
     * 权限分配
     * @param rid
     * @param ids
     * @return
     */
    @RequestMapping("saveRoleAndPermission")
    public JsonData selectPermissionJson(Integer rid,Integer[] ids){
        try {
            if (ids==null){
                return new JsonData(200,"分配成功");
            }else {
                // 保存角色和权限之间的关系
                roleService.saveRoleAndPermission(rid,ids);
                return new JsonData(200,"分配成功");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"分配失败");
        }
    }
}
