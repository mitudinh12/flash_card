package com.flash_card.model.dao;

import com.flash_card.model.datasource.MariaDbJpaConnection;
import com.flash_card.model.entity.Flashcard;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

import java.util.ArrayList;
import java.util.List;

public class FlashcardDao {
    static EntityManager entityManager = MariaDbJpaConnection.getInstance();

    public static void persist(Flashcard flashcard) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(flashcard);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error in persisting Flashcard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Flashcard findById(int id) {
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

    public static void update(Flashcard flashcard) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(flashcard);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error in updating a flashcard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void delete(Flashcard flashcard) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(flashcard);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error in deleting a flashcard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static ArrayList<Flashcard> findAllSets() {
        ArrayList<Flashcard> flashcards = new ArrayList<>();
        try {
            List<Flashcard> resultList = entityManager.createQuery("SELECT f FROM Flashcard f", Flashcard.class).getResultList();
            flashcards.addAll(resultList);
        } catch (PersistenceException e) {
            System.err.println("Database connection error " + e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            System.err.println("Table flashcard does not exist or query is incorrect: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("An unexpected error occured while getting all flashcards:" + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return flashcards;
    }


}
