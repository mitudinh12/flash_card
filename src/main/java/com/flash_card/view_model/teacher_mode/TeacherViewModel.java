package com.flash_card.view_model.teacher_mode;

import com.flash_card.model.dao.AssignedSetDao;
import com.flash_card.model.dao.ClassroomDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.Classroom;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;

import java.util.List;

public class TeacherViewModel {
    private ClassroomDao classroomDao;
    private UserDao userDao;
    private AssignedSetDao assignedSetDao;
    private String userId;

    public TeacherViewModel (String userId, EntityManager em) {
        classroomDao = ClassroomDao.getInstance(em);
        userDao = UserDao.getInstance(em);
        assignedSetDao = AssignedSetDao.getInstance(em);
        this.userId = userId;
    }

    public int addClass(String name, String description) {
        User user = userDao.findById(userId);
        try {
            Classroom classroom = new Classroom(name, description, user);
            classroomDao.persistClass(classroom);
            return classroom.getClassroomId();
        } catch (Exception e) {
            System.err.println("Error in creating new class: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public int editClass(int classId, String className, String classDescription) {
        Classroom updateClass = classroomDao.findClassById(classId);
        if (updateClass != null) {
            updateClass.setClassroomName(className);
            updateClass.setDescription(classDescription);
            try {
                classroomDao.updateClass(updateClass);
                return updateClass.getClassroomId();
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        } else {
            return 0;
        }
    }

    public int deleteClass(int classId) {
        Classroom classroom = classroomDao.findClassById(classId);
        if (classroom != null) {
            try {
                classroomDao.deleteClass(classroom);
                return classroom.getClassroomId();
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        } else {
            return 0;
        }
    }

    public List<Classroom> getAllClassByTeacherId() {
        return classroomDao.findAllClassByTeacherId(userId);
    }

    public List<FlashcardSet> getAllFlashcardSetByClassId(int classId) {
        return assignedSetDao.findAllSetsByClassId(classId);
    }

}
