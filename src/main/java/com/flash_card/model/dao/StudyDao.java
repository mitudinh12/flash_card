package com.flash_card.model.dao;

import com.flash_card.model.entity.Study;
import jakarta.persistence.EntityManager;
import java.util.List;

public class StudyDao {
    private static StudyDao instance;
    private EntityManager entityManager;

    private StudyDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public static StudyDao getInstance(EntityManager entityManager) {
        if (instance == null) {
            instance = new StudyDao(entityManager);
        }
        return instance;
    }

    public void persist(Study study) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(study);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error in persisting Study: " + e.getMessage());
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    public Study findById(int id) {
        Study study = null;
        try {
            study = entityManager.find(Study.class, id);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while getting a study: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return study;
    }

    public void update(Study study) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(study);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error in updating a study: " + e.getMessage());
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }
    }

    public void delete(Study study) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(study);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error in deleting a study: " + e.getMessage());
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }
    }

    public Study findByUserIdAndSetId(String userId, int setId) {
        try {
            return entityManager.createQuery("SELECT s FROM Study s WHERE s.user.userId = :userId AND s.flashcardSet.setId = :setId", Study.class)
                    .setParameter("userId", userId)
                    .setParameter("setId", setId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}