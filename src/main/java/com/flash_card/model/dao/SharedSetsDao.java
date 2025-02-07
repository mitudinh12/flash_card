package com.flash_card.model.dao;
import com.flash_card.model.datasource.MariaDbJpaConnection;
import com.flash_card.model.entity.SharedSet;
import jakarta.persistence.EntityManager;

import java.util.List;

public class SharedSetsDao {
    private static SharedSetsDao instance;
    private EntityManager entityManager;

    private SharedSetsDao() {
        entityManager = MariaDbJpaConnection.getInstance();
    }

    public static SharedSetsDao getInstance() {
        if (instance == null) {
            instance = new SharedSetsDao();
        }
        return instance;
    }

    public void persist(SharedSet sharedSet) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(sharedSet);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error in persisting a SharedSet: " + e.getMessage());
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    public SharedSet findById(int id) {
        SharedSet sharedSet = null;
        try {
            sharedSet = entityManager.find(SharedSet.class, id);
        } catch (Exception e) {
            System.err.println("An unexpected error occured while getting a shared set:" + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return sharedSet;
    }

    public void update(SharedSet sharedSet) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(sharedSet);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error in updating a SharedSet: " + e.getMessage());
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    public void delete(SharedSet sharedSet) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(sharedSet);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error in deleting a SharedSet: " + e.getMessage());
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    public List<SharedSet> findByUserId(String userId) {
        List<SharedSet> sharedSets = null;
        try {
            sharedSets = entityManager.createQuery("SELECT s FROM SharedSet s WHERE s.user.userId = :userId", SharedSet.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("An unexpected error occured while getting shared sets by user id:" + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return sharedSets;
    }


    public SharedSet findByFlashcardSetId(int fsId) {
        SharedSet sharedSet = null;
        try {
            sharedSet = entityManager.createQuery("SELECT s FROM SharedSet s WHERE s.flashcardSet.setId = :fsId", SharedSet.class)
                    .setParameter("fsId", fsId)
                    .getSingleResult();
        } catch (Exception e) {
            System.err.println("An unexpected error occured while getting a shared set:" + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return sharedSet;
    }

}
