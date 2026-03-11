# ─────────────────────────────────────────────
# Stage 1 — Build the JAR
# ─────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set working directory inside the container
WORKDIR /app

# Copy pom.xml first — lets Docker cache dependencies
# so they are not re-downloaded on every build
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Now copy the entire source code
COPY src ./src

# Build the JAR, skip tests for faster builds
RUN mvn clean package -DskipTests

# ─────────────────────────────────────────────
# Stage 2 — Run the JAR
# ─────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy only the built JAR from Stage 1
COPY --from=build /app/target/*.jar app.jar

# Expose the port Spring Boot runs on
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
