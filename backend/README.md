# Bidder Microservices Backend

This backend consists of a set of Dockerized Spring Boot microservices, designed to demonstrate a microservice architecture. Each microservice serves a unique purpose within the system, working together to provide a comprehensive backend solution.

## Microservices Overview

- **Eureka Server:** Service registry for microservices discovery.
- **API Gateway:** Entry point for all client requests, routing them to appropriate microservices.
- **Auction Catalogue Service:** Manages auction items' data.
- **Auction Processing Service:** Handles the logic for auction bidding and processing.
- **Payment Processing Service:** Manages payment transactions.
- **User Service:** Handles user authentication and profile management.
- **Notification Service:** Sends notifications to users based on various triggers.

## Getting Started

To get a local copy up and running, follow these steps.

### Prerequisites

- Docker and Docker Compose installed.
- Java 17 or later for local development.

### Installation and Start-up

1. Build the Docker images:

   This project uses Docker Compose to manage and run the Docker containers. You can build all the Docker images using the following command:

   ```bash
   docker-compose build
   ```

2. Start the services:

   Once the build process is complete, you can start all the services with:

   ```bash
   docker-compose up
   ```

3. Verify the services:

   After all services are up, you can navigate to `http://localhost:8761` to access the Eureka Dashboard and verify that all microservices are registered.

4. Access the API Gateway:

   The API gateway is set up to listen on port 8080. You can access the various microservice endpoints through the gateway based on the routes defined.

### Stopping the Services

- To stop all running services, you can use the following command:

  ```bash
  docker-compose down
  ```

This command will stop and remove all the containers, networks, and volumes created by `docker-compose up`.
