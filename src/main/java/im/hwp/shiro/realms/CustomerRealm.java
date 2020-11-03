package im.hwp.shiro.realms;

import im.hwp.entity.Role;
import im.hwp.entity.User;
import im.hwp.service.UserService;
import im.hwp.utils.MyByteSource;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CustomerRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String primaryPrincipal = (String) principalCollection.getPrimaryPrincipal();
        System.out.println("==========用户授权开始执行，当前用户为：" + primaryPrincipal + "============");
        List<Role> roles = userService.findRoleByUsername(primaryPrincipal);
        if(!CollectionUtils.isEmpty(roles)){
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            roles.forEach(role -> {
                simpleAuthorizationInfo.addRole(role.getName());
                simpleAuthorizationInfo.addStringPermission("user:delete:*");
            });
            return simpleAuthorizationInfo;
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("用户认证程序执行");
        String principal = (String) authenticationToken.getPrincipal();
        //根据获取的用户名去数据库中获取对应的加密后密码和随机盐
        User user = userService.findUser(principal);
        if(user !=null){
            //封装信息有 用户名 密码 随机盐 和 当前对象
            return new SimpleAuthenticationInfo(user.getUsername(),user.getPassword(), MyByteSource.Util.bytes(user.getSalt()),this.getName());
        }
        return null;
    }
}
