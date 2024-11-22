FROM openjdk:21-slim

ARG APPLICATION_JAR_NAME=payment-service-1.0.0.jar

# Copy the JAR file from the target folder into the container
COPY target/payment-service-1.0.0.jar payment-service-1.0.0.jar

EXPOSE 8084

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "/payment-service-1.0.0.jar"]