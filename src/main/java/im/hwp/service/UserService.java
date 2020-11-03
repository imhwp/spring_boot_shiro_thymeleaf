package im.hwp.service;

import im.hwp.entity.Role;
import im.hwp.entity.User;

import java.util.List;

public interface UserService {
    void register(User user);
    User findUser(String username);
    List<Role> findRoleByUsername(String username);
}
