package com.flash_card.model.dao;

import com.flash_card.model.datasource.MariaDbJpaConnection;
import com.flash_card.model.entity.Flashcard;
import com.flash_card.model.entity.FlashcardSet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

import java.util.ArrayList;
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

    public ArrayList<FlashcardSet> findAllSets() {
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

    public List<FlashcardSet> findAllSetsByCreatorId(String userId) {
        List<FlashcardSet> flashcardSetList = new ArrayList<>();
        try {
             flashcardSetList = entityManager.createQuery(
                    "SELECT f FROM FlashcardSet f WHERE f.flashcardCreator.userId = :userId",
                    FlashcardSet.class
            ).setParameter("userId", userId).getResultList();
            System.out.println(flashcardSetList);
            return flashcardSetList;
        } catch (Exception e) {
            e.printStackTrace();
            return flashcardSetList;
        }
    }
    public List<FlashcardSet> findThreeNewestSets() {
        List<FlashcardSet> flashcardSets = new ArrayList<>();
        try {
            flashcardSets = entityManager.createQuery("SELECT fs FROM FlashcardSet fs ORDER BY fs.id DESC", FlashcardSet.class)
                    .setMaxResults(6)
                    .getResultList();
        } catch (PersistenceException e) {
            System.err.println("Database connection error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while getting the newest flashcard sets: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return flashcardSets;
    }

    public List<FlashcardSet> findByUserId(String userId) {
        List<FlashcardSet> flashcardSets = null;
        try {
            flashcardSets = entityManager.createQuery("SELECT fs FROM FlashcardSet fs WHERE fs.flashcardCreator.userId = :userId", FlashcardSet.class)
                    .setParameter("userId", userId)
                    .getResultList();
        }catch (PersistenceException e) {
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

        return flashcardSets;
    }

}