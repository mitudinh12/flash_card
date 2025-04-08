package com.flash_card.model.dao;

import com.flash_card.model.entity.Classroom;
import jakarta.persistence.EntityManager;

import java.util.List;
/**
 * Data Access Object (DAO) class for managing {@link Classroom} entities.
 * Provides methods for persisting, deleting, updating, and querying classrooms
 * in the database.
 */
public class ClassroomDao {
    /**
     * Singleton instance of the {@link ClassroomDao}.
     */
    private static ClassroomDao classroomDao = null;
    /**
     * The {@link EntityManager} used for database operations.
     */
    private final EntityManager em;
    /**
     * Private constructor to initialize the DAO with the provided
     * {@link EntityManager}.
     *
     * @param entityManager the {@link EntityManager} to use for database
     *                      operations
     */
    private ClassroomDao(EntityManager entityManager) {
        this.em = entityManager;
    }
    /**
     * Provides a singleton instance of {@link ClassroomDao}.
     * If the instance does not exist, it is created with the provided
     * {@link EntityManager}.
     *
     * @param em the {@link EntityManager} to use for database operations
     * @return the singleton instance of {@link ClassroomDao}
     */
    public static ClassroomDao getInstance(EntityManager em) {
        if (classroomDao == null) {
            classroomDao = new ClassroomDao(em);
        }
        return classroomDao;
    }
    /**
     * Persists a {@link Classroom} entity in the database.
     *
     * @param classroom the {@link Classroom} entity to persist
     * @return true if the operation is successful, false otherwise
     */
    public boolean persistClass(Classroom classroom) {

        try {
            em.getTransaction().begin();
            em.persist(classroom);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error in persisting Classroom: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Deletes a {@link Classroom} entity from the database.
     *
     * @param classroom the {@link Classroom} entity to delete
     * @return true if the operation is successful, false otherwise
     */
    public boolean deleteClass(Classroom classroom) {
        try {
            em.getTransaction().begin();
            em.remove(classroom);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error in deleting Classroom: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Updates a {@link Classroom} entity in the database.
     *
     * @param classroom the {@link Classroom} entity to update
     * @return true if the operation is successful, false otherwise
     */
    public boolean updateClass(Classroom classroom) {
        try {
            em.getTransaction().begin();
            em.merge(classroom);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error in updating Classroom: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Finds a {@link Classroom} entity by its ID.
     *
     * @param id the ID of the classroom
     * @return the {@link Classroom} entity if found, or null if not found
     */
    public Classroom findClassById(int id) {
        return em.find(Classroom.class, id);
    }
    /**
     * Finds all {@link Classroom} entities associated with a specific teacher ID.
     *
     * @param teacherId the ID of the teacher
     * @return a list of {@link Classroom} entities associated with the teacher
     */
    public List<Classroom> findAllClassByTeacherId(String teacherId) {
        return em.createQuery("SELECT c FROM Classroom c WHERE c.teacher.id = :teacherId", Classroom.class)
                .setParameter("teacherId", teacherId)
                .getResultList();
    }
}
