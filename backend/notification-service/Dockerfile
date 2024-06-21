# Stage 1: Build the project using Maven
FROM maven:3.8.4-openjdk-17-slim AS build

# Copy the project files to the container
COPY . /home/app

# Set the working directory inside the container to the microservice's directory
WORKDIR /home/app/notification-service

# Execute the Maven build, skipping tests for faster builds
RUN mvn clean package -DskipTests -Djib.verbose=true

# Stage 2: Create the final image with just the JRE and the jar file
FROM openjdk:17-slim

# Expose the port the app runs on
EXPOSE 8083

# Copy the built jar file from the build stage to the final image
COPY --from=build /home/app/notification-service/target/notification-service-*.jar /notification-service.jar


# Specify the command to run the application
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/notification-service.jar"]