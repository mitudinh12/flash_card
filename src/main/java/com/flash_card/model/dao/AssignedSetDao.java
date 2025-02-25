package com.flash_card.model.dao;

import com.flash_card.model.entity.AssignedSet;
import com.flash_card.model.entity.FlashcardSet;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AssignedSetDao {
    private static AssignedSetDao assignedSetDao = null;
    private final EntityManager em;

    private AssignedSetDao(EntityManager entityManager) {
        this.em = entityManager;
    }

    public static AssignedSetDao getInstance(EntityManager em) {
        if (assignedSetDao == null) {
            assignedSetDao = new AssignedSetDao(em);
        }
        return assignedSetDao;
    }

    public boolean persistAssignedSet(AssignedSet assignedSet) {
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

    public boolean deleteAssignedSet(AssignedSet assignedSet) {
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

    public List<FlashcardSet> findAllSetsByClassId(int classId) {
        return em.createQuery("SELECT fs FROM FlashcardSet fs JOIN AssignedSet aset ON fs.setId = aset.flashcardSet.setId WHERE aset.classroom.classroomId = :classId", FlashcardSet.class)
                .setParameter("classId", classId)
                .getResultList();
    }

    public AssignedSet findBySetIdAndClassId(int setId, int classId) {

        List<AssignedSet> results = em.createQuery("SELECT aset FROM AssignedSet aset WHERE aset.flashcardSet.setId = :setId AND aset.classroom.classroomId = :classId", AssignedSet.class)
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
