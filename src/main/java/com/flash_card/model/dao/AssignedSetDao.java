package com.flash_card.model.dao;

import com.flash_card.model.entity.AssignedSet;
import com.flash_card.model.entity.FlashcardSet;
import jakarta.persistence.EntityManager;

import java.util.List;
/**
 * Data Access Object (DAO) class for managing {@link AssignedSet} entities.
 * Provides methods for persisting, deleting, and querying assigned sets in the database.
 */
public final class AssignedSetDao {
    /**
     * Singleton instance of the {@link AssignedSetDao}.
     */
    private static AssignedSetDao assignedSetDao = null;
    /**
     * The {@link EntityManager} used for database operations.
     */
    private final EntityManager em;
    /**
     * Private constructor to initialize the DAO with the provided {@link EntityManager}.
     *
     * @param entityManager the {@link EntityManager} to use for database operations
     */
    private AssignedSetDao(final EntityManager entityManager) {
        this.em = entityManager;
    }
    /**
     * Provides a singleton instance of {@link AssignedSetDao}.
     * If the instance does not exist, it is created with the provided {@link EntityManager}.
     *
     * @param em the {@link EntityManager} to use for database operations
     * @return the singleton instance of {@link AssignedSetDao}
     */
    public static AssignedSetDao getInstance(final EntityManager em) {
        if (assignedSetDao == null) {
            assignedSetDao = new AssignedSetDao(em);
        }
        return assignedSetDao;
    }
    /**
     * Persists an {@link AssignedSet} entity in the database.
     *
     * @param assignedSet the {@link AssignedSet} entity to persist
     * @return true if the operation is successful, false otherwise
     */
    public boolean persistAssignedSet(final AssignedSet assignedSet) {
        try {
            em.getTransaction().begin();
            em.persist(assignedSet);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error in persisting AssignedSet: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Deletes an {@link AssignedSet} entity from the database.
     *
     * @param assignedSet the {@link AssignedSet} entity to delete
     * @return true if the operation is successful, false otherwise
     */
    public boolean deleteAssignedSet(final AssignedSet assignedSet) {
        try {
            em.getTransaction().begin();
            em.remove(assignedSet);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error in deleting AssignedSet: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Finds all {@link FlashcardSet} entities associated with a specific classroom ID.
     *
     * @param classId the ID of the classroom
     * @return a list of {@link FlashcardSet} entities associated with the classroom
     */
    public List<FlashcardSet> findAllSetsByClassId(final int classId) {
        return em.createQuery(
                        "SELECT fs FROM FlashcardSet fs "
                         + "JOIN AssignedSet aset ON fs.setId = aset.flashcardSet.setId "
                         + "WHERE aset.classroom.classroomId = :classId",
                        FlashcardSet.class
                )
                .setParameter("classId", classId)
                .getResultList();
    }
    /**
     * Finds an {@link AssignedSet} entity by its flashcard set ID and classroom ID.
     *
     * @param setId   the ID of the flashcard set
     * @param classId the ID of the classroom
     * @return the {@link AssignedSet} entity if found, or null if not found
     */
    public AssignedSet findBySetIdAndClassId(final int setId, final int classId) {
        List<AssignedSet> results = em.createQuery(
                "SELECT aset FROM AssignedSet aset "
                 + "WHERE aset.flashcardSet.setId = :setId "
                 + "AND aset.classroom.classroomId = :classId",
                        AssignedSet.class)
                .setParameter("setId", setId)
                .setParameter("classId", classId)
                .getResultList();
        if (!results.isEmpty()) {
            return results.getFirst();
        } else {
            return null;
        }

    }
}
