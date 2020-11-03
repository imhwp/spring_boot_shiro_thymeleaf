package im.hwp.config;


import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import im.hwp.shiro.realms.CustomerRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Bean(name = "shiroDialect")
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }
    @Bean
    public ShiroFilterFactoryBean getShiroFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager){
        //1.创建ShiroFilterFactoryBean
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //2.为shiroFactoryBean对象绑定SecurityManager，在web项目中，需要绑定defaultWebSecurityManager对象
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        //3. 配置系统公共资源和系统受限资源
        Map<String,String> map = new HashMap<String,String>();
        map.put("/user/login","anon");
        map.put("/login.html","anon");
        map.put("/user/getImage","anon");
        map.put("/user/register","anon");
        map.put("/user/registerview","anon");
        map.put("/register.html","anon");
        map.put("/**","authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        //4.设置web的登录页面，当需要认证和授权时，自动跳转该页面
        shiroFilterFactoryBean.setLoginUrl("/user/loginview");
        System.out.println("shiroFilterFactoryBean 初始化成功...");
        return shiroFilterFactoryBean;
    }
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(Realm realm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(realm);
        return defaultWebSecurityManager;

    }
    @Bean
    public Realm getRealm(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        hashedCredentialsMatcher.setHashIterations(1024);
        CustomerRealm customerRealm = new CustomerRealm();
        customerRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        //设置缓存管理器
        customerRealm.setCacheManager(new EhCacheManager());
//        customerRealm.setCacheManager(new RedisCacheManager());
        //开启缓存管理
        customerRealm.setCachingEnabled(true);
        customerRealm.setAuthenticationCachingEnabled(true);
        customerRealm.setAuthorizationCachingEnabled(true);
        customerRealm.setAuthenticationCacheName("authenticationCache");
        customerRealm.setAuthorizationCacheName("authorizationCache");
        return customerRealm;
    }


    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager defaultWebSecurityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(defaultWebSecurityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
