package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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
    public void updateUser(MyUser user, Long id_current_user) {
        String newPassword;
        if (user.getPassword().isEmpty()) {
            newPassword = this.getUser(user.getId()).getPassword();
        } else {
            BCryptPasswordEncoder passwordEncoder = (BCryptPasswordEncoder) context.getBean("passwordEncoder");
            newPassword = passwordEncoder.encode(user.getPassword());
        }
        if (user.getId() == id_current_user) {
            UserDetails us = User.builder()
                    .username(user.getEmail())
                    .password(newPassword)
                    .authorities(user.getRoles()).build();
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(new UsernamePasswordAuthenticationToken(us, newPassword, us.getAuthorities()));
        }
        user.setPassword(newPassword);
        entityManager.merge(user);
    }

    @Override
    public MyUser getUserByName(String email) {
        TypedQuery<MyUser> query = entityManager.createQuery("From MyUser u where u.email = :email", MyUser.class);
        query.setParameter("email", email);
        return query.getSingleResult();
    }
}
