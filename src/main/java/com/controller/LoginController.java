package com.controller;

import cn.hutool.core.date.DateTime;
import com.common.ActiverUser;
import com.common.JsonData;
import com.common.WebUtils;
import com.domain.Log;
import com.service.LogService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 记录登陆日志
 */
@RestController
@RequestMapping("login")
public class LoginController {
    @Autowired
    private LogService logService;
    /**
     * 登陆
     * @param loginname
     * @param pwd
     * @return
     */
    @RequestMapping("login")
    public JsonData login (String loginname, String pwd){
        System.out.println(loginname+" " + pwd);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginname,pwd);
        try {
             System.out.println("123");
             subject.login(usernamePasswordToken);
             ActiverUser activerUser = (ActiverUser)subject.getPrincipal();
             WebUtils.getSession().setAttribute("user",activerUser.getUser());
             // 登录日志
            Log log = new Log();
            log.setLoginname(activerUser.getUser().getName()+"-"+activerUser.getUser().getLoginname());
            log.setLoginip(WebUtils.getRequest().getRemoteAddr());
            log.setLogintime(new DateTime());
            logService.save(log);
             return new JsonData(200,"登录成功");
        }catch (Exception e){
            e.printStackTrace();
            return new JsonData(-1,"用户名或密码不正确");
        }
    }
}
