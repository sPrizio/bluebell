FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY . ./

# Ensure the entire project (including Radicle) is built first
RUN mvn clean install -DskipTests

# Extract only the Planter JAR for the runtime image
FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY --from=builder /app/planter/target/*.jar /app/planter.jar

EXPOSE 8080
CMD ["java", "-jar", "/app/planter.jar"]
