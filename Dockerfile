FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the JAR (adjust the name if needed)
COPY target/*.jar app.jar

# Run the app
# Use the default profile if none is provided, otherwise use the provided one inside the .env file
ENTRYPOINT ["java", "-jar", "app.jar"]