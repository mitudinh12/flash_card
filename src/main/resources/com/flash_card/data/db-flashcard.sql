-- Drop the database if it already exists
DROP DATABASE IF EXISTS flash_card;

-- Create the database
CREATE DATABASE flash_card;

-- Use the database
USE flash_card;

-- Create the user table
CREATE TABLE user
(
  user_id INT NOT NULL AUTO_INCREMENT,
  user_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  PRIMARY KEY (user_id)
);

-- Create the flashcard_set table
CREATE TABLE flashcard_set
(
  set_id INT NOT NULL AUTO_INCREMENT,
  set_name VARCHAR(255) NOT NULL,
  set_description VARCHAR(255),
  set_topic VARCHAR(255),
  creator_id INT NOT NULL,
  PRIMARY KEY (set_id),
  FOREIGN KEY (creator_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- Create the flashcard table
CREATE TABLE flashcard
(
  card_id INT NOT NULL AUTO_INCREMENT,
  term VARCHAR(255) NOT NULL,
  definition TEXT NOT NULL,
  difficult_level VARCHAR(50),
  set_id INT NOT NULL,
  creator_id INT NOT NULL,
  PRIMARY KEY (card_id),
  FOREIGN KEY (set_id) REFERENCES flashcard_set(set_id) ON DELETE CASCADE,
  FOREIGN KEY (creator_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- Create the classroom table
CREATE TABLE classroom
(
  classroom_id INT NOT NULL AUTO_INCREMENT,
  classroom_name VARCHAR(255) NOT NULL,
  description VARCHAR(255),
  teacher_id INT NOT NULL,
  PRIMARY KEY (classroom_id),
  FOREIGN KEY (teacher_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- Create the study table
CREATE TABLE study
(
  study_id INT NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL,
  set_id INT NOT NULL,
  start_time DATETIME NOT NULL,
  end_time DATETIME,
  number_studied_words INT DEFAULT 0,
  PRIMARY KEY (study_id),
  FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
  FOREIGN KEY (set_id) REFERENCES flashcard_set(set_id) ON DELETE CASCADE
);

-- Create the quiz table
CREATE TABLE quiz
(
  quiz_id INT NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL,
  set_id INT NOT NULL,
  start_time DATETIME NOT NULL,
  end_time DATETIME,
  correct_times INT DEFAULT 0,
  wrong_times INT DEFAULT 0,
  PRIMARY KEY (quiz_id),
  FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
  FOREIGN KEY (set_id) REFERENCES flashcard_set(set_id) ON DELETE CASCADE
);

-- Create the assigned table
CREATE TABLE assigned
(
  set_id INT NOT NULL,
  classroom_id INT NOT NULL,
  PRIMARY KEY (set_id, classroom_id),
  FOREIGN KEY (set_id) REFERENCES flashcard_set(set_id) ON DELETE CASCADE,
  FOREIGN KEY (classroom_id) REFERENCES classroom(classroom_id) ON DELETE CASCADE
);

-- Create the belongs table
CREATE TABLE belongs
(
  student_id INT NOT NULL,
  classroom_id INT NOT NULL,
  PRIMARY KEY (student_id, classroom_id),
  FOREIGN KEY (student_id) REFERENCES user(user_id) ON DELETE CASCADE,
  FOREIGN KEY (classroom_id) REFERENCES classroom(classroom_id) ON DELETE CASCADE
);

-- Create the sharing table
CREATE TABLE sharing
(
  user_id INT NOT NULL,
  set_id INT NOT NULL,
  PRIMARY KEY (user_id, set_id),
  FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
  FOREIGN KEY (set_id) REFERENCES flashcard_set(set_id) ON DELETE CASCADE
);

-- Drop the user account appuser, if it exists
DROP USER IF EXISTS 'appuser'@'localhost';

-- Create the user account appuser
CREATE USER 'appuser'@'localhost' IDENTIFIED BY 'app_password';

-- Grant privileges to appuser
GRANT SELECT, INSERT, UPDATE, DELETE, DROP, ALTER, CREATE ON flash_card.* TO 'appuser'@'localhost';

-- Flush privileges
FLUSH PRIVILEGES;
