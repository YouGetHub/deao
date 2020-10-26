package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.JsonData;
import com.domain.Provider;
import com.service.ProviderService;
import com.vo.ProviderVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 供应商管理
 */
@RequestMapping("provider")
@RestController
public class ProviderController {
    @Autowired
    ProviderService providerService;

    /**
     * 查询
     */
    @RequestMapping("loadAllProvider")
    public JsonData loadAllProvider(ProviderVo providerVo) {
        IPage<Provider> page = new Page<>(providerVo.getPage(), providerVo.getLimit());
        QueryWrapper<Provider> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(providerVo.getProvidername()), "providername",providerVo.getProvidername());
        queryWrapper.like(StringUtils.isNotBlank(providerVo.getPhone()), "phone", providerVo.getPhone());
        queryWrapper.like(StringUtils.isNotBlank(providerVo.getConnectionperson()), "connectionperson",providerVo.getConnectionperson());
        providerService.page(page, queryWrapper);
        return new JsonData(page.getTotal(), page.getRecords());
    }

    /**
     * 添加
     */
    @RequestMapping("addrovider")
    public JsonData addProvider(ProviderVo providerVo) {
        try {
            providerService.save(providerVo);
            return new JsonData(200,"添加成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"添加失败");
        }
    }

    /**
     * 修改
     */
    @RequestMapping("updateProvider")
    public JsonData updateProvider(ProviderVo providerVo) {
        try {
            providerService.updateById(providerVo);
            return new JsonData(200,"修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"修改失败");
        }
    }

    /**
     * 删除
     */
    @RequestMapping("deleteProvider")
    public JsonData deleteProvider(Integer id) {
        try {
            providerService.removeById(id);
            return new JsonData(200,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"删除失败");
        }
    }

    /**
     * 批量删除
     */
    @RequestMapping("batchDeleteProvider")
    public JsonData batchDeleteProvider(ProviderVo providerVo) {
        try {
            List<Integer> idList = Arrays.asList(providerVo.getIds());
            providerService.removeByIds(idList);
            return new JsonData(200,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"删除失败");
        }
    }


    /**
     * 加载所有可用的供应商
     */
    @RequestMapping("loadAllProviderName")
    public JsonData loadAllProviderName() {
        QueryWrapper<Provider> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("available",1);
        List<Provider> list = providerService.list(queryWrapper);
        return new JsonData(list);
    }
}
