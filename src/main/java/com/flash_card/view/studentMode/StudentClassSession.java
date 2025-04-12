package com.flash_card.view.studentMode;
/**
 * Singleton class to manage the session details of a student class.
 * Stores the class ID, class name, and teacher name for the current session.
 */
public class StudentClassSession {
    /**
     * The single instance of the `StudentClassSession` class.
     */
    private static StudentClassSession instance;

    /**
     * The name of the class in the current session.
     */
    private String className;
    /**
     * The name of the teacher for the class in the current session.
     */
    private String teacherName;
    /**
     * The ID of the class in the current session.
     */
    private int classId;
    /**
     * Private constructor to prevent instantiation from outside the class.
     */
    private StudentClassSession() {
    }
    /**
     * Returns the single instance of the `StudentClassSession` class.
     * If the instance is null, it creates a new instance.
     *
     * @return The single instance of the `StudentClassSession` class.
     */
    public static StudentClassSession getInstance() {
        if (instance == null) {
            instance = new StudentClassSession();
        }
        return instance;
    }
    /**
     * Retrieves the ID of the class in the current session.
     *
     * @return the class ID
     */
    public int getClassId() {
        return classId;
    }
    /**
     * Sets the ID of the class in the current session.
     *
     * @param classIdParam the class ID to set
     */
    public void setClassId(int classIdParam) {
        this.classId = classIdParam;
    }
    /**
     * Retrieves the name of the class in the current session.
     *
     * @return the class name
     */
    public String getClassName() {
        return className;
    }
    /**
     * Sets the name of the class in the current session.
     *
     * @param classNameParam the class name to set
     */
    public void setClassName(String classNameParam) {
        this.className = classNameParam;
    }
    /**
     * Retrieves the name of the teacher for the class in the current session.
     *
     * @return the teacher name
     */
    public String getTeacherName() {
        return teacherName;
    }
    /**
     * Sets the name of the teacher for the class in the current session.
     *
     * @param teacherNameParam the teacher name to set
     */
    public void setTeacherName(String teacherNameParam) {
        this.teacherName = teacherNameParam;
    }
}
