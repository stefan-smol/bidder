
FROM maven:3.8.4-openjdk-17-slim AS build

# Copy the whole monorepo
COPY . /home/app

# Set the working directory to the microservice's directory
WORKDIR /home/app/user-service


# Build the microservice
RUN mvn clean package -DskipTests -Djib.verbose=true


# Stage 2: Set up the Docker container for the discovery server with JDK 17
FROM openjdk:17-slim


EXPOSE 8081

COPY --from=build /home/app/user-service/target/user-service-*.jar user-service.jar

# Copy the wait-for-it.sh script
COPY ../scripts/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh


ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/user-service.jar"]
# ENTRYPOINT ["/bin/bash", "-c", "/wait-for-it.sh mysql_db:3306 --timeout=60 --strict -- java -Djava.security.egd=file:/dev/./urandom -jar /user-service.jar"]