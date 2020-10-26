package com.service.impl;

import java.util.Date;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.WebUtils;
import com.domain.Goods;
import com.domain.Inport;
import com.domain.Outport;
import com.domain.User;
import com.mapper.GoodsMapper;
import com.mapper.InportMapper;
import com.mapper.OutportMapper;
import com.service.OutportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OutportServiceImpl extends ServiceImpl<OutportMapper, Outport> implements OutportService {

	@Autowired
	private InportMapper inportMapper;
	
	@Autowired
	private GoodsMapper goodsMapper;
	
	@Override
	public void addOutPort(Integer id, Integer number, String remark) {
		//根据进货单ID查询进货单信息
		Inport inport = this.inportMapper.selectById(id);
		//根据商品ID查询商品信息
		Goods goods = this.goodsMapper.selectById(inport.getGoodsid());
		goods.setNumber(goods.getNumber()-number);
		this.goodsMapper.updateById(goods);
		//添加退货单信息
		Outport entity=new Outport();
		entity.setGoodsid(inport.getGoodsid());
		entity.setNumber(number);
		User user=(User) WebUtils.getSession().getAttribute("user");
		entity.setOperateperson(user.getName());
		entity.setOutportprice(inport.getInportprice());
		entity.setOutputtime(new Date());
		entity.setPaytype(inport.getPaytype());
		entity.setProviderid(inport.getProviderid());
		entity.setRemark(remark);
		this.getBaseMapper().insert(entity);
	}

}
