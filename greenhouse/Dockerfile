FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY ./greenhouse ./

RUN mvn clean install -DskipTests=true

FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY --from=builder /app/target/*.jar /app/greenhouse.jar

EXPOSE 8080
CMD ["java", "-jar", "/app/greenhouse.jar"]