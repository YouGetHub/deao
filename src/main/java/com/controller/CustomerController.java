package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.JsonData;
import com.domain.Customer;
import com.service.CustomerService;
import com.vo.CustomerVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 客户
 */
@RequestMapping("customer")
@RestController
public class CustomerController {
    @Autowired
    CustomerService customerService;

    /**
     * 查询
     */
    @RequestMapping("loadAllCustomer")
    public JsonData loadAllCustomer(CustomerVo customerVo) {
        IPage<Customer> page = new Page<>(customerVo.getPage(), customerVo.getLimit());
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(customerVo.getCustomername()), "customername",customerVo.getCustomername());
        queryWrapper.like(StringUtils.isNotBlank(customerVo.getPhone()), "phone", customerVo.getPhone());
        queryWrapper.like(StringUtils.isNotBlank(customerVo.getConnectionperson()), "connectionperson",customerVo.getConnectionperson());
        customerService.page(page, queryWrapper);
        return new JsonData(page.getTotal(), page.getRecords());
    }

    /**
     * 添加
     */
    @RequestMapping("addCustomer")
    public JsonData addCustomer(CustomerVo customerVo) {
        try {
            customerService.save(customerVo);
            return new JsonData(200,"添加成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"添加失败");
        }
    }

    /**
     * 修改
     */
    @RequestMapping("updateCustomer")
    public JsonData updateCustomer(CustomerVo customerVo) {
        try {
            customerService.updateById(customerVo);
            return new JsonData(200,"修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"修改失败");
        }
    }

    /**
     * 删除
     */
    @RequestMapping("deleteCustomer")
    public JsonData deleteCustomer(Integer id) {
        try {
            customerService.removeById(id);
            return new JsonData(200,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"删除失败");
        }
    }

    /**
     * 批量删除
     */
    @RequestMapping("batchDeleteCustomer")
    public JsonData batchDeleteCustomer(CustomerVo customerVo) {
        try {
            List<Integer> idList = Arrays.asList(customerVo.getIds());
            customerService.removeByIds(idList);
            return new JsonData(200,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"删除失败");
        }
    }
}
