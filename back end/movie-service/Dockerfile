FROM openjdk:21-slim

ARG APPLICATION_JAR_NAME=movie-service-1.0.0.jar

# Copy the JAR file from the target folder into the container
COPY target/movie-service-1.0.0.jar movie-service-1.0.0.jar

EXPOSE 8082

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "/movie-service-1.0.0.jar"]