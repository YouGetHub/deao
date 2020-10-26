package com.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 退出 清除sessionID
 */
@Controller
@RequestMapping("public")
public class LogoutController {


    @RequestMapping("/logout")
    @ResponseBody
    public void findMyPlayRecord(HttpServletRequest request) {
        System.out.println("已退出" + request.getSession().getId());
        System.out.println("logout成功");

/*        Subject subject = SecurityUtils.getSubject();

        if(subject.getPrincipals() != null ){

        }

        SecurityUtils.getSubject().logout();

        return JsonData.buildSuccess("logout成功");

    }*/
    }
}
