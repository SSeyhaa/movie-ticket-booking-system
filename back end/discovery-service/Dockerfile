FROM openjdk:21-slim

ARG APPLICATION_JAR_NAME=discovery-service-1.0.0.jar

# Copy the JAR file from the target folder into the container
COPY target/discovery-service-1.0.0.jar discovery-service-1.0.0.jar

EXPOSE 8761

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "/discovery-service-1.0.0.jar"]