package com.flash_card.model.dao;

import com.flash_card.model.entity.ClassMember;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ClassMemberDao {
    private static ClassMemberDao classMemberDao = null;
    private EntityManager em;

    private ClassMemberDao(EntityManager entityManager) {
        this.em = entityManager;
    }

    public static ClassMemberDao getInstance(EntityManager em) {
        if (classMemberDao == null) {
            classMemberDao = new ClassMemberDao(em);
        }
        return classMemberDao;
    }

    public void persistClassMember(ClassMember classMember) {
        try {
            em.getTransaction().begin();
            em.persist(classMember);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error in persisting ClassMember: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteClassMember(ClassMember classMember) {
        try {
            em.getTransaction().begin();
            em.remove(classMember);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error in deleting ClassMember: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<User> findAllStudentByClassId(int classId) {
        List<User> students = null;
        try {
            students = em.createQuery("SELECT u FROM User u JOIN ClassMember cm ON u.userId = cm.student.userId WHERE cm.classroom.classroomId = :classId", User.class)
                    .setParameter("classId", classId)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("Error in finding all students by class id: " + e.getMessage());
            e.printStackTrace();
        }
        return students;
    }

    public ClassMember findClassMemberById(int id) {
        ClassMember classMember = null;
        try {
            classMember = em.find(ClassMember.class, id);
        } catch (Exception e) {
            System.err.println("Error in finding class member by id: " + e.getMessage());
            e.printStackTrace();
        }
        return classMember;
    }
}
