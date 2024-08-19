package com.bowen.erpidm.shiroAuth;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.lang.util.ByteSource;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * AuthorizingRealm 可以同时提供认证和授权
 * */
@Slf4j
public class MyShiroRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("身份验证");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        String password = new String(token.getPassword());


        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也有时间间隔机制，2分钟不会重复执行该方法
        //用户密码的比对是Shiro帮我们完成的
        SimpleAuthenticationInfo sai = new SimpleAuthenticationInfo(
                username,    // user account
                password,   //密码
                ByteSource.Util.bytes("salt"),   //salt
                "MyShiroRealm"
        );
        return sai;
    }
}
