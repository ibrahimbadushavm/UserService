FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /workspace

LABEL authors="ibrahimbadushavm"

# Copy pom and download dependencies to leverage layer caching
COPY pom.xml .
RUN mvn -B -Dmaven.test.skip=true dependency:go-offline

# Copy source and build the application
COPY src ./src
RUN mvn -B -Dmaven.test.skip=true package

# Runtime stage: lightweight JRE
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

ARG JAR_FILE=target/*.jar
COPY --from=build /workspace/${JAR_FILE} app.jar

ENV AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} \
    AWS_LOG_REGION=${AWS_LOG_REGION} \
    AWS_REGION=${AWS_REGION} \
    AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY} \
    EUREKA_SERVER_HOST=${EUREKA_SERVER_HOST} \
    EUREKA_SERVER_PORT=${EUREKA_SERVER_PORT} \
    USER_SERVICE_DB_PASSWORD=${USER_SERVICE_DB_PASSWORD} \
    USER_SERVICE_DB_URL=${USER_SERVICE_DB_URL} \
    USER_SERVICE_DB_USERNAME=${USER_SERVICE_DB_USERNAME} \
    USER_SERVICE_PORT=${USER_SERVICE_PORT} \
    JAVA_OPTS=""



ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
