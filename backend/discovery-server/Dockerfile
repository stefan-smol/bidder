FROM maven:3.8.4-openjdk-17-slim AS build

# Copy the whole monorepo
COPY . /home/app

# Set the working directory to the microservice's directory
WORKDIR /home/app/discovery-server


RUN ls -la /home/app

# Build the microservice
RUN mvn clean package -DskipTests

# Stage 2: Set up the Docker container for the discovery server with JDK 17
FROM openjdk:17-slim

EXPOSE 8761

# Copy the built jar file from the build stage into the container
COPY --from=build /home/app/discovery-server/target/discovery-server-*.jar discovery-server.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/discovery-server.jar"]