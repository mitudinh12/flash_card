package com.flash_card.model.dao;

import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public final class UserDao {
    private static UserDao userDao = null;
    private EntityManager em = null;

    private UserDao(EntityManager entityManager) {
        em = entityManager;
    }

    public static UserDao getInstance(EntityManager em) {
        if (userDao == null) {
            userDao = new UserDao(em);
        }
        return userDao;
    }

    public boolean persist(User user) {
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error in persisting User: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(User user) {
        try {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error in updating User: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

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

    public boolean delete(User user) {
        try {
            em.getTransaction().begin();
            em.remove(user);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error in deleting User: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
