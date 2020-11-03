package im.hwp.service.impl;

import im.hwp.dao.UserDao;
import im.hwp.entity.Role;
import im.hwp.entity.User;
import im.hwp.service.UserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public void register(User user) {
        //对用户明文密码进行md5操作
        String salt = UUID.randomUUID().toString().replaceAll("-", "");
        Md5Hash md5Hash = new Md5Hash(user.getPassword(),salt,1024);
        String newPassWd = md5Hash.toHex();
        user.setPassword(newPassWd);
        user.setSalt(salt);
        userDao.save(user);
    }

    @Override
    public User findUser(String username) {
        User user = userDao.findByUsername(username);
        return user;
    }

    @Override
    public List<Role> findRoleByUsername(String username) {
        User user = userDao.findRoleByUsername(username);
        return user.getRoles();
    }
}

