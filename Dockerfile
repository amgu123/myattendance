# Use Java 17 as the base image (you can adjust the version based on your needs)
FROM eclipse-temurin:17-jdk

# Set working directory in container
WORKDIR /app

# Copy the JAR file (assuming your application is built as a JAR)
COPY target/*.jar app.jar

# Copy any additional resources if needed
COPY src/main/resources/ /app/resources/

# Expose the port your application runs on (adjust as needed)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
