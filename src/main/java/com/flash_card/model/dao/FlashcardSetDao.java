package com.flash_card.model.dao;
import com.flash_card.model.entity.FlashcardSet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import java.util.List;

public class FlashcardSetDao {
    private static FlashcardSetDao instance;
    private EntityManager entityManager;

    FlashcardSetDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public static FlashcardSetDao getInstance(EntityManager entityManager) {
        if (instance == null) {
            instance = new FlashcardSetDao(entityManager);
        }
        return instance;
    }

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

    public FlashcardSet findById(int id) {
        return entityManager.find(FlashcardSet.class, id);
    }

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


    public List<FlashcardSet> findByUserId(String userId) {
        return entityManager.createQuery("SELECT fs FROM FlashcardSet fs WHERE fs.flashcardCreator.userId = :userId", FlashcardSet.class)
                .setParameter("userId", userId)
                .getResultList();

    }


}