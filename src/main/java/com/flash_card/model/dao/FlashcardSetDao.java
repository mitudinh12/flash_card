package com.flash_card.model.dao;

import com.flash_card.model.entity.FlashcardSet;
import jakarta.persistence.EntityManager;

import java.util.List;
/**
 * Data Access Object (DAO) class for managing {@link FlashcardSet} entities.
 * Provides methods for persisting, deleting, updating, and querying flashcard
 * sets in the database.
 */
public class FlashcardSetDao {
    /**
     * Singleton instance of the {@link FlashcardSetDao}.
     */
    private static FlashcardSetDao instance;
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
    FlashcardSetDao(final EntityManager entityManagerParam) {
        this.entityManager = entityManagerParam;
    }
    /**
     * Provides a singleton instance of {@link FlashcardSetDao}.
     * If the instance does not exist, it is created with the provided
     * {@link EntityManager}.
     *
     * @param entityManager the {@link EntityManager} to use for database
     *                      operations
     * @return the singleton instance of {@link FlashcardSetDao}
     */
    public static FlashcardSetDao getInstance(final EntityManager entityManager) {
        if (instance == null) {
            instance = new FlashcardSetDao(entityManager);
        }
        return instance;
    }
    /**
     * Persists a {@link FlashcardSet} entity in the database.
     *
     * @param flashcardSet the {@link FlashcardSet} entity to persist
     * @return true if the operation is successful, false otherwise
     */
    public boolean persist(FlashcardSet flashcardSet) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(flashcardSet);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("Error in persisting FlashcardSet: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Finds a {@link FlashcardSet} entity by its ID.
     *
     * @param id the ID of the flashcard set
     * @return the {@link FlashcardSet} entity if found, or null if not found
     */
    public FlashcardSet findById(int id) {
        return entityManager.find(FlashcardSet.class, id);
    }
    /**
     * Updates a {@link FlashcardSet} entity in the database.
     *
     * @param flashcardSet the {@link FlashcardSet} entity to update
     * @return true if the operation is successful, false otherwise
     */
    public boolean update(FlashcardSet flashcardSet) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(flashcardSet);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.err.println("Error in updating FlashcardSet: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Deletes a {@link FlashcardSet} entity from the database.
     *
     * @param flashcardSet the {@link FlashcardSet} entity to delete
     * @return true if the operation is successful, false otherwise
     */
    public boolean delete(FlashcardSet flashcardSet) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(flashcardSet);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.err.println("Error in deleting FlashcardSet: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Finds all {@link FlashcardSet} entities associated with a specific user ID.
     *
     * @param userId the ID of the user
     * @return a list of {@link FlashcardSet} entities associated with the user
     */
    public List<FlashcardSet> findByUserId(String userId) {
        return entityManager.createQuery(
                        "SELECT fs FROM FlashcardSet fs "
                         + "WHERE fs.flashcardCreator.userId = :userId",
                        FlashcardSet.class
                )
                .setParameter("userId", userId)
                .getResultList();
    }
}
