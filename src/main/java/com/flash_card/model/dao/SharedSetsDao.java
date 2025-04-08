package com.flash_card.model.dao;

import com.flash_card.model.entity.SharedSet;
import jakarta.persistence.EntityManager;

import java.util.List;
/**
 * Data Access Object (DAO) class for managing {@link SharedSet} entities.
 * Provides methods for persisting, deleting, updating, and querying shared
 * sets in the database.
 */
public class SharedSetsDao {
    /**
     * Singleton instance of the {@link SharedSetsDao}.
     */
    private static SharedSetsDao instance;
    /**
     * The {@link EntityManager} used for database operations.
     */
    private final EntityManager entityManager;
    /**
     * Private constructor to initialize the DAO with the provided
     * {@link EntityManager}.
     *
     * @param entityManagerParam the {@link EntityManager} to use for database
     *                      operations
     */
    private SharedSetsDao(final EntityManager entityManagerParam) {
        this.entityManager = entityManagerParam;
    }
    /**
     * Provides a singleton instance of {@link SharedSetsDao}.
     * If the instance does not exist, it is created with the provided
     * {@link EntityManager}.
     *
     * @param entityManagerParam the {@link EntityManager} to use for database
     *                      operations
     * @return the singleton instance of {@link SharedSetsDao}
     */
    public static SharedSetsDao getInstance(final EntityManager entityManagerParam) {
        if (instance == null) {
            instance = new SharedSetsDao(entityManagerParam);
        }
        return instance;
    }
    /**
     * Persists a {@link SharedSet} entity in the database.
     *
     * @param sharedSet the {@link SharedSet} entity to persist
     * @return true if the operation is successful, false otherwise
     */
    public boolean persist(SharedSet sharedSet) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(sharedSet);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.err.println("Error in persisting a SharedSet: " + e.getMessage());
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return false;
        }
    }
    /**
     * Finds a {@link SharedSet} entity by its ID.
     *
     * @param id the ID of the shared set
     * @return the {@link SharedSet} entity if found, or null if not found
     */
    public SharedSet findById(int id) {
        return entityManager.find(SharedSet.class, id);
    }
    /**
     * Deletes a {@link SharedSet} entity from the database.
     *
     * @param sharedSet the {@link SharedSet} entity to delete
     * @return true if the operation is successful, false otherwise
     */
    public boolean delete(SharedSet sharedSet) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(sharedSet);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.err.println("Error in deleting a SharedSet: " + e.getMessage());
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return false;
        }
    }
    /**
     * Finds all {@link SharedSet} entities associated with a specific user ID.
     *
     * @param userId the ID of the user
     * @return a list of {@link SharedSet} entities associated with the user
     */
    public List<SharedSet> findByUserId(String userId) {
        return entityManager.createQuery("SELECT s FROM SharedSet s WHERE s.user.userId = :userId", SharedSet.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    /**
     * Finds a {@link SharedSet} entity by its flashcard set ID and user ID.
     *
     * @param flashcardSetId   the ID of the flashcard set
     * @param userId the ID of the user
     * @return the {@link SharedSet} entity if found, or null if not found
     */
    public SharedSet findBySetIdAndUserId(int flashcardSetId, String userId) {
        try {
            return entityManager.createQuery(
                            "SELECT s FROM SharedSet s "
                             + "WHERE s.flashcardSet.setId = :fsId "
                             + "AND s.user.userId = :userId",
                            SharedSet.class
                    )
                    .setParameter("fsId", flashcardSetId)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (Exception e) {
            System.err.println("Error in finding a SharedSet: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
