package com.flash_card.model.dao;

import com.flash_card.model.entity.ClassMember;
import com.flash_card.model.entity.Classroom;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ClassMemberDao {
    private static ClassMemberDao classMemberDao = null;
    private final EntityManager em;

    private ClassMemberDao(EntityManager entityManager) {
        this.em = entityManager;
    }

    public static ClassMemberDao getInstance(EntityManager em) {
        if (classMemberDao == null) {
            classMemberDao = new ClassMemberDao(em);
        }
        return classMemberDao;
    }

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

    public List<User> findAllStudentByClassId(int classId) {
        return em.createQuery("SELECT u FROM User u JOIN ClassMember cm ON u.userId = cm.student.userId WHERE cm.classroom.classroomId = :classId", User.class)
                .setParameter("classId", classId)
                .getResultList();

    }

    public ClassMember findByStudentIdAndClassId(int classId, String studentId) {
        ClassMember classMember = null;
        List<ClassMember> resultList = em.createQuery("SELECT cm FROM ClassMember cm WHERE cm.student.userId = :studentId and cm.classroom.classroomId = :classId", ClassMember.class)
                .setParameter("studentId", studentId)
                .setParameter("classId", classId)
                .getResultList();
        if (!resultList.isEmpty()) {
            classMember = resultList.getFirst();
        }
        return classMember;
    }

    public List<Classroom> findAllClassesByStudentId(String userId) {
        return em.createQuery("SELECT cm.classroom FROM ClassMember cm WHERE cm.student.userId = :userId", Classroom.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
