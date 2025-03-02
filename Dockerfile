# First Stage: Build the JAR using Maven
FROM maven:latest AS build
WORKDIR /app
COPY . .
RUN mvn clean package

# Second Stage: Run the JAR using OpenJDK
FROM openjdk:21
WORKDIR /app
COPY --from=build /app/target/flash_card-1.0.jar app.jar
CMD ["java", "-jar", "app.jar"]