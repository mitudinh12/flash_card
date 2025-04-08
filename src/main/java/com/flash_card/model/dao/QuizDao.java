package com.flash_card.model.dao;

import com.flash_card.model.entity.Quiz;
import jakarta.persistence.EntityManager;

import java.util.List;
/**
 * Data Access Object (DAO) class for managing {@link Quiz} entities.
 * Provides methods for persisting, deleting, updating, and querying quizzes
 * in the database.
 */
public class QuizDao {
    /**
     * Singleton instance of the {@link QuizDao}.
     */
    private static QuizDao instance;
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
    private QuizDao(final EntityManager entityManagerParam) {
        this.entityManager = entityManagerParam;
    }
    /**
     * Provides a singleton instance of {@link QuizDao}.
     * If the instance does not exist, it is created with the provided
     * {@link EntityManager}.
     *
     * @param entityManagerParam the {@link EntityManager} to use for database
     *                      operations
     * @return the singleton instance of {@link QuizDao}
     */
    public static QuizDao getInstance(final EntityManager entityManagerParam) {
        if (instance == null) {
            instance = new QuizDao(entityManagerParam);
        }
        return instance;
    }
    /**
     * Persists a {@link Quiz} entity in the database.
     *
     * @param quiz the {@link Quiz} entity to persist
     * @return true if the operation is successful, false otherwise
     */
    public boolean persist(Quiz quiz) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(quiz);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.err.println("Error in persisting Quiz: " + e.getMessage());
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return false;
        }
    }
    /**
     * Finds a {@link Quiz} entity by its ID.
     *
     * @param id the ID of the quiz
     * @return the {@link Quiz} entity if found, or null if not found
     */
    public Quiz findById(int id) {
        Quiz quiz = entityManager.find(Quiz.class, id);
        return quiz;
    }
    /**
     * Updates a {@link Quiz} entity in the database.
     *
     * @param quiz the {@link Quiz} entity to update
     * @return true if the operation is successful, false otherwise
     */
    public boolean update(Quiz quiz) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(quiz);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.err.println("Error in updating a quiz: " + e.getMessage());
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return false;
        }
    }
    /**
     * Deletes a {@link Quiz} entity from the database.
     *
     * @param quiz the {@link Quiz} entity to delete
     * @return true if the operation is successful, false otherwise
     */
    public boolean delete(Quiz quiz) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(quiz);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.err.println("Error in deleting a quiz: " + e.getMessage());
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return false;
        }
    }
    /**
     * Finds all {@link Quiz} entities associated with a specific user ID
     * and flashcard set ID.
     *
     * @param userId the ID of the user
     * @param setId  the ID of the flashcard set
     * @return a list of {@link Quiz} entities associated with the user and set
     */
    public List<Quiz> findByUserIdAndSetId(String userId, int setId) {
        return entityManager.createQuery(
                        "SELECT q FROM Quiz q "
                         + "WHERE q.user.userId = :userId "
                         + "AND q.flashcardSet.setId = :setId",
                        Quiz.class
                )
                .setParameter("userId", userId)
                .setParameter("setId", setId)
                .getResultList();
    }
}
