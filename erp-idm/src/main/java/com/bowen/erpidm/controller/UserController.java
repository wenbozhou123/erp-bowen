package com.bowen.erpidm.controller;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user")
public class UserController {
    @PostMapping("/shiroLogin")
    public String shiroLogin(String username, String password) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        subject.login(token);
        String sessionId = subject.getSession().getId().toString();
        return sessionId;
    }

    @PostMapping("/shiroLogout")
    public String shiroLoginOut() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return null;
    }


}
