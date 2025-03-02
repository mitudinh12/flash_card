package com.flash_card.model.dao;

import com.flash_card.model.entity.Classroom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.RollbackException;

import java.util.List;

public class ClassroomDao {
    private static ClassroomDao classroomDao = null;
    private final EntityManager em;

    private ClassroomDao(EntityManager entityManager) {
        this.em = entityManager;
    }

    public static ClassroomDao getInstance(EntityManager em) {
        if (classroomDao == null) {
            classroomDao = new ClassroomDao(em);
        }
        return classroomDao;
    }

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

    public Classroom findClassById(int id) {
        return em.find(Classroom.class, id);
    }

    public List<Classroom> findAllClassByTeacherId(String teacherId) {
        return em.createQuery("SELECT c FROM Classroom c WHERE c.teacher.id = :teacherId", Classroom.class)
                .setParameter("teacherId", teacherId)
                .getResultList();
    }
}
