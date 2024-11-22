FROM openjdk:21-slim

ARG APPLICATION_JAR_NAME=user-service-1.0.0.jar

# Copy the JAR file from the target folder into the container
COPY target/user-service-1.0.0.jar user-service-1.0.0.jar

EXPOSE 8081

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "/user-service-1.0.0.jar"]