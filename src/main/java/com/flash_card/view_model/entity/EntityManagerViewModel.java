package com.flash_card.view_model.entity;

import com.flash_card.model.datasource.MariaDbJpaConnection;
import jakarta.persistence.EntityManager;

public class EntityManagerViewModel {
    private EntityManager entityManager;

    public static EntityManager getEntityManager() {
        return MariaDbJpaConnection.getInstance();
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
