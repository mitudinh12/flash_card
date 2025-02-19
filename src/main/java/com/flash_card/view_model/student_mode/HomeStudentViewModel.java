package com.flash_card.view_model.student_mode;

import com.flash_card.model.dao.*;
import com.flash_card.model.entity.Classroom;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeStudentViewModel {
    private AssignedSetDao assignedSetDao;
    private ClassMemberDao classMemberDao;
    String studentId;
    private List<Classroom> classes;

    public HomeStudentViewModel(String studentId, EntityManager entityManager) {
        classMemberDao = ClassMemberDao.getInstance(entityManager);
        assignedSetDao = AssignedSetDao.getInstance(entityManager);
        this.studentId = studentId;
    }

    public void loadClasses() {
        classes = classMemberDao.findAllClassesByStudentId(studentId);
    }

    public List<Map<String, Object>> getClassInfo() {
        List<Map<String, Object>> classInfoList = new ArrayList<>();
        for (Classroom classroom : classes) {
            Map<String, Object> classInfo = new HashMap<>();
            classInfo.put("className", classroom.getClassroomName());
            classInfo.put("teacherName", classroom.getTeacher().getFirstName() + " " + classroom.getTeacher().getLastName());
            classInfo.put("numberSet", assignedSetDao.findAllSetsByClassId(classroom.getClassroomId()).size());
            classInfo.put("numberStudent", classMemberDao.findAllStudentByClassId(classroom.getClassroomId()).size());
            classInfoList.add(classInfo);
        }
        return classInfoList;
    }
}
