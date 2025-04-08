package com.flash_card.model.dao;

import com.flash_card.model.entity.ClassMember;
import com.flash_card.model.entity.Classroom;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;

import java.util.List;
/**
 * Data Access Object (DAO) class for managing {@link ClassMember} entities.
 * Provides methods for persisting, deleting, and querying class members in the database.
 */
public class ClassMemberDao {
    /**
     * Singleton instance of the {@link ClassMemberDao}.
     */
    private static ClassMemberDao classMemberDao = null;
    /**
     * The {@link EntityManager} used for database operations.
     */
    private final EntityManager em;
    /**
     * Private constructor to initialize the DAO with the provided {@link EntityManager}.
     *
     * @param entityManager the {@link EntityManager} to use for database operations
     */
    private ClassMemberDao(EntityManager entityManager) {
        this.em = entityManager;
    }
    /**
     * Provides a singleton instance of {@link ClassMemberDao}.
     * If the instance does not exist, it is created with the provided {@link EntityManager}.
     *
     * @param em the {@link EntityManager} to use for database operations
     * @return the singleton instance of {@link ClassMemberDao}
     */
    public static ClassMemberDao getInstance(EntityManager em) {
        if (classMemberDao == null) {
            classMemberDao = new ClassMemberDao(em);
        }
        return classMemberDao;
    }
    /**
     * Persists a {@link ClassMember} entity in the database.
     *
     * @param classMember the {@link ClassMember} entity to persist
     * @return true if the operation is successful, false otherwise
     */
    public boolean persistClassMember(ClassMember classMember) {
        try {
            em.getTransaction().begin();
            em.persist(classMember);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error in persisting ClassMember: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Deletes a {@link ClassMember} entity from the database.
     *
     * @param classMember the {@link ClassMember} entity to delete
     * @return true if the operation is successful, false otherwise
     */
    public boolean deleteClassMember(ClassMember classMember) {
        try {
            em.getTransaction().begin();
            em.remove(classMember);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error in deleting ClassMember: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Finds all {@link User} entities associated with a specific classroom ID.
     *
     * @param classId the ID of the classroom
     * @return a list of {@link User} entities associated with the classroom
     */
    public List<User> findAllStudentByClassId(int classId) {
        return em.createQuery(
                        "SELECT u FROM User u "
                         + "JOIN ClassMember cm ON u.userId = cm.student.userId "
                         + "WHERE cm.classroom.classroomId = :classId",
                        User.class
                )
                .setParameter("classId", classId)
                .getResultList();

    }
    /**
     * Finds a {@link ClassMember} entity by its student ID and classroom ID.
     *
     * @param classId   the ID of the classroom
     * @param studentId the ID of the student
     * @return the {@link ClassMember} entity if found, or null if not found
     */
    public ClassMember findByStudentIdAndClassId(int classId, String studentId) {
        ClassMember classMember = null;
        List<ClassMember> resultList = em.createQuery(
                        "SELECT cm FROM ClassMember cm "
                         + "WHERE cm.student.userId = :studentId "
                         + "AND cm.classroom.classroomId = :classId",
                        ClassMember.class
                )
                .setParameter("studentId", studentId)
                .setParameter("classId", classId)
                .getResultList();
        if (!resultList.isEmpty()) {
            classMember = resultList.getFirst();
        }
        return classMember;
    }
    /**
     * Finds all {@link Classroom} entities associated with a specific student ID.
     *
     * @param userId the ID of the student
     * @return a list of {@link Classroom} entities associated with the student
     */
    public List<Classroom> findAllClassesByStudentId(String userId) {
        return em.createQuery(
                        "SELECT cm.classroom FROM ClassMember cm "
                         + "WHERE cm.student.userId = :userId",
                        Classroom.class
                )
                .setParameter("userId", userId)
                .getResultList();
    }
}
