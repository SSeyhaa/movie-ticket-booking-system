FROM openjdk:21-slim

ARG APPLICATION_JAR_NAME=booking-service-1.0.0.jar

# Copy the JAR file from the target folder into the container
COPY target/booking-service-1.0.0.jar booking-service-1.0.0.jar

EXPOSE 8083

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "/booking-service-1.0.0.jar"]