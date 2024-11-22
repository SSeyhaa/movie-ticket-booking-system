FROM openjdk:21-slim

ARG APPLICATION_JAR_NAME=notification-service-1.0.0.jar

# Copy the JAR file from the target folder into the container
COPY target/notification-service-1.0.0.jar notification-service-1.0.0.jar

EXPOSE 8085

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "/notification-service-1.0.0.jar"]