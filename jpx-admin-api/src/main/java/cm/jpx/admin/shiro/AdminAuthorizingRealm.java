package cm.jpx.admin.shiro;


import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import cm.jpx.core.util.bcrypt.BCryptPasswordEncoder;
import cm.jpx.db.domain.LitemallAdmin;
import cm.jpx.db.service.LitemallAdminService;
import cm.jpx.db.service.LitemallPermissionService;
import cm.jpx.db.service.LitemallRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

public class AdminAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private LitemallAdminService adminService;
    @Autowired
    private LitemallRoleService roleService;
    @Autowired
    private LitemallPermissionService permissionService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }

        LitemallAdmin admin = (LitemallAdmin) getAvailablePrincipal(principals);
        Integer[] roleIds = admin.getRoleIds();
        // 用户的角色集合
        Set<String> roles = roleService.queryByIds(roleIds);
        Set<String> permissions = permissionService.queryByRoleIds(roleIds);
        // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(roles);
        info.setStringPermissions(permissions);
        return info;
    }

    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        //获取输入的用户名和密码
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        String password = new String(upToken.getPassword());

        if (StringUtils.isEmpty(username)) {
            throw new AccountException("用户名不能为空");
        }
        if (StringUtils.isEmpty(password)) {
            throw new AccountException("密码不能为空");
        }

        //根据 用户名 获取 用户账户信息
        List<LitemallAdmin> adminList = adminService.findAdmin(username);
        Assert.state(adminList.size() < 2, "同一个用户名存在两个账户");
        if (adminList.size() == 0) {
            throw new UnknownAccountException("找不到用户（" + username + "）的帐号信息");
        }
        LitemallAdmin admin = adminList.get(0);

        //密码比较
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, admin.getPassword())) {
            throw new UnknownAccountException("找不到用户（" + username + "）的帐号信息");
        }

        return new SimpleAuthenticationInfo(admin, password, getName());
    }

}
