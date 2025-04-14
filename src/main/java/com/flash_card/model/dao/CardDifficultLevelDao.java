package com.flash_card.model.dao;

import com.flash_card.model.entity.CardDifficultLevel;
import jakarta.persistence.EntityManager;

import java.util.List;
/**
 * Data Access Object (DAO) class for managing {@link CardDifficultLevel} entities.
 * Provides methods for persisting, deleting, and querying card difficulty levels in the database.
 */
public final class CardDifficultLevelDao {
    /**
     * Singleton instance of the {@link CardDifficultLevelDao}.
     */
    private static CardDifficultLevelDao cardDifficultLevelDao = null;
    /**
     * The {@link EntityManager} used for database operations.
     */
    private final EntityManager em;
    /**
     * Private constructor to initialize the DAO with the provided {@link EntityManager}.
     *
     * @param entityManager the {@link EntityManager} to use for database operations
     */
    private CardDifficultLevelDao(final EntityManager entityManager) {
        this.em = entityManager;
    }
    /**
     * Provides a singleton instance of {@link CardDifficultLevelDao}.
     * If the instance does not exist, it is created with the provided {@link EntityManager}.
     *
     * @param em the {@link EntityManager} to use for database operations
     * @return the singleton instance of {@link CardDifficultLevelDao}
     */
    public static CardDifficultLevelDao getInstance(final EntityManager em) {
        if (cardDifficultLevelDao == null) {
            cardDifficultLevelDao = new CardDifficultLevelDao(em);
        }
        return cardDifficultLevelDao;
    }
    /**
     * Persists a {@link CardDifficultLevel} entity in the database.
     *
     * @param cardDifficultLevel the {@link CardDifficultLevel} entity to persist
     * @return true if the operation is successful, false otherwise
     */
    public boolean persistCardDifficultLevel(final CardDifficultLevel cardDifficultLevel) {
        try {
            em.getTransaction().begin();
            em.persist(cardDifficultLevel);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error in persisting CardDifficultLevel: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Deletes a {@link CardDifficultLevel} entity from the database.
     *
     * @param cardDifficultLevel the {@link CardDifficultLevel} entity to delete
     * @return true if the operation is successful, false otherwise
     */
    public boolean deleteCardDifficultLevel(final CardDifficultLevel cardDifficultLevel) {
        try {
            em.getTransaction().begin();
            em.remove(cardDifficultLevel);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error in deleting CardDifficultLevel: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Finds a {@link CardDifficultLevel} entity by its flashcard ID and study ID.
     *
     * @param cardId  the ID of the flashcard
     * @param studyId the ID of the study
     * @return the {@link CardDifficultLevel} entity if found, or null if not found
     */
    public CardDifficultLevel findCardDifficultLevelByCardIdAndStudyId(final int cardId, final int studyId) {
        CardDifficultLevel level = null;
        List<CardDifficultLevel> results = em.createQuery(
                    "SELECT cdl FROM CardDifficultLevel cdl "
                    + "WHERE cdl.flashcard.cardId = :cardId "
                    + "AND cdl.study.studyId = :studyId",
                    CardDifficultLevel.class
                )
                .setParameter("cardId", cardId)
                .setParameter("studyId", studyId)
                .getResultList();
        if (!results.isEmpty()) {
            level = results.getFirst();
        }
        return level;
    }
}
