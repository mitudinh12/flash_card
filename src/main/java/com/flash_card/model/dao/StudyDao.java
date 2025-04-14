package com.flash_card.model.dao;

import com.flash_card.model.entity.Study;
import jakarta.persistence.EntityManager;
/**
 * Data Access Object (DAO) class for managing {@link Study} entities.
 * Provides methods for persisting, deleting, updating, and querying studies
 * in the database.
 */
public final class StudyDao {
    /**
     * Singleton instance of the {@link StudyDao}.
     */
    private static StudyDao instance;
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
    private StudyDao(final EntityManager entityManagerParam) {
        this.entityManager = entityManagerParam;
    }
    /**
     * Provides a singleton instance of {@link StudyDao}.
     * If the instance does not exist, it is created with the provided
     * {@link EntityManager}.
     *
     * @param entityManagerParam the {@link EntityManager} to use for database
     *                      operations
     * @return the singleton instance of {@link StudyDao}
     */
    public static StudyDao getInstance(final EntityManager entityManagerParam) {
        if (instance == null) {
            instance = new StudyDao(entityManagerParam);
        }
        return instance;
    }
    /**
     * Persists a {@link Study} entity in the database.
     *
     * @param study the {@link Study} entity to persist
     * @return true if the operation is successful, false otherwise
     */
    public boolean persist(final Study study) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(study);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("Error in persisting Study: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Finds a {@link Study} entity by its ID.
     *
     * @param id the ID of the study
     * @return the {@link Study} entity if found, or null if not found
     */
    public Study findById(final int id) {
        Study study = entityManager.find(Study.class, id);
        return study;
    }
    /**
     * Updates a {@link Study} entity in the database.
     *
     * @param study the {@link Study} entity to update
     * @return true if the operation is successful, false otherwise
     */
    public boolean update(final Study study) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(study);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("Error in updating a study: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Deletes a {@link Study} entity from the database.
     *
     * @param study the {@link Study} entity to delete
     * @return true if the operation is successful, false otherwise
     */
    public boolean delete(final Study study) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(study);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("Error in deleting a study: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Finds a {@link Study} entity by its user ID and flashcard set ID.
     *
     * @param userId the ID of the user
     * @param setId  the ID of the flashcard set
     * @return the {@link Study} entity if found, or null if not found
     */
    public Study findByUserIdAndSetId(final String userId, final int setId) {
        try {
            return entityManager.createQuery(
                            "SELECT s FROM Study s "
                             + "WHERE s.user.userId = :userId "
                             + "AND s.flashcardSet.setId = :setId",
                            Study.class
                    )
                    .setParameter("userId", userId)
                    .setParameter("setId", setId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
