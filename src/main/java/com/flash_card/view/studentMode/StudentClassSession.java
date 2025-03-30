package com.flash_card.view.studentMode;

public class StudentClassSession {
    private static StudentClassSession instance;
    private int classId;
    private String className;
    private String teacherName;

    private StudentClassSession() {}

    public static StudentClassSession getInstance() {
        if (instance == null) {
            instance = new StudentClassSession();
        }
        return instance;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}