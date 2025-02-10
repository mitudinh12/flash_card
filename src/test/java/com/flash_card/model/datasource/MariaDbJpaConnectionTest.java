package com.flash_card.model.datasource;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MariaDbJpaConnectionTest {

    @BeforeAll
    public static void setUp() {
        // Set the system property to use the test persistence unit
        System.setProperty("persistenceUnitName", "test-persistence-unit");
    }

    @Test
    public void testGetInstance() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        assertNotNull(em, "EntityManager should not be null");
    }

    @Test
    public void testEntityManagerFactorySingleton() {
        EntityManager em1 = MariaDbJpaConnection.getInstance();
        EntityManager em2 = MariaDbJpaConnection.getInstance();
        assertSame(em1, em2, "EntityManager instances should be the same");
    }

    @Test
    public void testPersistenceUnitName() {
        System.setProperty("persistenceUnitName", "test-persistence-unit");
        EntityManager em = MariaDbJpaConnection.getInstance();
        assertNotNull(em, "EntityManager should not be null");
    }
}