package com.flash_card.model.dao;

import com.flash_card.model.entity.CardDifficultLevel;
import jakarta.persistence.EntityManager;

import java.util.List;

public class CardDifficultLevelDao {
    private static CardDifficultLevelDao cardDifficultLevelDao = null;
    private final EntityManager em;

    private CardDifficultLevelDao(EntityManager entityManager) {
        this.em = entityManager;
    }

    public static CardDifficultLevelDao getInstance(EntityManager em) {
        if (cardDifficultLevelDao == null) {
            cardDifficultLevelDao = new CardDifficultLevelDao(em);
        }
        return cardDifficultLevelDao;
    }

    public boolean persistCardDifficultLevel(CardDifficultLevel cardDifficultLevel) {
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

    public boolean deleteCardDifficultLevel(CardDifficultLevel cardDifficultLevel) {
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

    public List<CardDifficultLevel> findAllCardDifficultLevelByStudyId(int studyId) {
        return em.createQuery("SELECT cdl FROM CardDifficultLevel cdl WHERE cdl.study.studyId = :studyId", CardDifficultLevel.class)
                .setParameter("studyId", studyId)
                .getResultList();
    }

    public CardDifficultLevel findCardDifficultLevelByCardId(int cardId) {
        CardDifficultLevel level = null;
        List<CardDifficultLevel> results = em.createQuery("SELECT cdl FROM CardDifficultLevel cdl WHERE cdl.flashcard.cardId = :cardId", CardDifficultLevel.class)
                .setParameter("cardId", cardId)
                .getResultList();
        if (!results.isEmpty()){
            level = results.getFirst();
        }
        return level;
    }
}
