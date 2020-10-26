package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.JsonData;
import com.common.TreeNode;
import com.domain.Permission;
import com.service.PermissionService;
import com.vo.PermissionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限
 */
@RequestMapping("permission")
@RestController
public class PermissionController {
    @Autowired
    PermissionService permissionService;
    /**
     * 返回权限管理json数据
     * @return
     */
    @RequestMapping("loadPermissionDtree")
    public JsonData loadPermissionDtree(){
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type","menu");
        List<Permission> list = permissionService.list(queryWrapper);
        List<TreeNode> treeNodeList = new ArrayList<>();
        for (Permission permission : list){
            boolean parentId = permission.getOpen()==1?true:false;
            treeNodeList.add(new TreeNode(permission.getId(),permission.getPid(),permission.getTitle(),parentId));
        }
        return new JsonData(treeNodeList);
    }

    /**
     * 查询权限
     * @param permissionVo
     * @return
     */
    @RequestMapping("/loadAllPermission")
    public JsonData loadAllPermission(PermissionVo permissionVo){
        IPage<Permission> page = new Page<>(permissionVo.getPage(),permissionVo.getLimit());
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        // 只查询权限
        queryWrapper.eq("type","permission");
        // 判断是否为空
        queryWrapper.like(StringUtils.isNotBlank(permissionVo.getTitle()),"title", permissionVo.getTitle());
        queryWrapper.like(StringUtils.isNotBlank(permissionVo.getPercode()), "percode", permissionVo.getPercode());
        // 节点点击传入的id，在判断
        queryWrapper.eq(permissionVo.getId()!=null,"pid",permissionVo.getId());
        // 通过排序码排序
        queryWrapper.orderByAsc("ordernum");
        permissionService.page(page,queryWrapper);
        return new JsonData(page.getTotal(), page.getRecords());
    }

    /**
     * 添加
     * @param permissionVo
     * @return
     */
    @RequestMapping("/addPermission")
    public JsonData addPermission(PermissionVo permissionVo){
        try{
            // 设置添加类型
            permissionVo.setType("menu");
            permissionService.save(permissionVo);
            return new JsonData(200,"添加成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"添加失败");
        }
    }

    /**
     * 修改
     * @param permissionVo
     * @return
     */
    @RequestMapping("/updatePermission")
    public JsonData updatePermission(PermissionVo permissionVo){
        try{
            permissionService.updateById(permissionVo);
            return new JsonData(200,"修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"修改失败");
        }
    }

    /**
     * 获取最大排序码
     * @return
     */
    @RequestMapping("/ordernumMax")
    public Map<String,Object> ordernumMax(){
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        // 最大的排序码 条件
        queryWrapper.orderByDesc("ordernum");
        List<Permission> deptList = permissionService.list(queryWrapper);
        if (deptList.size()>0){
            // 第一条数据的排序码
            map.put("value",deptList.get(0).getOrdernum()+1);
        }else {
            map.put("value",1);
        }
        return map;
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/deletePermission")
    public JsonData deletePermission(PermissionVo permissionVo){
        try{
            permissionService.removeById(permissionVo.getId());
            return new JsonData(200,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"删除失败");
        }
    }
}
