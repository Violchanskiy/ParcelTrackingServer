FROM openjdk:17-jdk-slim
WORKDIR /app
COPY PostService.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
