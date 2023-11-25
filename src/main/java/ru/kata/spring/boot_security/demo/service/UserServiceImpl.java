package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.kata.spring.boot_security.demo.model.MyUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
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
    public void updateUser(MyUser user, Long id_current_user) {
        userDao.updateUser(user, id_current_user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser user = userDao.getUserByName(username);
        if (user == null) {
            throw  new UsernameNotFoundException(String.format("User c логином %s не обнаружен", username));
        }
        UserDetails us = User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles()).build();
        return us;
    }

    @Override
    public MyUser getUserByName(String email) {
        return userDao.getUserByName(email);
    }
}
