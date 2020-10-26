package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.JsonData;
import com.common.TreeNode;
import com.domain.Dept;
import com.service.DeptService;
import com.vo.DeptVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 部门
 */
@RequestMapping("dept")
@RestController
public class DeptController {

    @Autowired
    private DeptService deptService;

    /**
     * 返回部门树json数据
     * @return
     */
    @RequestMapping("loadDeptDtree")
    public JsonData loadDeptDtree(){
        List<Dept> list = deptService.list();
        List<TreeNode> treeNodeList = new ArrayList<>();
        for (Dept dept : list){
            System.out.println(dept.toString());
            boolean parentId = dept.getOpen() ==1?true:false;
            treeNodeList.add(new TreeNode(dept.getId(),dept.getPid(),dept.getTitle(),parentId));
        }
        return new JsonData(treeNodeList);
    }

    /**
     * 查询部门
     * @param deptVo
     * @return
     */
    @RequestMapping("/loadAllDept")
    public JsonData loadAllDept(DeptVo deptVo){
        IPage<Dept> page = new Page<>(deptVo.getPage(),deptVo.getLimit());
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        // 判断是否为空
        queryWrapper.like(StringUtils.isNotBlank(deptVo.getTitle()),"title", deptVo.getTitle());
        queryWrapper.like(StringUtils.isNotBlank(deptVo.getRemark()),"remark", deptVo.getRemark());
        queryWrapper.like(StringUtils.isNotBlank(deptVo.getAddress()),"address", deptVo.getAddress());
        // 节点点击传入的id，在判断
        queryWrapper.eq(deptVo.getId()!=null,"id",deptVo.getId()).or().eq(deptVo.getId()!=null,"pid",deptVo.getId());
        // 通过排序码排序
        queryWrapper.orderByAsc("ordernum");
        deptService.page(page,queryWrapper);
        return new JsonData(page.getTotal(), page.getRecords());
    }

    /**
     * 添加
     * @param deptVo
     * @return
     */
    @RequestMapping("/addDept")
    public JsonData addDept(DeptVo deptVo){
        try{
            deptService.save(deptVo);
            deptVo.setCreatetime(new Date());
            return new JsonData(200,"添加成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"添加失败");
        }
    }

    /**
     * 修改
     * @param deptVo
     * @return
     */
    @RequestMapping("/updateDept")
    public JsonData updateDept(DeptVo deptVo){
        System.out.println("修改");
        try{
            deptService.updateById(deptVo);
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
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        // 最大的排序码 条件
        queryWrapper.orderByDesc("ordernum");
//        Page<Dept> page = new Page<>(1, 1);
//        List<Dept> deptList = deptService.page(page,queryWrapper).getRecords();
        List<Dept> deptList = deptService.list(queryWrapper);
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
    @RequestMapping("showChildDept")
    public Map<String,Object> showChildDept(DeptVo deptVo){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(deptVo.getId()!=null,"pid",deptVo.getId());
        List<Dept> list = deptService.list(queryWrapper);
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
    @RequestMapping("/deleteDept")
    public JsonData deleteDept(DeptVo deptVo){
        try{
            deptService.removeById(deptVo.getId());
            return new JsonData(200,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"删除失败");
        }
    }
}
