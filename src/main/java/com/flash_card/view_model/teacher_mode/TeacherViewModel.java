package com.flash_card.view_model.teacher_mode;

import com.flash_card.model.dao.ClassroomDao;
import com.flash_card.model.dao.UserDao;
import com.flash_card.model.dao.AssignedSetDao;
import com.flash_card.model.dao.FlashcardSetDao;
import com.flash_card.model.dao.ClassMemberDao;
import com.flash_card.model.entity.FlashcardSet;
import com.flash_card.model.entity.Classroom;
import com.flash_card.model.entity.User;
import com.flash_card.model.entity.ClassMember;
import com.flash_card.model.entity.AssignedSet;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * ViewModel for performing teacher-related operations, such as managing classrooms,
 * students, and flashcard sets. Acts as a bridge between the teacher UI and data access layer.
 */
public class TeacherViewModel {

    /**
     * Data access object for classroom operations.
     */
    private final ClassroomDao classroomDao;

    /**
     * Data access object for user operations.
     */
    private final UserDao userDao;

    /**
     * Data access object for assigned flashcard sets.
     */
    private final AssignedSetDao assignedSetDao;

    /**
     * Data access object for flashcard set operations.
     */
    private final FlashcardSetDao flashcardSetDao;

    /**
     * Data access object for class membership (student-class relationships).
     */
    private final ClassMemberDao classMemberDao;

    /**
     * The ID of the logged-in teacher.
     */
    private final String teacherId;

    /**
     * Constructs a new {@code TeacherViewModel} using the given user ID and entity manager.
     *
     * @param userId the ID of the teacher
     * @param em     the {@link EntityManager} for database access
     */
    public TeacherViewModel(final String userId, final EntityManager em) {
        classroomDao = ClassroomDao.getInstance(em);
        userDao = UserDao.getInstance(em);
        assignedSetDao = AssignedSetDao.getInstance(em);
        flashcardSetDao = FlashcardSetDao.getInstance(em);
        classMemberDao = ClassMemberDao.getInstance(em);
        this.teacherId = userId;
    }

    /**
     * Adds a new classroom for the teacher.
     *
     * @param name        the name of the classroom
     * @param description the description of the classroom
     * @return {@code 1} if successful, {@code -1} otherwise
     */
    public int addClass(final String name, final String description) {
        User user = userDao.findById(teacherId);
        Classroom classroom = new Classroom(name, description, user);
        boolean result = classroomDao.persistClass(classroom);
        return result ? 1 : -1;
    }

    /**
     * Edits the details of an existing classroom.
     *
     * @param classId         the ID of the classroom to update
     * @param className       the new name of the classroom
     * @param classDescription the new description of the classroom
     * @return {@code 1} if successful, {@code 0} if the class was not found
     */
    public int editClass(final int classId, final String className, final String classDescription) {
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

    /**
     * Deletes a classroom.
     *
     * @param classroom the {@link Classroom} to delete
     * @return {@code 1} if successful, {@code -1} otherwise
     */
    public int deleteClass(final Classroom classroom) {
        boolean result = classroomDao.deleteClass(classroom);
        return result ? 1 : -1;
    }

    /**
     * Adds a student to a classroom by their email.
     *
     * @param classId      the ID of the class
     * @param studentEmail the email of the student to add
     * @return {@code 1} if successful, {@code 0} if student or class not found
     */
    public int addStudent(final int classId, final String studentEmail) {
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

    /**
     * Deletes a student from a classroom.
     *
     * @param classId   the class ID
     * @param studentId the student ID
     * @return {@code 1} if successful, {@code 0} if student not found in the class
     */
    public int deleteStudent(final int classId, final String studentId) {
        ClassMember classMember = classMemberDao.findByStudentIdAndClassId(classId, studentId);
        if (classMember != null) {
            classMemberDao.deleteClassMember(classMember);
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Checks if a user with the given email exists.
     *
     * @param email the user's email
     * @return {@code true} if the user exists, {@code false} otherwise
     */
    public boolean isUserValid(final String email) {
        return userDao.findByEmail(email) != null;
    }

    /**
     * Checks if the student has already been added to the specified class.
     *
     * @param classId      the class ID
     * @param studentEmail the student email
     * @return {@code true} if the student is already added; {@code false} otherwise
     */
    public boolean isStudentAdded(final int classId, final String studentEmail) {
        User student = userDao.findByEmail(studentEmail);
        return classMemberDao.findByStudentIdAndClassId(classId, student.getUserId()) != null;
    }

    /**
     * Returns all classrooms created by the teacher.
     *
     * @return a list of {@link Classroom} entities
     */
    public List<Classroom> getAllClassByTeacherId() {
        return classroomDao.findAllClassByTeacherId(teacherId);
    }

    /**
     * Returns all students associated with a given class.
     *
     * @param classId the class ID
     * @return a list of {@link User} entities representing students
     */
    public List<User> getAllStudentsByClassId(final int classId) {
        return classMemberDao.findAllStudentByClassId(classId);
    }

    /**
     * Returns all flashcard sets assigned to a given class.
     *
     * @param classId the class ID
     * @return a list of {@link FlashcardSet} entities
     */
    public List<FlashcardSet> getAllSetsByClassId(final int classId) {
        return assignedSetDao.findAllSetsByClassId(classId);
    }

    /**
     * Returns all flashcard sets created by the teacher that are not yet assigned to the class.
     *
     * @param classId the class ID
     * @return a list of unassigned {@link FlashcardSet} entities
     */
    public List<FlashcardSet> getAllUnassignedSetsByClassIdAndTeacherId(final int classId) {
        List<FlashcardSet> allSets = flashcardSetDao.findByUserId(teacherId);
        List<FlashcardSet> assignedSets = assignedSetDao.findAllSetsByClassId(classId);
        allSets.removeAll(assignedSets);
        return allSets;
    }

    /**
     * Assigns multiple flashcard sets to a class.
     *
     * @param listSetIds list of flashcard set IDs
     * @param classId    the class ID
     * @return {@code 1} if at least one assignment was successful, {@code -1} otherwise
     */
    public int assignFlashcardSets(final List<Integer> listSetIds, final int classId) {
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

    /**
     * Deletes an assigned flashcard set from a class.
     *
     * @param setId   the ID of the flashcard set
     * @param classId the ID of the class
     * @return {@code 1} if successful, {@code -1} otherwise
     */
    public int deleteAssignedSet(final int setId, final int classId) {
        AssignedSet assignedSet = assignedSetDao.findBySetIdAndClassId(setId, classId);
        boolean result = assignedSetDao.deleteAssignedSet(assignedSet);
        return result ? 1 : -1;
    }
}
