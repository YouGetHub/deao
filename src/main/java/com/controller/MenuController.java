package com.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.*;
import com.domain.Permission;
import com.domain.User;
import com.service.PermissionService;
import com.service.RoleService;
import com.service.UserService;
import com.vo.PermissionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.*;

/**
 * 主页菜单
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    /**
     * 菜单权限类型
     */
    public static final String TYPE_MENU = "menu";
    public static final String TYPE_PERMISSION = "permission";

    /**
     * 是否可用状态
     */
    public static final Integer AVAILABLE_TRUE = 1;
    public static final Integer AVAILABLE_FALSE = 2;
    // 菜单展开类型
    public static final Integer OPEN_TRUE = 1;
    public static final Integer OPEN_FALSE = 0;
    @Autowired
    private PermissionService permissionService;

    @RequestMapping("IndexLeftJson")
    public JsonData IndexLeftJson(){
        //查询所有菜单
        QueryWrapper<Permission> queryWrapper=new QueryWrapper<>();
        //只查询菜单
        queryWrapper.eq("type","menu");
        //只查询可用
        queryWrapper.eq("available",1);

        // 从Session获取用户type
        User user = (User)WebUtils.getSession().getAttribute("user");

        List<Permission> list = null;

        // 判断用户的角色
        if (user.getType()==0){
            //超级用户
            list = permissionService.list(queryWrapper);
        }else{//普通用户
            Integer userId=user.getId();
            //根据用户ID查询当前用户拥有的角色id集合
            List<Integer> rids = roleService.queryRoleUserIdsByUid(userId);
            // 用户会有多个角色 根据角色查询出来的权限Id会有重复，用hashSet去重
            Set<Integer> pids = new HashSet<>();
            for (Integer rid: rids){
                //根据角色ID查询当前角色拥有的所有的权限或菜单ID
                List<Integer> permissionIds = roleService.queryRolePermissionIdsByRid(rid);
                // 去重权限Id
                pids.addAll(permissionIds);
            }
            //查询对应的权限
            if (pids.size()>0) {
                queryWrapper.in("id", pids);
                list = permissionService.list(queryWrapper);
            }else{
                list = new ArrayList<>();
            }

        }

        //导航树格式
        ArrayList<TreeNode> treeNodes = new ArrayList<>();
        for (Permission permission : list){
            Integer id = permission.getId();
            Integer pid = permission.getPid();
            String title = permission.getTitle();
            String icon = permission.getIcon();
            String href = permission.getHref();
            Boolean spread = permission.getOpen()==1?true:false;
            treeNodes.add(new TreeNode(id,pid,title,icon,href,spread));
        }
        List<TreeNode> treeNodeList = TreeNodeBuilder.build(treeNodes, 1);
        // 菜单层级关系
        return new JsonData(treeNodeList);
    }

    /**
     * 返回部门树json数据
     * @return
     */
    @RequestMapping("loadMenuDtree")
    public JsonData loadMenuDtree(){
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        // 只查询菜单
        queryWrapper.eq("type","menu");
        List<Permission> list = permissionService.list(queryWrapper);
        List<TreeNode> treeNodeList = new ArrayList<>();
        for (Permission permission : list){
            boolean parentId = permission.getOpen() ==1?true:false;
            treeNodeList.add(new TreeNode(permission.getId(),permission.getPid(),permission.getTitle(),parentId));
        }
        return new JsonData(treeNodeList);
    }

    /**
     * 查询部门
     * @param permissionVo
     * @return
     */
    @RequestMapping("/loadAllMenu")
    public JsonData loadAllMenu(PermissionVo permissionVo){
        IPage<Permission> page = new Page<>(permissionVo.getPage(),permissionVo.getLimit());
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        // 节点点击传入的id，在判断
        queryWrapper.eq(permissionVo.getId()!=null,"id",permissionVo.getId()).or().eq(permissionVo.getId()!=null,"pid",permissionVo.getId());
        // 只查询菜单
        queryWrapper.eq("type","menu");
        // 判断是否为空
        queryWrapper.like(StringUtils.isNotBlank(permissionVo.getTitle()),"title", permissionVo.getTitle());
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
    @RequestMapping("/addMenu")
    public JsonData addMenu(PermissionVo permissionVo){
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
    @RequestMapping("/updateMenu")
    public JsonData updateMenu(PermissionVo permissionVo){
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
     * 查看是否有子部门
     */
    @RequestMapping("showChildMenu")
    public Map<String,Object> showChildMenu(PermissionVo permissionVo){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(permissionVo.getId()!=null,"pid",permissionVo.getId());
        List<Permission> list = permissionService.list(queryWrapper);
        if (list.size()>0){
            map.put("value",true);
            return map;
        }
        map.put("value",false);
        return map;
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/deleteMenu")
    public JsonData deleteMenu(PermissionVo permissionVo){
        try{
            permissionService.removeById(permissionVo.getId());
            return new JsonData(200,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"删除失败");
        }
    }
}
