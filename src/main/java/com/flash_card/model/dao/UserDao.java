package com.flash_card.model.dao;

import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import com.flash_card.model.datasource.MariaDbJpaConnection;

public class UserDao {
    static EntityManager em = MariaDbJpaConnection.getInstance();

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
}
