FROM eclipse-temurin:21-jdk-alpine
RUN mkdir -p /usr/src/app
COPY target/*.jar /usr/src/app/app.jar
EXPOSE 8084
CMD ["java", "-jar", "/usr/src/app/app.jar"]