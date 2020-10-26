package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.AppFileUtils;
import com.common.JsonData;
import com.domain.Goods;
import com.domain.Provider;
import com.service.GoodsService;
import com.service.ProviderService;
import com.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品
 */
@RequestMapping("goods")
@RestController
public class GoodsController {
    @Autowired
    GoodsService goodsService;
    @Autowired
    ProviderService providerService;
    /**
     * 查询
     */
    @RequestMapping("loadAllGoods")
    public JsonData loadAllGoods(GoodsVo goodsVo) {
        IPage<Goods> page = new Page<>(goodsVo.getPage(), goodsVo.getLimit());
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        //查询商品有供应商的
        queryWrapper.eq(goodsVo.getProviderid()!=null&&goodsVo.getProviderid()!=0,"providerid",goodsVo.getProviderid());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getGoodsname()), "goodsname", goodsVo.getGoodsname());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getProductcode()), "productcode", goodsVo.getProductcode());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getPromitcode()), "promitcode", goodsVo.getPromitcode());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getDescription()), "description", goodsVo.getDescription());
        queryWrapper.like(StringUtils.isNotBlank(goodsVo.getSize()), "size", goodsVo.getSize());
        goodsService.page(page, queryWrapper);
        List<Goods> records = page.getRecords();

        for (Goods goods : records) {
            //根据供应商id 查询供应商名
            Provider provider = providerService.getById(goods.getProviderid());
            if(null!=provider) {
                // set供应商名
                goods.setProvidername(provider.getProvidername());
            }
        }
        return new JsonData(page.getTotal(), records);
    }

    /**
     * 添加
     */
    @RequestMapping("addGoods")
    public JsonData addGoods(GoodsVo goodsVo) {
        try {
            goodsVo.setGoodsimg(goodsVo.getGoodsimg());
            goodsService.save(goodsVo);
            return new JsonData(200,"添加成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"添加失败");
        }
    }

    /**
     * 修改
     */
    @RequestMapping("updateGoods")
    public JsonData updateGoods(GoodsVo goodsVo) {
        try {
            // 不是默认图片
            if (!goodsVo.getGoodsimg().equals("default/default.PNG")){
                Goods goods = goodsService.getById(goodsVo.getId());
                String goodsimg = goods.getGoodsimg();
                //删除图片
                AppFileUtils.removeFileByPath(goodsimg);
                //修改
                goodsService.updateById(goodsVo);
            }
            //是默认图片
            goodsService.updateById(goodsVo);
            return new JsonData(200,"修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"修改失败");
        }
    }
    //删除
    @RequestMapping("deleteGoods")
    public JsonData deleteGoods(Integer id,String goodsimg) {
        try {
            // 是默认图片
            if ("default/default.PNG".equals(goodsimg)){
                goodsService.removeById(id);
                return new JsonData(200,"删除成功");
            }
            //删除图片
            AppFileUtils.removeFileByPath(goodsimg);
            goodsService.removeById(id);
            return new JsonData(200,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"删除失败");
        }
    }

    /**
     * 根据供应商id查询商品的下拉框
     * @param providerid
     * @return
     */
    @RequestMapping("loadGoodsByProviderId")
    public JsonData loadGoodsByProviderId(Integer providerid) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        // 可用
        queryWrapper.eq("available",1);
        queryWrapper.eq(providerid!=null&&providerid!=0,"providerid",providerid);
        List<Goods> goodsList = goodsService.list(queryWrapper);
//        //查询商品有供应商的
//        for (Goods goods : goodsList) {
//            //根据供应商id 查询供应商名
//            Provider provider = providerService.getById(goods.getProviderid());
//            if(null!=provider) {
//                // set供应商名
//                goods.setProvidername(provider.getProvidername());
//            }
//        }
        return new JsonData(goodsList);
    }

    /**
     * 根据供应商id查询商品的下拉框
     * @param providerid
     * @return
     */
    @RequestMapping("loadGoodsByProviderIdSelect")
    public JsonData loadGoodsByProviderIdSelect(Integer providerid) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        // 可用
        queryWrapper.eq("available",1);
        queryWrapper.eq(providerid!=null&&providerid!=0,"providerid",providerid);
        List<Goods> goodsList = goodsService.list(queryWrapper);
        return new JsonData(goodsList);
    }
}
