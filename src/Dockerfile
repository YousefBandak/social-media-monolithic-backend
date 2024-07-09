FROM openjdk:23-jdk-slim-bullseye
WORKDIR /app
COPY target/*.jar /app/backend.jar
CMD ["java", "-jar", "/app/backend.jar"]
EXPOSE 8080