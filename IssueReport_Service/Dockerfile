# Use an official OpenJDK runtime as a parent image (matching your Java 17 setup)
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from your target folder into the container
# (We will run Maven to build this JAR in the next step)
COPY target/IssueReport_Service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your application runs on
EXPOSE 8084

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]