package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.JsonData;
import com.domain.Goods;
import com.domain.Outport;
import com.domain.Provider;
import com.service.GoodsService;
import com.service.OutportService;
import com.service.ProviderService;
import com.vo.OutportVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 退货
 */
@RestController
@RequestMapping("outport")
public class OutportController {
	
	@Autowired
	private OutportService outportService;
	
	@Autowired
	private ProviderService providerService;
	
	@Autowired
	private GoodsService goodsService;

	/**
	 * 查询
	 */
	@RequestMapping("loadAllOutport")
	public JsonData loadAllOutport(OutportVo outportVo) {
		IPage<Outport> page = new Page<>(outportVo.getPage(), outportVo.getLimit());
		QueryWrapper<Outport> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(outportVo.getProviderid()!=null&&outportVo.getProviderid()!=0,"providerid",outportVo.getProviderid());
		queryWrapper.eq(outportVo.getGoodsid()!=null&&outportVo.getGoodsid()!=0,"goodsid",outportVo.getGoodsid());
		queryWrapper.ge(outportVo.getStartTime()!=null, "outputtime", outportVo.getStartTime());
		queryWrapper.le(outportVo.getEndTime()!=null, "outputtime", outportVo.getEndTime());
		queryWrapper.like(StringUtils.isNotBlank(outportVo.getOperateperson()), "operateperson", outportVo.getOperateperson());
		queryWrapper.like(StringUtils.isNotBlank(outportVo.getRemark()), "remark", outportVo.getRemark());
		queryWrapper.orderByDesc("outputtime");
		outportService.page(page, queryWrapper);
		List<Outport> records = page.getRecords();
		for (Outport outport : records) {
			Provider provider = this.providerService.getById(outport.getProviderid());
			if(null!=provider) {
				outport.setProvidername(provider.getProvidername());
			}
			Goods goods = this.goodsService.getById(outport.getGoodsid());
			if(null!=goods) {
				outport.setGoodsname(goods.getGoodsname());
				outport.setSize(goods.getSize());
			}
		}
		return new JsonData(page.getTotal(), records);
	}

	/**
	 * 添加退货信息
	 */
	@RequestMapping("addOutport")
	public JsonData addOutport(Integer id, Integer number, String remark) {
		try {
			outportService.addOutPort(id,number,remark);
			return new JsonData(200,"退货成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonData(-1,"退货成功");
		}
	}
	
}

