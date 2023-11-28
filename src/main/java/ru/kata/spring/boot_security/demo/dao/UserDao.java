package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.MyUser;
import java.util.List;

public interface UserDao {List<MyUser> getUsers();
    void addUser(MyUser user);
    MyUser getUser(Long id);
    void deleteUser(Long id);
    void updateUser(MyUser user);
    MyUser getUserByName(String username);

}
