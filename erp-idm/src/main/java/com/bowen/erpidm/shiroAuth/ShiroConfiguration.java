package com.bowen.erpidm.shiroAuth;

import jakarta.servlet.Filter;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    @Autowired
    private DataSource dataSource;

    @Value("300")
    private long expire = 30 * 24 * 60 * 60;   //30天

    /**
     * Shiro 核心过滤器
     * */

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManagerMyRealm") SecurityManager securityManager){
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager);   // Shiro核心安全接口， 这个属性必须设置
        filterFactoryBean.setLoginUrl("/home/login.shtml"); // 身份验证失败，跳转到登录页面
        filterFactoryBean.setUnauthorizedUrl("/403");       // 权限验证失败，跳转到指定页面
        filterFactoryBean.setSuccessUrl("/index.jsp");      // 身份验证成功，跳转到指定页面

        /**
            anon 任何人都可以访问
            authc 只有认证后才可以访问
            logout 只有登陆后可以访问
            roles[角色名] 只有拥有特定角色才能访问
            perms["行为"] 只有拥有某种行为的才能访问， 例如 /admin/deluser = perms["admin:delete"]
        */
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/logout", "anon");

        //表示 访问/** 首先要通过authc和myAuthenticator过滤器
        filterChainDefinitionMap.put("/**", "authc,myAuthenticator,kickout");
        //自定义的过滤器实现
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("myAuthenticator", new MyAuthenticator()); // 自定义的过滤器
        filterMap.put("kickout", null);  // 限制同一账号同时在线的个数

        filterFactoryBean.setFilters(filterMap);
        filterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return filterFactoryBean;
    }

    @Bean("securityManagerJdbc")
    public SecurityManager securityManager(JdbcRealm jdbcRealm) {
        return new DefaultWebSecurityManager(jdbcRealm);
    }

    //todo 确实会话管理和缓存管理
    @Bean("securityManagerMyRealm")
    public SecurityManager securityManager(MyShiroRealm myShiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm);
        // securityManager.setSessionManager(null);
        // securityManager.setCacheManager(null);
        return securityManager;
    }


    @Bean("sessionManager")
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultWebSessionManager sessionManager(){
        return null;
    }


    @Bean("jdbcRealm")
    public JdbcRealm jdbcRealm() {
        JdbcRealm jdbcRealm = new JdbcRealm();
        //修改查询用户的sql
        String authenticationQuery = "select password from sys_user where user_code = ?";
        //修改查询用户角色的sql
        String userRolesQuery = "select t2.role_id from sys_user t1,sys_user_role t2 where t1.user_id = t2.user_id and t1.user_code = ?";
        //修改查询角色权限的sql
        String permissionsQuery = "select perms from sys_menu t1,sys_role_menu t2 where t1.menu_id = t2.menu_id and role_id = ?";
        //设置允许查询权限（默认是false）
        jdbcRealm.setPermissionsLookupEnabled(true);
        jdbcRealm.setAuthenticationQuery(authenticationQuery);
        jdbcRealm.setUserRolesQuery(userRolesQuery);
        jdbcRealm.setPermissionsQuery(permissionsQuery);
        //设置数据源
        jdbcRealm.setDataSource(dataSource);
        return jdbcRealm;
    }

    /**
     * MyShiroRealm 是自定义的认证类，继承于AuthorizingRealm
     * 负责用户的认证和权限处理，可以参考JdbcRealm实现
     * */
    @Bean("myShiroRealm")
    public MyShiroRealm myShiroRealm(CredentialsMatcher hashCredentialsMatcher) {
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        myShiroRealm.setCredentialsMatcher(hashCredentialsMatcher);
        return myShiroRealm;
    }

    /**
     * HashedCredentialsMatcher 对密码进行编码
     * 防止密码在数据库明文保存，当然在登陆时候，也是这个类对form李输入的密码进行编码
     * */
    @Bean("hashCredentialsMatcher")
    public CredentialsMatcher hashCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");   //散列算法 为md5
        hashedCredentialsMatcher.setHashIterations(2);  //散列次数
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);  //true 代表Hex存储，否则Base64-encode存储
        return hashedCredentialsMatcher;
    }

    /**
     * 用于管理shiro组件的生命周期
     * LifecycleBeanPostProcessor 是DestructionBeanPostProcessor的子类
     * 负责 org.apache.shiro.util.Initializable 类型的bean的生命周期，初始化和销毁
     * 主要是 AuthorizingRealm的子类，以及EnCacheManager类
     * @return
     */
    @Bean("lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * DefaultAdvisorAutoProxyCreator， spring的一个bean,由Advisor决定对哪些类的方法进行AOP代理
     * 检查容器中是否存在目标类型（ConditionalOnMissingBean注解的value值）的bean了，
     *      如果存在就跳过原始bean的BeanDefinition加载动作
     * */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator=new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * AuthorizationAttributeSourceAdvisor， shiro里实现的Advisor类，
     * 内部使用 AopAllianceAnnotationAuthorizingMethodInterceptor
     * */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManagerMyRealm") SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }



}
