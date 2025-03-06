package com.flash_card.model.dao;

import com.flash_card.framework.DifficultyLevel;
import com.flash_card.model.datasource.MariaDbJpaConnection;
import com.flash_card.model.entity.Flashcard;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

import java.util.List;

public class FlashcardDao {
    private static FlashcardDao instance;
    private EntityManager entityManager;

    private FlashcardDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public static FlashcardDao getInstance(EntityManager entityManager) {
        if (instance == null) {
            instance = new FlashcardDao(entityManager);
        }
        return instance;
    }

    public boolean persist(Flashcard flashcard) {
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

    public Flashcard findById(int id) {
        return entityManager.find(Flashcard.class, id);

    }

    public boolean update(Flashcard flashcard) {
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

    public boolean delete(Flashcard flashcard) {
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


    public List<Flashcard> findBySetId(int setId) {
        return entityManager.createQuery("SELECT f FROM Flashcard f WHERE f.flashcardSet.setId = :setId", Flashcard.class)
                    .setParameter("setId", setId)
                    .getResultList();

    }

    public List<Flashcard> getHardFlashcards(int setId, int studyId) {
        return entityManager.createQuery(
                        "SELECT f FROM Flashcard f JOIN CardDifficultLevel cdl ON f.cardId = cdl.flashcard.cardId " +
                                "WHERE f.flashcardSet.setId = :setId AND cdl.study.studyId = :studyId AND cdl.difficultLevel = :difficultyLevel",
                        Flashcard.class)
                .setParameter("setId", setId)
                .setParameter("studyId", studyId)
                .setParameter("difficultyLevel", DifficultyLevel.hard)
                .getResultList();
    }
}