package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.model.MyUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class UserAndRoleServiceImpl implements UserService, RoleService, UserDetailsService {
    private final UserDao userDao;
    private final RoleDao roleDao;

    @Autowired
    public UserAndRoleServiceImpl(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @Override
    public List<MyUser> getUsers() {
        return userDao.getUsers();
    }

    @Override
    @Transactional
    public void addUser(MyUser user) {
        userDao.addUser(user);
    }

    @Override
    public MyUser getUser(Long id) {
        return userDao.getUser(id);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }

    @Override
    @Transactional
    public void updateUser(MyUser user) {
        userDao.updateUser(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser user = userDao.getUserByName(username);
        if (user == null) {
            throw  new UsernameNotFoundException(String.format("User c логином %s не обнаружен", username));
        }
        UserDetails us = User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles()).build();
        return us;
    }

    @Override
    public MyUser getUserByName(String login) {
        return userDao.getUserByName(login);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleDao.getAllRoles();
    }

    @Override
    public Set<Role> getRolesByIds(List<String> roles_id) {
        return roleDao.getRolesByIdList(roles_id);
    }

}
