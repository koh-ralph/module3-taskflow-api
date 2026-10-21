# Build stage: compile the jar with Maven on JDK 21.
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B -ntp -q dependency:go-offline
COPY src ./src
RUN mvn -B -ntp -q package -DskipTests

# Run stage: only the JRE and the jar.
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/taskflow-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
