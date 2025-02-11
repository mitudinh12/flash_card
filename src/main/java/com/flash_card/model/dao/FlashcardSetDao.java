package com.flash_card.model.dao;
import com.flash_card.model.entity.FlashcardSet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import java.util.List;

public class FlashcardSetDao {
    private static FlashcardSetDao instance;
    private EntityManager entityManager;

    private FlashcardSetDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public static FlashcardSetDao getInstance(EntityManager entityManager) {
        if (instance == null) {
            instance = new FlashcardSetDao(entityManager);
        }
        return instance;
    }

    public void persist(FlashcardSet flashcardSet) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(flashcardSet);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("Error in persisting FlashcardSet: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public FlashcardSet findById(int id) {
        FlashcardSet flashcardSet = null;
        try {
            flashcardSet = entityManager.find(FlashcardSet.class, id);
        } catch (Exception e) {
            System.err.println("An unexpected error occured while getting a flashcard set:" + e.getMessage());
            e.printStackTrace();
        }
        return flashcardSet;
    }

    public void update(FlashcardSet flashcardSet) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(flashcardSet);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error in updating FlashcardSet: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void delete(FlashcardSet flashcardSet) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(flashcardSet);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error in deleting FlashcardSet: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public List<FlashcardSet> findByUserId(String userId) {
        List<FlashcardSet> flashcardSets = null;
        try {
            flashcardSets = entityManager.createQuery("SELECT fs FROM FlashcardSet fs WHERE fs.flashcardCreator.userId = :userId", FlashcardSet.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while getting flashcards by user ID: " + e.getMessage());
            e.printStackTrace();
        }

        return flashcardSets;
    }


}