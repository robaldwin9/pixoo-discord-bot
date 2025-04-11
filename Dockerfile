# Use a multi-stage build to optimize image size
# First stage: Build the application
FROM maven:3.8.8-eclipse-temurin-17 AS builder
WORKDIR /app
COPY ./pom.xml pom.xml
COPY ./src ./src
RUN ls ./src
RUN mvn clean package -DskipTests

# Second stage: Create the runtime image
FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /app/target/pixoobot.jar pixoobot.jar
COPY --from=builder /app/target/logback.xml logback.xml
COPY --from=builder /app/target/config.properties config.properties
CMD ["java", "-jar", "pixoobot.jar"]