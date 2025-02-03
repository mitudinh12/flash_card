-- Drop the database if it already exists
DROP DATABASE IF EXISTS flash_card;

-- Create the database
CREATE DATABASE flash_card;

-- Use the database
USE flash_card;

-- Create the users table
CREATE TABLE users (
  user_id VARCHAR(255) NOT NULL COMMENT 'Primary key for users',
  first_name VARCHAR(255) NOT NULL COMMENT 'first name of the user',
  last_name VARCHAR(255) NOT NULL COMMENT 'last name of the user',
  email VARCHAR(255) NOT NULL UNIQUE COMMENT 'Unique email address for the user',
  id_token TEXT NOT NULL COMMENT 'Id token of the user',
  PRIMARY KEY (user_id)
) ENGINE=InnoDB COMMENT='Stores user accounts for the flashcard app';

-- Create the flashcard_sets table
CREATE TABLE flashcard_sets (
  set_id INT NOT NULL AUTO_INCREMENT COMMENT 'Primary key for flashcard sets',
  set_name VARCHAR(255) NOT NULL COMMENT 'Name of the flashcard set',
  set_description VARCHAR(255) COMMENT 'Optional description of the set',
  set_topic VARCHAR(255) COMMENT 'Topic of the flashcard set',
  creator_id VARCHAR(255) NOT NULL COMMENT 'Foreign key referencing users(user_id)',
  PRIMARY KEY (set_id),
  FOREIGN KEY (creator_id) REFERENCES users(user_id) ON DELETE CASCADE,
  INDEX idx_creator_id (creator_id) COMMENT 'Index to speed up queries on creator_id'
) ENGINE=InnoDB COMMENT='Stores flashcard sets created by users';

-- Create the flashcards table
CREATE TABLE flashcards (
  card_id INT NOT NULL AUTO_INCREMENT COMMENT 'Primary key for flashcards',
  term VARCHAR(255) NOT NULL COMMENT 'Term or word being defined',
  definition TEXT NOT NULL COMMENT 'Definition of the term',
  difficult_level ENUM('easy', 'hard') COMMENT 'Difficulty level of the card',
  set_id INT NOT NULL COMMENT 'Foreign key referencing flashcard_sets(set_id)',
  creator_id VARCHAR(255) NOT NULL COMMENT 'Foreign key referencing users(user_id)',
  PRIMARY KEY (card_id),
  FOREIGN KEY (set_id) REFERENCES flashcard_sets(set_id) ON DELETE CASCADE,
  FOREIGN KEY (creator_id) REFERENCES users(user_id) ON DELETE CASCADE,
  INDEX idx_set_id (set_id) COMMENT 'Index to speed up queries on set_id',
  INDEX idx_creator_id (creator_id) COMMENT 'Index to speed up queries on creator_id'
) ENGINE=InnoDB COMMENT='Stores individual flashcards';

-- Create the classrooms table
CREATE TABLE classrooms (
  classroom_id INT NOT NULL AUTO_INCREMENT COMMENT 'Primary key for classrooms',
  classroom_name VARCHAR(255) NOT NULL COMMENT 'Name of the classroom',
  description VARCHAR(255) COMMENT 'Optional description of the classroom',
  teacher_id VARCHAR(255) NOT NULL COMMENT 'Foreign key referencing users(user_id)',
  PRIMARY KEY (classroom_id),
  FOREIGN KEY (teacher_id) REFERENCES users(user_id) ON DELETE CASCADE,
  INDEX idx_teacher_id (teacher_id) COMMENT 'Index to speed up queries on teacher_id'
) ENGINE=InnoDB COMMENT='Stores classrooms managed by teachers';

-- Create the studies table
CREATE TABLE studies (
  study_id INT NOT NULL AUTO_INCREMENT COMMENT 'Primary key for study sessions',
  user_id VARCHAR(255) NOT NULL COMMENT 'Foreign key referencing users(user_id)',
  set_id INT NOT NULL COMMENT 'Foreign key referencing flashcard_sets(set_id)',
  start_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Start time of the study session',
  end_time DATETIME DEFAULT NULL COMMENT 'End time of the study session',
  number_studied_words INT DEFAULT 0 COMMENT 'Number of words studied in the session',
  PRIMARY KEY (study_id),
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
  FOREIGN KEY (set_id) REFERENCES flashcard_sets(set_id) ON DELETE CASCADE,
  INDEX idx_user_id (user_id) COMMENT 'Index to speed up queries on user_id',
  INDEX idx_set_id (set_id) COMMENT 'Index to speed up queries on set_id'
) ENGINE=InnoDB COMMENT='Tracks study sessions for users and flashcard sets';

-- Create the quizzes table
CREATE TABLE quizzes (
  quiz_id INT NOT NULL AUTO_INCREMENT COMMENT 'Primary key for quizzes',
  user_id VARCHAR(255) NOT NULL COMMENT 'Foreign key referencing users(user_id)',
  set_id INT NOT NULL COMMENT 'Foreign key referencing flashcard_sets(set_id)',
  start_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Start time of the quiz',
  end_time DATETIME DEFAULT NULL COMMENT 'End time of the quiz',
  correct_times INT DEFAULT 0 COMMENT 'Number of correct answers',
  wrong_times INT DEFAULT 0 COMMENT 'Number of wrong answers',
  PRIMARY KEY (quiz_id),
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
  FOREIGN KEY (set_id) REFERENCES flashcard_sets(set_id) ON DELETE CASCADE,
  INDEX idx_user_id (user_id) COMMENT 'Index to speed up queries on user_id',
  INDEX idx_set_id (set_id) COMMENT 'Index to speed up queries on set_id'
) ENGINE=InnoDB COMMENT='Tracks quiz attempts for users and flashcard sets';

-- Create the assigned_sets table
CREATE TABLE assigned_sets (
  set_id INT NOT NULL COMMENT 'Foreign key referencing flashcard_sets(set_id)',
  classroom_id INT NOT NULL COMMENT 'Foreign key referencing classrooms(classroom_id)',
  PRIMARY KEY (set_id, classroom_id),
  FOREIGN KEY (set_id) REFERENCES flashcard_sets(set_id) ON DELETE CASCADE,
  FOREIGN KEY (classroom_id) REFERENCES classrooms(classroom_id) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='Tracks assignments of flashcard sets to classrooms';

-- Create the classroom_members table
CREATE TABLE classroom_members (
  student_id VARCHAR(255) NOT NULL COMMENT 'Foreign key referencing users(user_id)',
  classroom_id INT NOT NULL COMMENT 'Foreign key referencing classrooms(classroom_id)',
  PRIMARY KEY (student_id, classroom_id),
  FOREIGN KEY (student_id) REFERENCES users(user_id) ON DELETE CASCADE,
  FOREIGN KEY (classroom_id) REFERENCES classrooms(classroom_id) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='Tracks membership of students in classrooms';

-- Create the shared_sets table
CREATE TABLE shared_sets (
  user_id VARCHAR(255) NOT NULL COMMENT 'Foreign key referencing users(user_id)',
  set_id INT NOT NULL COMMENT 'Foreign key referencing flashcard_sets(set_id)',
  PRIMARY KEY (user_id, set_id),
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
  FOREIGN KEY (set_id) REFERENCES flashcard_sets(set_id) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='Tracks shared flashcard sets between users';


-- Drop the user account appuser, if it exists
DROP USER IF EXISTS 'appuser'@'localhost';

-- Create the user account appuser
CREATE USER 'appuser'@'localhost' IDENTIFIED BY 'app_password';

-- Grant privileges to appuser
GRANT SELECT, INSERT, UPDATE, DELETE, DROP, ALTER, CREATE ON flash_card.* TO 'appuser'@'localhost';

-- Flush privileges
FLUSH PRIVILEGES;
