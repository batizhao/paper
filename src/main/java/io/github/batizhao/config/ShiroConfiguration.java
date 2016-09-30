package io.github.batizhao.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import io.github.batizhao.shiro.ShiroDbRealm;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@Configuration
public class ShiroConfiguration {

    @Bean
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager());
        factoryBean.setSuccessUrl("/dashboard");
        factoryBean.setLoginUrl("/login");
        factoryBean.setUnauthorizedUrl("/403");

        Map<String, String> filterChainDefinitionManager = new LinkedHashMap<String, String>();
        filterChainDefinitionManager.put("/dashboard/**", "authc");
        filterChainDefinitionManager.put("/account/**", "authc,roles[administrator]");
        filterChainDefinitionManager.put("/role/**", "authc,roles[administrator]");
        filterChainDefinitionManager.put("/course/**", "authc,roles[administrator]");
        filterChainDefinitionManager.put("/score/**", "authc");
        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionManager);
        return factoryBean;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager() {
        final DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm());
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    @Bean
    public DefaultWebSessionManager sessionManager() {
        final DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(sessionDao());
        sessionManager.setGlobalSessionTimeout(1800000);
        return sessionManager;
    }

    @Bean
    public SessionDAO sessionDao() {
        return new EnterpriseCacheSessionDAO();
    }

    @Bean(name = "realm")
    @DependsOn("lifecycleBeanPostProcessor")
    public ShiroDbRealm realm() {
        final ShiroDbRealm realm = new ShiroDbRealm();
        realm.setCredentialsMatcher(credentialsMatcher());
        return realm;
    }

    @Bean(name = "credentialsMatcher")
    public HashedCredentialsMatcher credentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher("SHA-1");
        matcher.setHashIterations(1);
        return matcher;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    //use shiro tag for thymeleaf
    @Bean(name = "shiroDialect")
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }
}
