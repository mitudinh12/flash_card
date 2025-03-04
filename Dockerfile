# First Stage: Build the JAR using Maven
FROM maven:latest AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Second Stage: Run the JAR using OpenJDK
FROM openjdk:21
WORKDIR /app

# Install required dependencies for JavaFX
RUN microdnf install -y \
    libX11 \
    libXext \
    libXrender \
    libXtst \
    libXi \
    mesa-libGL \
    gtk3 \
    && microdnf clean all

# Set Default Database URL (can be overridden at runtime)
ENV DB_HOST=localhost

# Copy built JAR from first stage
COPY --from=build /app/target/flash_card-1.0.jar app.jar

# Run the application with the database URL as an argument
#CMD ["java", "-jar", "app.jar"]
CMD ["java", "-DDB_URL=${DB_URL}", "-jar", "app.jar"]