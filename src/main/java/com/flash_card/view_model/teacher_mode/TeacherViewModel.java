package com.flash_card.view_model.teacher_mode;

import com.flash_card.model.dao.*;
import com.flash_card.model.entity.*;
import jakarta.persistence.EntityManager;

import java.util.List;

public class TeacherViewModel {
    private final ClassroomDao classroomDao;
    private final UserDao userDao;
    private final AssignedSetDao assignedSetDao;
    private final FlashcardSetDao flashcardSetDao;
    private final ClassMemberDao classMemberDao;
    private final String teacherId;

    public TeacherViewModel(String userId, EntityManager em) {
        classroomDao = ClassroomDao.getInstance(em);
        userDao = UserDao.getInstance(em);
        assignedSetDao = AssignedSetDao.getInstance(em);
        flashcardSetDao = FlashcardSetDao.getInstance(em);
        classMemberDao = ClassMemberDao.getInstance(em);
        this.teacherId = userId;
    }

    public int addClass(String name, String description) {
        User user = userDao.findById(teacherId);
        Classroom classroom = new Classroom(name, description, user);
        boolean result = classroomDao.persistClass(classroom);
        if (result) {
            return 1;
        } else {
            return -1;
        }
    }

    public int editClass(int classId, String className, String classDescription) {
        Classroom updateClass = classroomDao.findClassById(classId);
        if (updateClass == null) {
            return 0;
        } else {
            updateClass.setClassroomName(className);
            updateClass.setDescription(classDescription);
            classroomDao.updateClass(updateClass);
            return 1;
        }
    }

    public int deleteClass(Classroom classroom) {
        boolean result = classroomDao.deleteClass(classroom);
        if (result) {
            return 1;
        } else {
            return -1;
        }
    }

    public int addStudent(int classId, String studentEmail) {
        User student = userDao.findByEmail(studentEmail);
        Classroom classroom = classroomDao.findClassById(classId);
        if (student != null && classroom != null) {
            ClassMember classMember = new ClassMember(student, classroom);
            classMemberDao.persistClassMember(classMember);
            return 1;
        } else {
            return 0;
        }
    }

    public int deleteStudent(int classId, String studentId) {
        ClassMember classMember = classMemberDao.findByStudentIdAndClassId(classId, studentId);
        if (classMember != null) {
            classMemberDao.deleteClassMember(classMember);
            return 1;
        } else {
            return 0;
        }
    }

    public boolean isUserValid(String email) {
        return userDao.findByEmail(email) != null;
    }

    public boolean isStudentAdded(int classId, String studentEmail) {
        User student = userDao.findByEmail(studentEmail);
        return classMemberDao.findByStudentIdAndClassId(classId, student.getUserId()) != null; // return true if student is already added to class
    }

    public List<Classroom> getAllClassByTeacherId() {
        return classroomDao.findAllClassByTeacherId(teacherId);
    }

    public List<User> getAllStudentsByClassId(int classId) {
        return classMemberDao.findAllStudentByClassId(classId);
    }

    public List<FlashcardSet> getAllSetsByClassId(int classId) {
        return assignedSetDao.findAllSetsByClassId(classId);
    }

    public List<FlashcardSet> getAllUnassignedSetsByClassIdAndTeacherId(int classId) {
        List<FlashcardSet> allSets = flashcardSetDao.findByUserId(teacherId);
        List<FlashcardSet> assignedSets = assignedSetDao.findAllSetsByClassId(classId);
        allSets.removeAll(assignedSets);
        return allSets;
    }

    public int assignFlashcardSets(List<Integer> listSetIds, int classId) {
        int result = -1;
        for (int setId : listSetIds) {
            FlashcardSet set = flashcardSetDao.findById(setId);
            Classroom classroom = classroomDao.findClassById(classId);
            AssignedSet assignedSet = new AssignedSet(set, classroom);
            boolean result1 = assignedSetDao.persistAssignedSet(assignedSet);
            if (result1) {
                result = 1;
            }
        }
        return result;

    }

    public int deleteAssignedSet(int setId, int classId) {
        AssignedSet assignedSet = assignedSetDao.findBySetIdAndClassId(setId, classId);
        boolean result = assignedSetDao.deleteAssignedSet(assignedSet);
        if (result) {
            return 1;
        } else {
            return -1;
        }
    }

}
