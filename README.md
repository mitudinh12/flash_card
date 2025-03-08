# Flashcard Application

## Overview

SuperFlash is a JavaFX-based study application designed to help users learn and memorize concepts efficiently through flashcards. The application can be used by both students and teachers, making it an efficient and user-friendly tool for creating, studying, and reviewing study materials. It connects to a MariaDB database via a TCP connection for data persistence. Developed with Java and built using Maven, SuperFlash ensures a robust and scalable software architecture. This project is ideal for students, educators, or anyone looking to enhance their learning experience.

## User Manual

For detailed instructions on how to use the application, refer to the [**User Manual**](https://github.com/nhidinh91/flash_card/blob/tu-readme/src/main/resources/documents/User%20Manual%20Guide%20for%20Online%20Flashcard%20System.pdf).

## Development Setup

The following diagram describes the development environment of the Flashcard system, including the essential tools and components:

<p align="center">![Diagram](/src/main/resources/documents/dev-env.png)</p>


### Nodes

Developer Machine: The primary workstation (laptop, desktop computer, etc.), which can run on Windows, Linux, or Mac operating systems. It serves as the base for all development activities.

IntelliJ IDEA: The Integrated Development Environment (IDE) used for coding and development.

MariaDB: The relational database management system used in the project.

### Component

JavaFX Application: The project is a JavaFX application, a platform for creating graphical user interfaces using Java. The application communicates with MariaDB over a TCP connection on port 3306 (default MariaDB port).

### Artifacts

db-flashcard.sql: Contains the SQL schema and data definitions for the flashcard application's database. It is used to set up and initialize the database structure.

pom.xml: The Maven Project Object Model (POM) file. Maven is a build automation tool used primarily for Java projects. The pom.xml file contains information about the project and configuration details used by Maven to build the project.

persistence.xml: Configures the Java Persistence API (JPA) settings. It specifies the JPA provider (Hibernate) and contains database connection settings and other persistence-related configurations.

### This setup ensures a streamlined and efficient development workflow for contributors to the Flashcard Application.
