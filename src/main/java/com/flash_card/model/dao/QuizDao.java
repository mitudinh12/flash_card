package com.flash_card.model.dao;
import com.flash_card.model.entity.Quiz;
import jakarta.persistence.EntityManager;

import java.util.List;

public class QuizDao {
    private static QuizDao instance;
    private EntityManager entityManager;

    private QuizDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public static QuizDao getInstance(EntityManager entityManager) {
        if (instance == null) {
            instance = new QuizDao(entityManager);
        }
        return instance;
    }

    public void persist(Quiz quiz) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(quiz);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error in persisting Quiz: " + e.getMessage());
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    public Quiz findById(int id) {
        Quiz quiz = null;
        try {
            quiz = entityManager.find(Quiz.class, id);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while getting a quiz: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return quiz;
    }

    public void update(Quiz quiz) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(quiz);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error in updating a quiz: " + e.getMessage());
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }
    }

    public void delete(Quiz quiz) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(quiz);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error in deleting a quiz: " + e.getMessage());
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }
    }

    public Quiz findByUserIdAndSetId(String userId, int setId) {
        Quiz quiz = null;
        try {
            quiz = entityManager.createQuery("SELECT q FROM Quiz q WHERE q.user.userId = :userId AND q.flashcardSet.setId = :setId", Quiz.class)
                    .setParameter("userId", userId)
                    .setParameter("setId", setId)
                    .getSingleResult();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while getting a quiz: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return quiz;
    }

    public List<Quiz> findByUserId(String userId) {
        List<Quiz> quizzes = null;
        try {
            quizzes = entityManager.createQuery("SELECT q FROM Quiz q WHERE q.user.userId = :userId", Quiz.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while getting a quiz: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return quizzes;
    }

    public List<Quiz> findBySetId(int setId) {
        List<Quiz> quizzes = null;
        try {
            quizzes = entityManager.createQuery("SELECT q FROM Quiz q WHERE q.flashcardSet.setId = :setId", Quiz.class)
                    .setParameter("setId", setId)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while getting a quiz: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return quizzes;
    }

}
