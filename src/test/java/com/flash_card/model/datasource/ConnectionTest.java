package com.flash_card.model.datasource;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConnectionTest {
    @Test
    public void testGetInstance() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        assertNotNull(em, "EntityManager should not be null");
    }
}
