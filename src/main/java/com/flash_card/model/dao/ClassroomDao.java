package com.flash_card.model.dao;

import com.flash_card.model.entity.Classroom;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ClassroomDao {
    private static ClassroomDao classroomDao = null;
    private EntityManager em;

    private ClassroomDao(EntityManager entityManager) {
        this.em = entityManager;
    }

    public static ClassroomDao getInstance(EntityManager em) {
        if (classroomDao == null) {
            classroomDao = new ClassroomDao(em);
        }
        return classroomDao;
    }

    public void persistClass(Classroom classroom) {
        try {
            em.getTransaction().begin();
            em.persist(classroom);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error in persisting Classroom: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteClass(Classroom classroom) {
        try {
            em.getTransaction().begin();
            em.remove(classroom);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error in deleting Classroom: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateClass(Classroom classroom) {
        try {
            em.getTransaction().begin();
            em.merge(classroom);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error in updating Classroom: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Classroom findClassById(int id) {
        Classroom classroom = null;
        try {
            classroom = em.find(Classroom.class, id);
        } catch (Exception e) {
            System.err.println("An unexpected error occured while getting a classroom:" + e.getMessage());
            e.printStackTrace();
        }
        return classroom;
    }

    public List<Classroom> findAllClassByTeacherId(String teacherId) {
        List<Classroom> classrooms = null;
        try {
            classrooms = em.createQuery("SELECT c FROM Classroom c WHERE c.teacher.id = :teacherId", Classroom.class)
                    .setParameter("teacherId", teacherId)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("An unexpected error occured while getting a classroom:" + e.getMessage());
            e.printStackTrace();
        }
        return classrooms;
    }
}
