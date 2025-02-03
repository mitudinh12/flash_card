package com.flash_card.model.dao;

import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import com.flash_card.model.datasource.MariaDbJpaConnection;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class UserDao {
    static EntityManager em = MariaDbJpaConnection.getInstance();
    private static UserDao userDao = null;

    public static UserDao getInstance() {
        if (userDao== null) {
            userDao = new UserDao();
        }
        return userDao;
    }

    public void persist(User user) {
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
    }

    public void update(User user) {
        em.getTransaction().begin();
        em.merge(user);
        em.getTransaction().commit();
    }

    public User findById(String id) {
        return em.find(User.class, id);
    }

    public User findByEmail(String email) {
        TypedQuery query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        List<User> users = query.getResultList();
        return users.isEmpty() ? null : users.get(0);
    }
}
