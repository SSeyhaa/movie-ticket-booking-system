FROM openjdk:21-slim

ARG APPLICATION_JAR_NAME=feedback-service-1.0.0.jar

# Copy the JAR file from the target folder into the container
COPY target/feedback-service-1.0.0.jar feedback-service-1.0.0.jar

EXPOSE 8086

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "/feedback-service-1.0.0.jar"]