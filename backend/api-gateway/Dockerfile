
FROM maven:3.8.4-openjdk-17-slim AS build

# Copy the whole monorepo
COPY . /home/app

# Set the working directory to the microservice's directory
WORKDIR /home/app/api-gateway


# Build the microservice
RUN mvn clean package -DskipTests

# Stage 2: Set up the Docker container for the discovery server with JDK 17
FROM openjdk:17-slim


EXPOSE 8080

COPY --from=build /home/app/api-gateway/target/api-gateway-*.jar api-gateway.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/api-gateway.jar"]