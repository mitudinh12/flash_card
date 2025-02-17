package com.flash_card.view_model.teacher_mode;

import com.flash_card.model.dao.*;
import com.flash_card.model.entity.*;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class TeacherViewModel {
    private ClassroomDao classroomDao;
    private UserDao userDao;
    private AssignedSetDao assignedSetDao;
    private FlashcardSetDao flashcardSetDao;
    private ClassMemberDao classMemberDao;
    private String teacherId;

    public TeacherViewModel (String userId, EntityManager em) {
        classroomDao = ClassroomDao.getInstance(em);
        userDao = UserDao.getInstance(em);
        assignedSetDao = AssignedSetDao.getInstance(em);
        flashcardSetDao = FlashcardSetDao.getInstance(em);
        classMemberDao = ClassMemberDao.getInstance(em);
        this.teacherId = userId;
    }

    public int addClass(String name, String description) {
        User user = userDao.findById(teacherId);
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

    public int deleteClass(Classroom classroom) {
        Classroom foundClassroom = classroomDao.findClassById(classroom.getClassroomId());
        if (foundClassroom != null) {
            try {
                classroomDao.deleteClass(foundClassroom);
                return classroom.getClassroomId();
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        } else {
            return 0;
        }
    }

    public int addStudent(int classId, String studentEmail) {
        User student = userDao.findByEmail(studentEmail);
        Classroom classroom = classroomDao.findClassById(classId);
        if (student != null && classroom != null) {
            ClassMember classMember = new ClassMember(student, classroom);
            try {
                classMemberDao.persistClassMember(classMember);
                return 1;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        } else {
            return 0;
        }
    }

    public int deleteStudent(int classId, String studentId) {
        ClassMember classMember = classMemberDao.findByStudentIdAndClassId(classId, studentId);
        if (classMember != null) {
            try {
                classMemberDao.deleteClassMember(classMember);
                return classMember.getId();
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
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
        for (int setId : listSetIds) {
            FlashcardSet set = flashcardSetDao.findById(setId);
            Classroom classroom = classroomDao.findClassById(classId);
            try {
                AssignedSet assignedSet = new AssignedSet(set, classroom);
                assignedSetDao.persistAssignedSet(assignedSet);
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
        return 1;
    }

    public int deleteAssignedSet(int setId, int classId) {
        AssignedSet assignedSet = assignedSetDao.findBySetIdAndClassId(setId, classId);
        if (assignedSet != null) {
            try {
                assignedSetDao.deleteAssignedSet(assignedSet);
                return 1;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        } else {
            return 0;
        }
    }

}
