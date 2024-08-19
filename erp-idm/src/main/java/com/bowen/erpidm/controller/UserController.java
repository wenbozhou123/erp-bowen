package com.bowen.erpidm.controller;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @PostMapping("/shiroLogin")
    public String shiroLogin(@RequestParam(name = "username", required = false) String username, @RequestParam(name = "password", required = false) String password) {
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
