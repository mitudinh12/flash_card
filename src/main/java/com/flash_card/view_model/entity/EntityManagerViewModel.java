package com.flash_card.view_model.entity;

import com.flash_card.model.datasource.MariaDbJpaConnection;
import jakarta.persistence.EntityManager;
/**
 * ViewModel class for managing the {@link EntityManager} instance.
 * Provides methods to retrieve and set the {@link EntityManager} used
 * for database operations.
 */
public class EntityManagerViewModel {
    /**
     * The {@link EntityManager} instance used for database operations.
     */
    private EntityManager entityManager;
    /**
     * Retrieves the singleton {@link EntityManager} instance from
     * {@link MariaDbJpaConnection}.
     *
     * @return the singleton {@link EntityManager} instance
     */
    public static EntityManager getEntityManager() {
        return MariaDbJpaConnection.getInstance();
    }
    /**
     * Sets the {@link EntityManager} instance for this ViewModel.
     *
     * @param entityManagerParam the {@link EntityManager} instance to set
     */
    public void setEntityManager(final EntityManager entityManagerParam) {
        this.entityManager = entityManagerParam;
    }
}
