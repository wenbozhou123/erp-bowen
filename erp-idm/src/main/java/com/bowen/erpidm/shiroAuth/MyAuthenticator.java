package com.bowen.erpidm.shiroAuth;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import java.io.PrintWriter;

@Slf4j
public class MyAuthenticator extends FormAuthenticationFilter {

    public MyAuthenticator() {
        super();
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return super.isAccessAllowed(request, response, mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        log.info("shiro filter authc拦截，请先登录");
        HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;
        String origin  = req.getHeader("origin");
        res.setHeader("Access-Control-Allow-Origin", origin);
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        res.setHeader("Access-Control-Max-Age", "3600");
        res.setHeader("Access-Control-Allow-Headers", "Origin, x-requested-with, Content-Type, Accept, Authorization");
        res.setContentType("application/json; charset=utf-8");
        res.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = res.getWriter();
        writer.write("请先登陆");
        writer.close();
        // return  false 拦截, true 放行
        return false;
    }
}
