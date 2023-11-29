package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.MyUser;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;


@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    public EntityManager entityManager;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserDaoImpl(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<MyUser> getUsers() {
        TypedQuery<MyUser> query = entityManager.createQuery("From MyUser u order by u.id ASC", MyUser.class);
        return query.getResultList();
    }

    @Override
    public void addUser(MyUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        entityManager.persist(user);
    }

    @Override
    public MyUser getUser(Long id) {
        return entityManager.find(MyUser.class, id);
    }

    @Override
    public void deleteUser(Long id) {
        entityManager.remove(entityManager.find(MyUser.class, id));
    }

    @Override
    public void updateUser(MyUser user) {
        entityManager.merge(user);
    }

    @Override
    public MyUser getUserByName(String email) {
        TypedQuery<MyUser> query = entityManager.createQuery("From MyUser u where u.email = :email", MyUser.class);
        query.setParameter("email", email);
        return query.getSingleResult();
    }
}
