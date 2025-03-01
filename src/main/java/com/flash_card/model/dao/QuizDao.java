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

    public Quiz findById(int id) {
        Quiz quiz = entityManager.find(Quiz.class, id);
        return quiz;
    }

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

    public List<Quiz> findByUserIdAndSetId(String userId, int setId) {
        try {
            return entityManager.createQuery("SELECT q FROM Quiz q WHERE q.user.userId = :userId AND q.flashcardSet.setId = :setId", Quiz.class)
                    .setParameter("userId", userId)
                    .setParameter("setId", setId)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}