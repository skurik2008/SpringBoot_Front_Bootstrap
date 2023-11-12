package ru.kata.spring.boot_security.demo.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.kata.spring.boot_security.demo.model.MyUser;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    public EntityManager entityManager;

    @Autowired
    public ApplicationContext context;

    @Override
    public List<MyUser> getUsers() {
        TypedQuery<MyUser> query = entityManager.createQuery("From MyUser u order by u.id ASC", MyUser.class);
        return query.getResultList();
    }

    @Override
    public void addUser(MyUser user) {
        BCryptPasswordEncoder passwordEncoder  = (BCryptPasswordEncoder) context.getBean("passwordEncoder");
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
    public MyUser getUserByName(String login) {
        TypedQuery<MyUser> query = entityManager.createQuery("From MyUser u where u.login = :login", MyUser.class);
        query.setParameter("login", login);
        return query.getSingleResult();
    }
}
