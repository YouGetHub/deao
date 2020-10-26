package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.JsonData;
import com.domain.Log;
import com.service.LogService;
import com.vo.LogVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 查询日志
 */
@RestController
@RequestMapping("/log")
public class LogController {
    @Autowired
    private LogService logService;

    /**
     * 查询全部日志
     * @param logVo
     * @return
     */
    @RequestMapping("/loadAllLog")
    public JsonData log(LogVo logVo){
        IPage<Log> page = new Page<>(logVo.getPage(),logVo.getLimit());
        QueryWrapper<Log> queryWrapper = new QueryWrapper<>();
        // 判断是否为空
        queryWrapper.like(StringUtils.isNotBlank(logVo.getLoginname()),"loginname", logVo.getLoginname());
        queryWrapper.like(StringUtils.isNotBlank(logVo.getLoginname()),"loginip", logVo.getLoginip());
        // 大于起始时间
        queryWrapper.gt(logVo.getStartTime()!=null,"logintime",logVo.getStartTime());
        // 小于结束时间
        queryWrapper.le(logVo.getEndTime()!=null,"logintime",logVo.getEndTime());
        // 通过时间排序
        queryWrapper.orderByDesc("logintime");
        logService.page(page,queryWrapper);
        return new JsonData(page.getTotal(), page.getRecords());
    }

    /**
     * 删除一条
     */
    @RequestMapping("deleteLog")
    public JsonData deleteLog(Integer id){
        try {
            logService.removeById(id);
            return new JsonData(200,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"删除失败");
        }
    }

    /**
     * 删除多条
     */
    @RequestMapping("batchDeleteLog")
    public JsonData batchDeleteLog(LogVo logVo){
        try {
            List<Integer> integers = Arrays.asList(logVo.getIds());
            logService.removeByIds(integers);
            return new JsonData(200,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"删除失败");
        }
    }
}
