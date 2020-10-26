package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.JsonData;
import com.common.PinyinUtils;
import com.domain.Dept;
import com.domain.Role;
import com.domain.User;
import com.service.DeptService;
import com.service.RoleService;
import com.service.UserService;
import com.test.Md5;
import com.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 用户
 */
@RequestMapping("user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private RoleService roleService;

    /**
     * 查询用户所有数据
     * @param userVo
     * @return
     */
    @RequestMapping("loadUserAll")
    JsonData loadUserAll(UserVo userVo) {
        Page<User> userPage = new Page<>(userVo.getPage(), userVo.getLimit());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        //只查询系统用户
        userQueryWrapper.eq(StringUtils.isNotBlank(userVo.getName()), "loginname", userVo.getName()).or().eq(StringUtils.isNotBlank(userVo.getName()), "name", userVo.getName());
        userQueryWrapper.eq(StringUtils.isNotBlank(userVo.getAddress()), "address", userVo.getAddress());
        userQueryWrapper.eq(userVo.getDeptid() != null, "deptid", userVo.getDeptid());
        userQueryWrapper.eq("type", 1);
        userService.page(userPage, userQueryWrapper);
        List<User> userList = userPage.getRecords();
        for (User user : userList) {
            Integer deptid = user.getDeptid();
            if (deptid != null) {
                Dept one = deptService.getById(deptid);
                user.setDeptname(one.getTitle());//部门title
            }
            Integer mgr = user.getMgr();
            if (mgr != null) {
                User one = userService.getById(mgr);
                user.setLeadername(one.getName());// 用户name
            }
        }
        return new JsonData(userPage.getTotal(),userList);
    }

    /**
     * 获取最大排序码
     * @return
     */
    @RequestMapping("/loadUserOrderNumMax")
    public Map<String,Object> loadUserOrderNumMax(){
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 最大的排序码 条件
        queryWrapper.orderByDesc("ordernum");
        List<User> deptList = userService.list(queryWrapper);
        if (deptList.size()>0){
            // 第一条数据的排序码
            map.put("value",deptList.get(0).getOrdernum()+1);
        }else {
            map.put("value",1);
        }
        return map;
    }

    /**
     * 根据部门ID查询用户
     * @param deptid
     * @return
     */
    @RequestMapping("/loadUsersByDeptId")
    public JsonData loadUsersByDeptId(Integer deptid){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq(deptid!=null,"deptid",deptid);
        userQueryWrapper.eq("type",1);
        userQueryWrapper.eq("available",1);
        List<User> list = userService.list();
        return new JsonData(list);
    }

    /**
     * 把用户名转成拼音
     * @param username
     * @return
     */
    @RequestMapping("loginnamePinyin")
    public Map<String,Object> loginnamePinyin(String username){
        System.out.println(username);
        Map<String,Object> map=new HashMap<>();
        if(null!=username) {
            map.put("value", PinyinUtils.getPingYin(username));
        }else {
            map.put("value", "");
        }
        return map;
    }

    /**
     *  添加用户
     * @param userVo
     * @return
     */
    @RequestMapping("addUser")
    public JsonData addUser(UserVo userVo) {
        try {
            userVo.setType(1);//设置类型
            userVo.setHiredate(new Date());
            Md5 md5 = new Md5();
            Md5 md51 = md5.setMd5("md5", "123456", 2);
            userVo.setSalt(md51.getSalt());//设置盐
            userVo.setPwd(md51.getPwd());//设置密码
            userService.save(userVo);
            return new JsonData(200,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonData(-1,"添加失败");
        }
    }

    /**
     * 根据用户ID查询一个用户
     * @param id
     * @return
     */
    @RequestMapping("loadUserById")
    public JsonData loadUserById(Integer id) {
        return new JsonData(userService.getById(id));
    }

    /**
     * 修改用户
     * @param userVo
     * @return
     */
    @RequestMapping("updateUser")
    public JsonData updateUser(UserVo userVo) {
        try {
            userService.updateById(userVo);
            return new JsonData(200,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonData(-1,"修改失败");
        }
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @RequestMapping("deleteUser")
    public JsonData deleteUser(Integer id) {
        try {
            userService.removeById(id);
            return new JsonData(200,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonData(-1,"删除失败");
        }
    }

    /**
     * 验证原密码
     * @param id
     * @param pwd
     * @return
     */
    @RequestMapping("LoadUserIdAndPwd")
    public JsonData LoadUserIdAndPwd(Integer id,String pwd) {
        try {
            User user = userService.getById(id);
            Md5 md5 = new Md5();
            // 验证
            Md5 md51 = md5.setMd5("md5", pwd,user.getSalt(),2);
            if (user.getPwd().equals(md51.getPwd())){
                return new JsonData(200,"原密码正确");
            }
            return new JsonData(-1,"原密码错误");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonData(-1,"原密码错误");
        }
    }

    /**
     * 修改密码
     * @param id
     * @param pwd
     * @return
     */
    @RequestMapping("resetPwd")
    public JsonData resetPwd(Integer id,String pwd) {
        System.out.println(id+"------"+pwd);
       try{
           User user=new User();
           user.setId(id);
           Md5 md5 = new Md5();
           // 验证
           Md5 md51 = md5.setMd5("md5", pwd,2);
           user.setPwd(md51.getPwd());
           user.setSalt(md51.getSalt());
           userService.updateById(user);
           return new JsonData(200,"修改成功");
       }catch (Exception e){
           e.printStackTrace();
           return new JsonData(-1,"修改失败");
       }
    }

    /**
     * 查询所有可用 用户已拥有 角色数据
     * @param uid
     * @return
     */
    @RequestMapping("loadAllUserByIdRole")
    public JsonData resetPwd(Integer uid) {
        // 查询所有可用role
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("available",1);
        List<Map<String, Object>> roleMaps = roleService.listMaps(queryWrapper);

        // 查询用户已拥有role
        List<Integer> userIdsByUidRids = roleService.queryRoleUserIdsByUid(uid);

        for (Map<String, Object> map : roleMaps){
            //LAY_CHECKED = true 复选框选中
            Boolean LAY_CHECKED=false;
            //获取rid
            Integer roleId = (Integer) map.get("id");

            for (Integer roleUserRid:userIdsByUidRids){
                if (roleUserRid==roleId){
                    LAY_CHECKED = true;
                    break;
                }
            }

            map.put("LAY_CHECKED",LAY_CHECKED);

        }
        return new JsonData(Long.valueOf(roleMaps.size()),roleMaps);
    }

    /**
     * 保存用户和角色表数据
     * @param uid
     * @param rids
     * @return
     */
    @RequestMapping("addUserRole")
    public JsonData addUserRole(Integer id,Integer[] rids) {
        try{
            userService.addUserRole(id,rids);
            return new JsonData(200,"分配成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"分配失败");
        }
    }

}
