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

    public boolean persist(Study study) {
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

    public Study findById(int id) {
        Study study = entityManager.find(Study.class, id);
        return study;
    }

    public boolean update(Study study) {
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

    public boolean delete(Study study) {
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