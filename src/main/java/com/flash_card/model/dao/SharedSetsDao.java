package com.flash_card.model.dao;

import com.flash_card.model.entity.SharedSet;
import jakarta.persistence.EntityManager;

import java.util.List;

public class SharedSetsDao {
    private static SharedSetsDao instance;
    private EntityManager entityManager;

    private SharedSetsDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public static SharedSetsDao getInstance(EntityManager entityManager) {
        if (instance == null) {
            instance = new SharedSetsDao(entityManager);
        }
        return instance;
    }

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

    public SharedSet findById(int id) {
        return entityManager.find(SharedSet.class, id);
    }

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

    public List<SharedSet> findByUserId(String userId) {
        return entityManager.createQuery("SELECT s FROM SharedSet s WHERE s.user.userId = :userId", SharedSet.class)
                    .setParameter("userId", userId)
                    .getResultList();
    }


    public SharedSet findBySetIdAndUserId(int fsId, String userId) {
        try {
            return entityManager.createQuery("SELECT s FROM SharedSet s WHERE s.flashcardSet.setId = :fsId and s.user.userId = :userId", SharedSet.class)
                    .setParameter("fsId", fsId)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (Exception e) {
                System.err.println("Error in finding a SharedSet: " + e.getMessage());
                e.printStackTrace();
                return null;
        }
    }

}
