FROM maven:3.9-eclipse-temurin-21 AS builder

ARG SPRING_PROFILE=staging

WORKDIR /app

COPY ./greenhouse /app/greenhouse/

WORKDIR /app/greenhouse

RUN mvn clean install -DskipTests=true

FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY --from=builder /app/greenhouse/target/*.jar /app/greenhouse/greenhouse.jar
COPY ./greenhouse /app/greenhouse/

ENV SPRING_PROFILES_ACTIVE=$SPRING_PROFILE
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0"

EXPOSE 8080

CMD java $JAVA_OPTS -jar /app/greenhouse/greenhouse.jar --os-profile=${SPRING_PROFILES_ACTIVE}