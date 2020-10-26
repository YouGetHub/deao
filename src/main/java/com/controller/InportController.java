package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.JsonData;
import com.common.WebUtils;
import com.domain.Goods;
import com.domain.Inport;
import com.domain.Provider;
import com.domain.User;
import com.service.GoodsService;
import com.service.InportService;
import com.service.ProviderService;
import com.vo.GoodsVo;
import com.vo.InportVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RequestMapping("inport")
@RestController
public class InportController {
    @Autowired
    InportService inportService;

    @Autowired
    ProviderService providerService;

    @Autowired
    GoodsService goodsService;
    /**
     * 查询
     */
    @RequestMapping("loadAllInport")
    public JsonData loadAllGoods(InportVo inportVo) {
        IPage<Inport> page = new Page<>(inportVo.getPage(), inportVo.getLimit());
        QueryWrapper<Inport> queryWrapper = new QueryWrapper<>();
        //有供应商
        queryWrapper.eq(inportVo.getProviderid() != null && inportVo.getProviderid() != 0, "providerid", inportVo.getProviderid());
        //有商品
        queryWrapper.eq(inportVo.getProviderid() != null && inportVo.getGoodsid() != 0, "goodsid", inportVo.getGoodsid());
        // 大于起始时间
        queryWrapper.gt(inportVo.getStartTime()!=null,"inporttime",inportVo.getStartTime());
        // 小于结束时间
        queryWrapper.le(inportVo.getEndTime()!=null,"inporttime",inportVo.getEndTime());
        // 通过时间排序
        queryWrapper.orderByDesc("inporttime");

//        queryWrapper.like(StringUtils.isNotBlank(inportVo.getOperateperson()), "operateperson", inportVo.getOperateperson());
//        queryWrapper.like(StringUtils.isNotBlank(inportVo.getRemark()), "remark", inportVo.getRemark());
        inportService.page(page,queryWrapper);

        List<Inport> records = page.getRecords();
        for (Inport inport : records){
            // 供应商
            Provider provider = providerService.getById(inport.getProviderid());
            if (null!=provider){
                inport.setProvidername(provider.getProvidername());
            }
            // 商品  商品规格
            Goods goods = goodsService.getById(inport.getGoodsid());
            if (null!=goods){
                inport.setGoodsname(goods.getGoodsname());
                inport.setSize(goods.getSize());
            }
        }
        return new JsonData(page.getTotal(),records);
    }
    /**
     * 添加
     */
    @RequestMapping("addInport")
    public JsonData addInport(InportVo inportVo) {
        try {
            inportVo.setInporttime(new Date());
            User user = (User)WebUtils.getSession().getAttribute("user");
            inportVo.setOperateperson(user.getName());
            inportService.save(inportVo);
            return new JsonData(200,"添加成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"添加失败");
        }
    }

    /**
     * 修改
     */
    @RequestMapping("updateInport")
    public JsonData updateInport(InportVo inportVo) {
        try {
            inportService.updateById(inportVo);
            return new JsonData(200,"添加成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"添加失败");
        }
    }

    /**
     * 删除
     */
    @RequestMapping("deleteInport")
    public JsonData deleteInport(Integer id) {
        try {
            inportService.removeById(id);
            return new JsonData(200,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"删除失败");
        }
    }

}