package com.flash_card.model.dao;

import com.flash_card.model.datasource.MariaDbJpaConnection;
import com.flash_card.model.entity.FlashcardSet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

public class FlashcardSetDao {
    private static FlashcardSetDao instance;
    private EntityManager entityManager;

    private FlashcardSetDao() {
        entityManager = MariaDbJpaConnection.getInstance();
    }

    public static FlashcardSetDao getInstance() {
        if (instance == null) {
            instance = new FlashcardSetDao();
        }
        return instance;
    }

    public FlashcardSet findById(int id) {
        FlashcardSet flashcardSet = null;
        try {
            flashcardSet = entityManager.find(FlashcardSet.class, id);
        } catch (PersistenceException e) {
            System.err.println("Database connection error: " + e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            System.err.println("Table flashcard_set does not exist or query is incorrect: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while getting a flashcard set: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return flashcardSet;
    }
}