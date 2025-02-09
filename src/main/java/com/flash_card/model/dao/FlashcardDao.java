package com.flash_card.model.dao;

import com.flash_card.model.datasource.MariaDbJpaConnection;
import com.flash_card.model.entity.Flashcard;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

import java.util.List;

public class FlashcardDao {
    private static FlashcardDao instance;
    private EntityManager entityManager;

    private FlashcardDao(EntityManager entityManager) {
        entityManager = MariaDbJpaConnection.getInstance();
    }

    public static FlashcardDao getInstance(EntityManager entityManager) {
        if (instance == null) {
            instance = new FlashcardDao(entityManager);
        }
        return instance;
    }

    public void persist(Flashcard flashcard) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(flashcard);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error in persisting Flashcard: " + e.getMessage());
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    public Flashcard findById(int id) {
        Flashcard flashcard = null;
        try {
            flashcard = entityManager.find(Flashcard.class, id);
        } catch (PersistenceException e) {
            System.err.println("Database connection error " + e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            System.err.println("Table flashcard does not exist or query is incorrect: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("An unexpected error occured while getting a flashcard:" + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return flashcard;
    }

    public void update(Flashcard flashcard) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(flashcard);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error in updating a flashcard: " + e.getMessage());
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    public void delete(Flashcard flashcard) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(flashcard);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error in deleting a flashcard: " + e.getMessage());
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    public List<Flashcard> findByUserId(int userId) {
        List<Flashcard> flashcards = null;
        try {
            flashcards = entityManager.createQuery("SELECT f FROM Flashcard f WHERE f.flashcardCreator.id = :userId", Flashcard.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (PersistenceException e) {
            System.err.println("Database connection error: " + e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            System.err.println("Table flashcard does not exist or query is incorrect: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while getting flashcards by user ID: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return flashcards;
    }

    public List<Flashcard> findBySetId(int setId) {
        List<Flashcard> flashcards = null;
        try {
            flashcards = entityManager.createQuery("SELECT f FROM Flashcard f WHERE f.flashcardSet.setId = :setId", Flashcard.class)
                    .setParameter("setId", setId)
                    .getResultList();
        } catch (PersistenceException e) {
            System.err.println("Database connection error: " + e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            System.err.println("Table flashcard does not exist or query is incorrect: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while getting flashcards by set ID: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return flashcards;
    }
}