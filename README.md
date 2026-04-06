# SuperFlash
*A JavaFX-based Flashcard Study & Classroom Platform*

## 📌 Overview
**SuperFlash** is a modern, feature-rich **desktop flashcard application** designed to support effective studying for both **students and educators**. Built with **JavaFX** and following the **MVVM (Model–View–ViewModel)** architecture, SuperFlash enables users to create, manage, study, and share flashcards, while also supporting classroom-based learning and progress tracking.

The application is suitable for **self-learners**, **teachers**, and **educational institutions**, offering a clean UI, multilingual support, and strong emphasis on software quality.

---

## 🎯 Project Goals
- Apply **Object-Oriented Programming** and **MVVM** principles in a real-world application
- Provide an intuitive flashcard-based study experience
- Support teacher–student collaboration via classrooms
- Track learning progress and performance statistics
- Ensure scalability, maintainability, and testability

---

## ✨ Key Features

### 🔐 Authentication & Security
- Google OAuth 2.0 login
- Secure user authentication using Google Auth Library

### 📚 Flashcard & Set Management
- Create, edit, and delete flashcards
- Organize flashcards into topic-based sets
- Share flashcard sets with other users

### 👥 Classroom Support
- Teacher mode and student mode
- Teachers can create classrooms
- Assign flashcard sets to entire classes
- Students can view assigned materials

### 🎮 Study Modes
- Flashcard review with flip animations
- Shuffle mode to avoid memorization by order
- Quiz mode generated from flashcards
- Card difficulty tracking (easy / difficult)
- Foundation for spaced repetition algorithms

### 📊 Progress Tracking & Statistics
- Track study sessions and reviewed cards
- Quiz results and accuracy statistics
- Classroom-level performance overview for teachers

### 🌍 Localization & Accessibility
- Multi-language UI support:
  - 🇬🇧 English
  - 🇫🇮 Finnish
  - 🇻🇳 Vietnamese
  - 🇰🇷 Korean
  - 🇹🇭 Thai
- Dynamic language switching at runtime
- Localized dates, times, and messages

---

## 🏗 Architecture
SuperFlash follows the **MVVM (Model–View–ViewModel)** pattern using **mvvmFX**, ensuring:
- Clear separation of concerns
- Testable business logic
- Scalable UI components

### Architectural Layers
- **Model**: Entities, database logic, and domain models
- **View**: JavaFX UI components (FXML)
- **ViewModel**: Application logic and state binding

---

## 🧰 Technology Stack

### Core Technologies
- **Java 21 (LTS)**
- **JavaFX 23.0.1**
- **Maven**

### Backend & Persistence
- **Hibernate 6.0.0**
- **JPA 3.0**
- **MariaDB**
- **MariaDB JDBC Driver 3.5.1**

### Authentication & Networking
- **Google OAuth 2.0**
- **Google Auth Library**
- **Apache HttpClient 5.2.1**

### Testing & Quality
- **JUnit 5**
- **TestNG**
- **Mockito**
- **TestFX** (JavaFX UI testing)
- **JaCoCo** (code coverage)
- **SonarQube** (code quality)

### Utilities
- **mvvmFX 1.8.0**
- **Gson 2.10.1**
- **Logback 1.5.16**

---

## Build and Setup Instructions

### Prerequisites
- Java Development Kit (**JDK 21+**)
- Apache Maven **3.6+**
- MariaDB **10.5+** (or MySQL 8+)
- Git
- IDE (IntelliJ IDEA recommended)

---

## 🗄 Database Schema
The application uses **10 core tables**, including:
- `users`
- `flashcard_sets`
- `flashcards`
- `classrooms`
- `class_members`
- `assigned_sets`
- `shared_sets`
- `studies`
- `quizzes`
- `card_difficult_level`

The diagram below shows the relational database design used in SuperFlash, including
users, flashcards, classrooms, study sessions, quizzes, and progress tracking.

![SuperFlash Database Schema](documents/db-schema-flashcard.png)

---

## Usage Guide

### For Students
- Sign in with Google
- Browse personal and shared flashcard sets
- Study using review or quiz mode
- Join classrooms and access assigned materials
- Track personal progress and statistics

### For Teachers
- Switch to Teacher Mode
- Create and manage classrooms
- Upload and assign flashcard sets
- Monitor classroom learning progress

---

## 🌐 Localization
Change language anytime from the top-right dropdown.

Supported languages:
- English (en_US)
- Finnish (fi_FI)
- Vietnamese (vi_VN)
- Korean (ko_KR)
- Thai (th_TH)

---

## 🔧 CI/CD Pipeline
The project includes a **Jenkins CI pipeline** with:
- Build & test automation
- JaCoCo coverage reports
- SonarQube analysis
- Docker image build & push

---

## 📊 Code Quality

### Code Coverage
```bash
mvn jacoco:report
```

### SonarQube Analysis
```bash
mvn sonar:sonar \
  -Dsonar.projectKey=flash_card \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=YOUR_TOKEN
```
