package com.config;

import com.common.UserRealm;

import lombok.Data;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.HashMap;
import java.util.Map;
import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

/**
 * shiro 配置类
 */
@Configuration
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnClass(value = { SecurityManager.class })
@ConfigurationProperties(prefix = "shiro")
@Data
public class ShiroConfig {
    private static final String SHIRO_FILTER = "shiroFilter";
    private static final String SHIRO_DIALECT = "shiroDialect";
    // 放行路径
    private String[] anonUrls;
    // 登出路径
    private String logOutUrl;
    // 拦截路径
    private String[] authcUlrs;

    /**
     * 密码加解密规则
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();

        //MD5散列算法
        credentialsMatcher.setHashAlgorithmName("md5");

        //散列次数2
        credentialsMatcher.setHashIterations(2);

        return credentialsMatcher;
    }

    /**
     * 自定义realm
     */
    @Bean
    public UserRealm userRealm(){
        UserRealm userRealm = new UserRealm();
        // 开启加密
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return userRealm;
    }

    /**
     * 安全管理器
     */
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm());
        return securityManager;
    }

    /**
     * 配置shiro过滤器
     */
    @Bean(SHIRO_FILTER)
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        System.out.println("执行 ShiroFilterFactoryBean ");
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        // 设置 安全管理器SecurityManager
        factoryBean.setSecurityManager(securityManager);

        // 设置未登陆时要登陆的页面
        factoryBean.setLoginUrl("/index.html");

        // 用于存放拦截的路径
        Map<String, String> filterChainDefinitionMap = new HashMap<>();
        // 设置放行的路径
        if (anonUrls != null && anonUrls.length > 0) {
            for (String anon : anonUrls) {
                filterChainDefinitionMap.put(anon, "anon");
            }
        }
        // 设置登出的路径
        if (null != logOutUrl) {
            filterChainDefinitionMap.put(logOutUrl, "logout");
        }
        // 设置拦截的路径
        if (authcUlrs != null && authcUlrs.length > 0) {
            for (String authc : authcUlrs) {
                filterChainDefinitionMap.put(authc, "authc");
            }
        }

        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return factoryBean;
    }

    /**
     * 注册shiro的委托过滤器
     */
    @Bean
    public FilterRegistrationBean<DelegatingFilterProxy> delegatingFilterProxy() {
        FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean = new FilterRegistrationBean<DelegatingFilterProxy>();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName(SHIRO_FILTER);
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }

    /**
     * 使用注解 开始
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 为了能在html页面引用shiro标签
     */
    @Bean(name = SHIRO_DIALECT)
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

}