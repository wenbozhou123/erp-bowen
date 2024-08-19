package com.bowen.erpidm.shiroAuth;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

public class KickoutSessionControlFilter extends AccessControlFilter {

    private static final Logger log = LoggerFactory.getLogger(KickoutSessionControlFilter.class);
    private String kickoutUrl = "";         //踢出后的地址
    private boolean kickoutAfter = false;   //踢出后之前的登录/之后的登录的用户， 默认踢出之前登录的用户
    private int maxSession = 1;     //同一个账号最大会话数。默认 1

    private SessionManager sessionManager;

    private Cache<String, Deque<Serializable>> cache;

    public String getKickoutUrl() {
        return kickoutUrl;
    }

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public boolean isKickoutAfter() {
        return kickoutAfter;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public int getMaxSession() {
        return maxSession;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public Cache<String, Deque<Serializable>> getCache() {
        return cache;
    }

    public void setCache(Cache<String, Deque<Serializable>> cache) {
        this.cache = cache;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            // 如果没有登录，直接进行之后的流程
            return true;
        }

        Session session = subject.getSession();
        String account = (String) subject.getPrincipal();

        Serializable sessionId =session.getId();

        //同步控制
        Deque<Serializable> deque = cache.get(account);
        if (deque == null) {
            deque = new LinkedList<>();
            cache.put(account, deque);
        }

        //如果队列里面没有此sessionId, 且用户没有被踢出，放入队列
        if (!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
            deque.push(sessionId);
            cache.put(account, deque);
        }

        //如果队列里的sessionId数超出最大会话数，并开始踢人
        while (deque.size() > maxSession) {
            Serializable kickoutSessionId = null;
            if (kickoutAfter) {     //踢出后者
                kickoutSessionId = deque.removeFirst();
                cache.put(account, deque);
            }else {     //踢出前者
                kickoutSessionId = deque.removeLast();
                cache.put(account, deque);
            }

            try {
                Session kickSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                if (kickSession != null) {
                    //设置会话的kickout属性， 表示踢出了
                    kickSession.setAttribute("kickout", true);

                }

            }catch (Exception e) {
                //ignore exception
            }
        }

        //如果被踢出了，直接退出，重定向到踢出后的地址
        if (session.getAttribute("kickout") != null) {
            // 会话被踢出了
            try {
                subject.logout();
            }catch (Exception e) {
                log.error("踢出用户，退出登录失败", e);
            }
            saveRequest(request);

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


        }

        return false;
    }
}
