package com.flash_card.model.datasource;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for managing the JPA connection to a MariaDB database.
 * This class provides a singleton instance of {@link EntityManager} for database interactions.
 */
public class MariaDbJpaConnection {

    // Singleton instance of the EntityManagerFactory
    private static EntityManagerFactory emf = null;

    // Singleton instance of the EntityManager
    private static EntityManager em = null;

    /**
     * Provides a singleton instance of {@link EntityManager}.
     * Initializes the {@link EntityManagerFactory} and {@link EntityManager} if not already created.
     *
     * @return an {@link EntityManager} instance for interacting with the MariaDB database.
     */
    public static EntityManager getInstance() {
        if (em == null) {
            if (emf == null) {
                // Determine the persistence unit name based on the environment
                String persistenceUnitName = System.getProperty("persistenceUnitName", "FlashcardMariaDbUnit");

                // Check if we are running tests
                boolean isTest = persistenceUnitName.toLowerCase().contains("test");
                Map<String, String> properties = new HashMap<>();
                if (!isTest) {
                    // Fetch database host from environment variable (defaults to localhost)
                    String dbHost = System.getenv("DB_HOST");
                    if (dbHost == null || dbHost.isEmpty()) {
                        dbHost = "localhost";
                    }
                    String jdbcUrl = "jdbc:mariadb://" + dbHost + ":3306/flash_card";
                    properties.put("jakarta.persistence.jdbc.url", jdbcUrl);
                }
                emf = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
            }
            // Create the EntityManager from the EntityManagerFactory
            em = emf.createEntityManager();
        }
        return em;
    }
}
