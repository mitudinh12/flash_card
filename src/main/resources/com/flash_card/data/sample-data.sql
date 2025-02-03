-- Insert Users
INSERT INTO users (user_id, user_name, email, password) VALUES
                                                            (1, 'John Doe', 'john.doe@example.com', '5f4dcc3b5aa765d61d8327deb882cf99'),
                                                            (2, 'Jane Smith', 'jane.smith@example.com', '5f4dcc3b5aa765d61d8327deb882cf99'),
                                                            (3, 'Alice Johnson', 'alice.j@example.com', '5f4dcc3b5aa765d61d8327deb882cf99'),
                                                            (4, 'Bob Brown', 'bob.brown@example.com', '5f4dcc3b5aa765d61d8327deb882cf99');

-- Insert Flashcard Sets
INSERT INTO flashcard_sets (set_id, set_name, set_description, set_topic, creator_id) VALUES
                                                                                          (1, 'Biology Basics', 'Introduction to biology concepts', 'Biology', 202131503118642168253),
                                                                                          (2, 'Physics 101', 'Fundamental physics principles', 'Physics', 303131503118642168254),
                                                                                          (3, 'Advanced Physics', 'Advanced topics in physics', 'Physics', 202131503118642168255),
                                                                                          (4, 'Chemistry Basics', 'Introduction to chemistry concepts', 'Chemistry', 101131503118642168252);

-- Insert Flashcards
INSERT INTO flashcards (card_id, term, definition, difficult_level, set_id, creator_id) VALUES
                                                                                            (1, 'Photosynthesis', 'Process by which plants make food', 'easy', 1, 202131503118642168253),
                                                                                            (2, 'Mitochondria', 'Powerhouse of the cell', 'hard', 1, 202131503118642168253),
                                                                                            (3, 'Newton\'s Laws', 'Laws of motion and gravity', 'easy', 2, 303131503118642168254),
    (4, 'Quantum Physics', 'Study of atomic and subatomic particles', 'hard', 3, 202131503118642168255);

-- Insert Classrooms
INSERT INTO classrooms (classroom_id, classroom_name, description, teacher_id) VALUES
    (1, 'Biology Class', 'Introductory biology for beginners', 202131503118642168253),
    (2, 'Physics Class', 'Fundamentals of physics', 303131503118642168254),
    (3, 'Chemistry Class', 'Basic chemistry concepts', 101131503118642168252);

-- Insert Studies
INSERT INTO studies (study_id, user_id, set_id, start_time, end_time, number_studied_words) VALUES
    (1, 202131503118642168253, 1, '2023-10-01 08:00:00', '2023-10-01 09:00:00', 15),
    (2, 303131503118642168254, 2, '2023-10-02 14:00:00', '2023-10-02 15:00:00', 20),
    (3, 202131503118642168255, 3, '2023-10-03 16:00:00', '2023-10-03 17:00:00', 10);

-- Insert Quizzes
INSERT INTO quizzes (quiz_id, user_id, set_id, start_time, end_time, correct_times, wrong_times) VALUES
    (1, 202131503118642168253, 1, '2023-10-01 10:00:00', '2023-10-01 10:30:00', 8, 2),
    (2, 303131503118642168254, 2, '2023-10-02 11:00:00', '2023-10-02 11:45:00', 5, 5),
    (3, 202131503118642168255, 3, '2023-10-03 09:00:00', '2023-10-03 09:20:00', 10, 0);

-- Insert Assigned Sets (Flashcard Sets to Classrooms)
INSERT INTO assigned_sets (set_id, classroom_id) VALUES
    (1, 1),
    (2, 2),
    (3, 2),
    (4, 3);

-- Insert Classroom Members (Users to Classrooms)
INSERT INTO classroom_members (student_id, classroom_id) VALUES
    (303131503118642168254, 1),
    (202131503118642168255, 1),
    (202131503118642168253, 2),
    (202131503118642168255, 3);

-- Insert Shared Sets (Users to Flashcard Sets)
INSERT INTO shared_sets (user_id, set_id) VALUES
    (202131503118642168253, 2),
    (303131503118642168254, 1),
    (202131503118642168255, 4),
    (101131503118642168252, 3);