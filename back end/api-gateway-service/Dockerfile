FROM openjdk:21-slim

ARG APPLICATION_JAR_NAME=api-gateway-service-1.0.0.jar

# Copy the JAR file from the target folder into the container
COPY target/api-gateway-service-1.0.0.jar api-gateway-service-1.0.0.jar

EXPOSE 8443

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "/api-gateway-service-1.0.0.jar"]