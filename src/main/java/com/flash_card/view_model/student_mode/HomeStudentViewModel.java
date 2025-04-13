package com.flash_card.view_model.student_mode;

import com.flash_card.model.dao.AssignedSetDao;
import com.flash_card.model.dao.ClassMemberDao;
import com.flash_card.model.entity.Classroom;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * ViewModel class for managing the student home view.
 * Provides methods to load classes and retrieve class information for a student.
 */
public class HomeStudentViewModel {
    /**
     * The ID of the student.
     */
    private String studentId;
    /**
     * DAO for managing assigned sets.
     */
    private final AssignedSetDao assignedSetDao;
    /**
     * DAO for managing class members.
     */
    private final ClassMemberDao classMemberDao;
    /**
     * List of classes the student is a member of.
     */
    private List<Classroom> classes;
    /**
     * Constructor to initialize the ViewModel with the DAOs and student ID.
     *
     * @param studentIdParam     the ID of the student
     * @param entityManager the {@link EntityManager} instance for database operations
     */
    public HomeStudentViewModel(final String studentIdParam, final EntityManager entityManager) {
        classMemberDao = ClassMemberDao.getInstance(entityManager);
        assignedSetDao = AssignedSetDao.getInstance(entityManager);
        this.studentId = studentIdParam;
    }
    /**
     * Loads the classes the student is a member of.
     */
    public void loadClasses() {
        classes = classMemberDao.findAllClassesByStudentId(studentId);
    }
    /**
     * Retrieves information about the classes the student is a member of.
     * Each class information includes class ID, class name, teacher name,
     * number of assigned sets, and number of students in the class.
     *
     * @return a list of maps containing class information
     */
    public List<Map<String, Object>> getClassInfo() {
        List<Map<String, Object>> classInfoList = new ArrayList<>();
        for (Classroom classroom : classes) {
            Map<String, Object> classInfo = new HashMap<>();
            classInfo.put("classId", classroom.getClassroomId());
            classInfo.put("className", classroom.getClassroomName());
            classInfo.put("teacherName", classroom.getTeacher().getFirstName() + " "
                    + classroom.getTeacher().getLastName());
            classInfo.put("numberSet", assignedSetDao.findAllSetsByClassId(classroom.getClassroomId()).size());
            classInfo.put("numberStudent", classMemberDao.findAllStudentByClassId(classroom.getClassroomId()).size());
            classInfoList.add(classInfo);
        }
        return classInfoList;
    }
}
