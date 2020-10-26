package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.JsonData;
import com.common.WebUtils;
import com.domain.Notice;
import com.domain.User;
import com.service.NoticeService;
import com.vo.NoticeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 公告
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    /**
     * 查询全部公告
     * @param noticeVo
     * @return
     */
    @RequestMapping("/loadAllNotice")
    public JsonData log(NoticeVo noticeVo){
        IPage<Notice> page = new Page<>(noticeVo.getPage(),noticeVo.getLimit());
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        // 判断是否为空
        queryWrapper.like(StringUtils.isNotBlank(noticeVo.getTitle()),"title", noticeVo.getTitle());
        // 大于起始时间
        queryWrapper.gt(noticeVo.getStartTime()!=null,"createtime",noticeVo.getStartTime());
        // 小于结束时间
        queryWrapper.le(noticeVo.getEndTime()!=null,"createtime",noticeVo.getEndTime());
        queryWrapper.like(StringUtils.isNotBlank(noticeVo.getOpername()),"opername", noticeVo.getOpername());
        // 通过时间排序
        queryWrapper.orderByDesc("createtime");
        noticeService.page(page,queryWrapper);
        return new JsonData(page.getTotal(), page.getRecords());
    }

    /**
     * 添加公告
     */
    @RequestMapping("/addNotice")
    public JsonData addNotice(Notice notice){
        try {
            notice.setCreatetime(new Date());
            User user = (User)WebUtils.getSession().getAttribute("user");
            notice.setOpername(user.getName());
            noticeService.save(notice);
            return new JsonData(200,"发布成功");
        }catch (Exception e){
            return new JsonData(-1,"发布失败");
        }
    }

    /**
     * 修改公告
     */
    @RequestMapping("/updateNotice")
    public JsonData updateNotice(Notice notice){
        try {
            noticeService.updateById(notice);
            return new JsonData(200,"修改成功");
        }catch (Exception e){
            return new JsonData(-1,"修改失败");
        }
    }


    /**
     * 删除一条
     */
    @RequestMapping("/deleteNotice")
    public JsonData deleteNotice(Integer id){
        try {
            noticeService.removeById(id);
            return new JsonData(200,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"删除失败.");
        }
    }

    /**
     * 删除多条
     */
    @RequestMapping("batchDeleteNotice")
    public JsonData batchDeleteNotice(NoticeVo noticeVo){
        try {
            List<Integer> integers = Arrays.asList(noticeVo.getIds());
            noticeService.removeByIds(integers);
            return new JsonData(200,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"删除失败.");
        }
    }


}
