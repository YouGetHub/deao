package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.domain.Goods;
import com.domain.Inport;
import com.mapper.GoodsMapper;
import com.mapper.InportMapper;
import com.service.InportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class InportServiceImpl extends ServiceImpl<InportMapper, Inport> implements InportService {
    @Autowired
    InportMapper inportMapper;

    @Autowired
    GoodsMapper goodsMapper;

    /**
     * 重写save
     * @param entity
     * @return
     */
    @Override
    public boolean save(Inport entity) {
        Goods goods = goodsMapper.selectById(entity.getGoodsid());
        Goods goods1 = goods.setNumber(goods.getNumber() + entity.getNumber());
        goodsMapper.updateById(goods1);
        return super.save(entity);
    }

    /**
     * 重写updateById
     * @param entity
     * @return
     */
    @Override
    public boolean updateById(Inport entity) {
        Inport inport = getBaseMapper().selectById(entity.getId());
        Goods goods = goodsMapper.selectById(entity.getGoodsid());
        goods.setNumber(goods.getNumber()-inport.getNumber()+goods.getNumber());
        return super.updateById(entity);
    }

    /**
     * 重写removeById
     * @param id
     * @return
     */
    @Override
    public boolean removeById(Serializable id) {
        Inport inport = getBaseMapper().selectById(id);
        Goods goods = goodsMapper.selectById(inport.getGoodsid());
        goods.setNumber(goods.getNumber()-inport.getNumber());
        goodsMapper.updateById(goods);
        return super.removeById(id);
    }
}
