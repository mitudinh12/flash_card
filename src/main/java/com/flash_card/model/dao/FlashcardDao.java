package com.flash_card.model.dao;

import com.flash_card.framework.DifficultyLevel;
import com.flash_card.model.entity.Flashcard;
import jakarta.persistence.EntityManager;

import java.util.List;
/**
 * Data Access Object (DAO) class for managing {@link Flashcard} entities.
 * Provides methods for persisting, deleting, updating, and querying flashcards
 * in the database.
 */
public final class FlashcardDao {
    /**
     * Singleton instance of the {@link FlashcardDao}.
     */
    private static FlashcardDao instance;
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
    private FlashcardDao(final EntityManager entityManagerParam) {
        this.entityManager = entityManagerParam;
    }
    /**
     * Provides a singleton instance of {@link FlashcardDao}.
     * If the instance does not exist, it is created with the provided
     * {@link EntityManager}.
     *
     * @param entityManager the {@link EntityManager} to use for database
     *                      operations
     * @return the singleton instance of {@link FlashcardDao}
     */
    public static FlashcardDao getInstance(final EntityManager entityManager) {
        if (instance == null) {
            instance = new FlashcardDao(entityManager);
        }
        return instance;
    }
    /**
     * Persists a {@link Flashcard} entity in the database.
     *
     * @param flashcard the {@link Flashcard} entity to persist
     * @return true if the operation is successful, false otherwise
     */
    public boolean persist(final Flashcard flashcard) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(flashcard);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.err.println("Error in persisting Flashcard: " + e.getMessage());
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return false;
        }
    }
    /**
     * Finds a {@link Flashcard} entity by its ID.
     *
     * @param id the ID of the flashcard
     * @return the {@link Flashcard} entity if found, or null if not found
     */
    public Flashcard findById(final int id) {
        return entityManager.find(Flashcard.class, id);

    }
    /**
     * Updates a {@link Flashcard} entity in the database.
     *
     * @param flashcard the {@link Flashcard} entity to update
     * @return true if the operation is successful, false otherwise
     */
    public boolean update(final Flashcard flashcard) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(flashcard);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.err.println("Error in updating a flashcard: " + e.getMessage());
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return false;
        }
    }
    /**
     * Deletes a {@link Flashcard} entity from the database.
     *
     * @param flashcard the {@link Flashcard} entity to delete
     * @return true if the operation is successful, false otherwise
     */
    public boolean delete(final Flashcard flashcard) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(flashcard);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.err.println("Error in deleting a flashcard: " + e.getMessage());
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return false;
        }
    }

    /**
     * Finds all {@link Flashcard} entities associated with a specific set ID.
     *
     * @param setId the ID of the flashcard set
     * @return a list of {@link Flashcard} entities associated with the set
     */
    public List<Flashcard> findBySetId(final int setId) {
        return entityManager.createQuery(
                        "SELECT f FROM Flashcard f "
                         + "WHERE f.flashcardSet.setId = :setId",
                        Flashcard.class
                )
                .setParameter("setId", setId)
                .getResultList();

    }
    /**
     * Finds all hard {@link Flashcard} entities associated with a specific set
     * and study ID.
     *
     * @param setId      the ID of the flashcard set
     * @param studyId    the ID of the study
     * @return a list of hard {@link Flashcard} entities
     */
    public List<Flashcard> getHardFlashcards(final int setId, final int studyId) {
        return entityManager.createQuery(
                        "SELECT f FROM Flashcard f "
                         + "JOIN CardDifficultLevel cdl "
                         + "ON f.cardId = cdl.flashcard.cardId "
                         + "WHERE f.flashcardSet.setId = :setId "
                         + "AND cdl.study.studyId = :studyId "
                         + "AND cdl.difficultLevel = :difficultyLevel",
                        Flashcard.class
                )
                .setParameter("setId", setId)
                .setParameter("studyId", studyId)
                .setParameter("difficultyLevel", DifficultyLevel.hard)
                .getResultList();
    }
}
