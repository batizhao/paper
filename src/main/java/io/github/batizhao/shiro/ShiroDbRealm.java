package io.github.batizhao.shiro;

import io.github.batizhao.domain.Account;
import io.github.batizhao.service.AccountService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ShiroDbRealm extends AuthorizingRealm {

    @Autowired
    private AccountService accountService;

    /**
     * 认证回调函数,登录时调用.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

        String username = token.getUsername();
        Account account = accountService.findByUsername(username);

        if (null == account) {
            throw new UnknownAccountException();
        }

        SimpleAuthenticationInfo simpleAuthenticationInfo
                = new SimpleAuthenticationInfo(new Account(account.getId(), account.getEmail(), account.getUsername(), account.getName(), account.getRoles()),
                                               account.getPassword(), getName());

        return simpleAuthenticationInfo;
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Account account = (Account) principals.getPrimaryPrincipal();
        List<String> roles = Arrays.asList(account.getRoles().split(","));

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 基于Role的权限信息
        for (String role : roles) {
            info.addRole(role);
        }

        return info;
    }

}
