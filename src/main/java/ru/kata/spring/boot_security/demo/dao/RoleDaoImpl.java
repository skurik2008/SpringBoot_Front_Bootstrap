package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    public EntityManager entityManager;

    @Override
    public List<Role> getAllRoles() {
        return entityManager.createQuery("From Role", Role.class).getResultList();
    }

    @Override
    public Set<Role> getRolesByIdList(List<String> roles_id) {
        Set<Role> roles = new HashSet<>();
        for (String id : roles_id) {
            roles.add(entityManager.find(Role.class, Long.valueOf(id)));
        }
        return roles;
    }
}
