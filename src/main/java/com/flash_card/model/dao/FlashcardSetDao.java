package com.flash_card.model.dao;

import com.flash_card.model.datasource.MariaDbJpaConnection;
import com.flash_card.model.entity.FlashcardSet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

import java.util.ArrayList;

public class FlashcardSetDao {
    static EntityManager entityManager = MariaDbJpaConnection.getInstance();

    public static void persist(FlashcardSet flashcardSet) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(flashcardSet);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error in persisting FlashcardSet: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static FlashcardSet findById(int id) {
        FlashcardSet flashcardSet = null;
        try {
            entityManager.find(FlashcardSet.class, id);
        } catch (PersistenceException e) {
            System.err.println("Database connection error " + e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            System.err.println("Table flashcard set does not exist or query is incorrect: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("An unexpected error occured while getting a flashcard set:" + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return flashcardSet;
    }

    public static void update(FlashcardSet flashcardSet) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(flashcardSet);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error in updating FlashcardSet: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void delete(FlashcardSet flashcardSet) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(flashcardSet);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error in deleting FlashcardSet: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static ArrayList<FlashcardSet> findAllSets() {
        ArrayList<FlashcardSet> flashcardSets = new ArrayList<>();
        try {
            flashcardSets = (ArrayList<FlashcardSet>) entityManager.createQuery("SELECT fs FROM FlashcardSet fs").getResultList();
        } catch (PersistenceException e) {
            System.err.println("Database connection error " + e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            System.err.println("Table flashcard set does not exist or query is incorrect: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("An unexpected error occured while getting all flashcard sets:" + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return flashcardSets;
    }


}
