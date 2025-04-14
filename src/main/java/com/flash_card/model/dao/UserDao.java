package com.flash_card.model.dao;

import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * Data Access Object (DAO) for performing CRUD operations on {@link User} entities.
 * Implements the Singleton pattern to ensure a single instance per application lifecycle.
 */
public final class UserDao {

    /**
     * Singleton instance of UserDao.
     */
    private static UserDao userDao = null;

    /**
     * The EntityManager used for database interactions.
     */
    private EntityManager em = null;

    /**
     * Private constructor to enforce singleton pattern.
     *
     * @param entityManager the entity manager to use for persistence
     */
    private UserDao(final EntityManager entityManager) {
        em = entityManager;
    }

    /**
     * Returns the singleton instance of UserDao.
     * If not already created, a new instance is initialized.
     *
     * @param em the entity manager to initialize the DAO
     * @return the singleton instance of UserDao
     */
    public static UserDao getInstance(final EntityManager em) {
        if (userDao == null) {
            userDao = new UserDao(em);
        }
        return userDao;
    }

    /**
     * Persists a new {@link User} entity into the database.
     *
     * @param user the user to be persisted
     * @return true if persistence was successful; false otherwise
     */
    public boolean persist(final User user) {
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

    /**
     * Updates an existing {@link User} entity in the database.
     *
     * @param user the user to be updated
     * @return true if update was successful; false otherwise
     */
    public boolean update(final User user) {
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

    /**
     * Finds a {@link User} by their ID.
     *
     * @param id the user ID
     * @return the User if found; null otherwise
     */
    public User findById(final String id) {
        return em.find(User.class, id);
    }

    /**
     * Finds a {@link User} by their email address.
     *
     * @param email the user's email address
     * @return the User if found; null otherwise
     */
    public User findByEmail(final String email) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        List<User> users = query.getResultList();
        return users.isEmpty() ? null : users.get(0);
    }

    /**
     * Deletes a {@link User} from the database.
     *
     * @param user the user to be deleted
     * @return true if deletion was successful; false otherwise
     */
    public boolean delete(final User user) {
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
