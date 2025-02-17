package com.flash_card.model.dao;

import com.flash_card.model.entity.AssignedSet;
import com.flash_card.model.entity.FlashcardSet;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AssignedSetDao {
    private static AssignedSetDao assignedSetDao = null;
    private EntityManager em;

    private AssignedSetDao(EntityManager entityManager) {
        this.em = entityManager;
    }

    public static AssignedSetDao getInstance(EntityManager em) {
        if (assignedSetDao == null) {
            assignedSetDao = new AssignedSetDao(em);
        }
        return assignedSetDao;
    }

    public void persistAssignedSet(AssignedSet assignedSet) {
        try {
            em.getTransaction().begin();
            em.persist(assignedSet);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error in persisting AssignedSet: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteAssignedSet(AssignedSet assignedSet) {
        try {
            em.getTransaction().begin();
            em.remove(assignedSet);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error in deleting AssignedSet: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<FlashcardSet> findAllSetsByClassId(int classId) {
        List<FlashcardSet> sets = null;
        try {
            sets = em.createQuery("SELECT fs FROM FlashcardSet fs JOIN AssignedSet aset ON fs.setId = aset.flashcardSet.setId WHERE aset.classroom.classroomId = :classId", FlashcardSet.class)
                    .setParameter("classId", classId)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("Error in finding all sets by class id: " + e.getMessage());
            e.printStackTrace();
        }
        return sets;
    }

    public AssignedSet findBySetIdAndClassId(int setId, int classId) {
        AssignedSet assignedSet = null;
        try {
            List<AssignedSet> results = em.createQuery("SELECT aset FROM AssignedSet aset WHERE aset.flashcardSet.setId = :setId AND aset.classroom.classroomId = :classId", AssignedSet.class)
                    .setParameter("setId", setId)
                    .setParameter("classId", classId)
                    .getResultList();
            if (!results.isEmpty()) {
                assignedSet = results.getFirst();
            }
        } catch (Exception e) {
            System.err.println("Error in finding assigned set: " + e.getMessage());
            e.printStackTrace();
        }
        return assignedSet;
    }
}
