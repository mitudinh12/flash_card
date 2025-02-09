package com.flash_card.model.entity;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Ensures @BeforeAll runs only once
public abstract class TestSetupAbstract {
    protected static EntityManagerFactory entityManagerFactory;

    @BeforeAll
    public void setUpDatabase() {
        System.out.println("Initializing EntityManagerFactory...");
        entityManagerFactory = Persistence.createEntityManagerFactory("test-persistence-unit");
    }

    @AfterAll
    public void tearDownDatabase() {
        System.out.println("Closing EntityManagerFactory...");
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
