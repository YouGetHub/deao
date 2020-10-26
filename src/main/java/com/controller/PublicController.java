package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转
 */
@RequestMapping("public")
@Controller
public class PublicController {

    /**
     * 跳转到登陆页面
     */
    @RequestMapping("login")
    public String login(){
        return "public/login";
    }

    /**
     * 跳转到首页
     */
    @RequestMapping("index")
    public String index(){
        return "public/index";
    }

    /**
     * 跳转到主页工作台
     */
    @RequestMapping("main")
    public String main(){
        return "public/main";
    }

    /**
     * 跳转到日志管理页面
     */
    @RequestMapping("log")
    public String log(){
        return "/log/log";
    }

    /**
     * 跳转到公告管理页面
     */
    @RequestMapping("notice")
    public String notice(){
        return "/notice/notice";
    }

    /**
     * 跳转到部门管理页面
     */
    @RequestMapping("dept")
    public String dept(){
        return "/dept/dept";
    }

    /**
     * 跳转到部门管理页面左边部分
     */
    @RequestMapping("deptLeft")
    public String deptLeft(){
        return "/dept/deptLeft";
    }

    /**
     * 跳转到部门管理页面右边部分
     */
    @RequestMapping("deptRight")
    public String deptRight(){
        return "/dept/deptRight";
    }

    /**
     * 跳转到菜单管理页面
     */
    @RequestMapping("menu")
    public String menu(){
        return "/menu/menu";
    }

    /**
     * 跳转到菜单管理页面左边部分
     */
    @RequestMapping("menuLeft")
    public String menuLeft(){
        return "/menu/menuLeft";
    }

    /**
     * 跳转到菜单管理页面右边部分
     */
    @RequestMapping("menuRight")
    public String menuRight(){
        return "/menu/menuRight";
    }

    /**
     * 跳转到权限管理页面
     */
    @RequestMapping("permission")
    public String permission(){
        return "/permission/permission";
    }

    /**
     * 跳转到权限管理页面左边部分
     */
    @RequestMapping("permissionLeft")
    public String permissionLeft(){
        return "/permission/permissionLeft";
    }

    /**
     * 跳转到权限管理页面右边部分
     */
    @RequestMapping("permissionRight")
    public String permissionRight(){
        return "/permission/permissionRight";
    }

    /**
     * 跳转到角色管理页面
     */
    @RequestMapping("role")
    public String role(){
        return "/role/role";
    }

    /**
     * 跳转到用户管理页面
     */
    @RequestMapping("user")
    public String user(){
        return "/user/user";
    }

    /**
     * 跳转到客户管理页面
     */
    @RequestMapping("customer")
    public String customer(){
        return "/customer/customer";
    }

    /**
     * 跳转到供应商管理页面
     */
    @RequestMapping("provider")
    public String provider(){
        return "/provider/provider";
    }

    /**
     * 跳转到商品管理页面
     */
    @RequestMapping("goods")
    public String goods(){
        return "/goods/goods";
    }

    /**
     * 跳转到商品进货页面
     */
    @RequestMapping("inport")
    public String inport(){
        return "/inport/inport";
    }

    /**
     * 跳转到商品退货页面
     */
    @RequestMapping("outport")
    public String outport(){
        return "/outport/outport";
    }

    /**
     * 跳转到商品退货页面
     */
    @RequestMapping("chufa")
    public String chufa(){
        return "/chufa/chufa";
    }
}
