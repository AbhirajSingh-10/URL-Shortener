# URL Shortener Service

A comprehensive, production-ready URL Shortener service built with Spring Boot, similar in functionality to services like Bitly or T.ly. This application allows users to convert long URLs into short, easy-to-share links and tracks click analytics for each link.

The project is fully containerized with Docker and orchestrated with Docker Compose for easy, one-command deployment.

## Core Features

*   **URL Shortening**: Generate a unique, short code for any given long URL.
*   **Custom Aliases**: Users can provide their own custom vanity codes for short links.
*   **Redirection**: Seamlessly redirects users from the short link to the original destination URL.
*   **Click Analytics**: Tracks the number of clicks for each shortened link and provides a statistics endpoint.
*   **URL Expiration**: Set an optional time-to-live (TTL) on links, after which they will no longer be active.
*   **Automated Cleanup**: A scheduled background job runs daily to automatically delete expired links from the database, ensuring system hygiene.
*   **RESTful API**: A well-defined API for programmatic interaction.
*   **Simple Web UI**: A basic Thymeleaf-based frontend for manual URL shortening and stats checking.
*   **Robust Error Handling**: Centralized exception handling provides consistent and meaningful error responses.

## Technology Stack

*   **Backend**:
    *   Java 17
    *   Spring Boot 3
    *   Spring Web (for REST APIs and MVC)
    *   Spring Data JPA (for database interaction)
    *   Hibernate
*   **Database**:
    *   PostgreSQL (for production)
    *   H2 Database (for development/testing)
*   **Frontend**:
    *   Thymeleaf (for server-side rendered HTML)
    *   HTML5 & CSS
*   **Build & Dependency Management**:
    *   Apache Maven
*   **Deployment**:
    *   Docker
    *   Docker Compose

## Getting Started

Follow these instructions to get the project up and running on your local machine for development and testing purposes.

### Prerequisites

You must have the following tools installed on your system:

*   **Docker**: [Get Docker](https://www.docker.com/get-started)
*   **Docker Compose**: Docker Compose is included with Docker Desktop for Windows and macOS. For Linux, you may need to [install it separately](https://docs.docker.com/compose/install/).
*   **Git**: To clone the repository.

### Running the Application with Docker (Recommended)

This is the simplest and recommended way to run the entire application stack.

1.  **Clone the repository:**
    ```bash
    git clone <your-repository-url>
    cd <repository-directory>
    ```

2.  **Run with Docker Compose:**
    Navigate to the root directory of the project (where `docker-compose.yml` is located) and run the following command:
    ```bash
    docker-compose up --build
    ```
    This command will:
    *   Build the Spring Boot application's Docker image using the `Dockerfile`.
    *   Pull the official PostgreSQL image from Docker Hub.
    *   Create and start containers for both the application and the database.
    *   Create a persistent volume for the database to ensure your data is not lost on restart.
    *   Set up a network for the containers to communicate with each other.

3.  **Verify the application is running:**
    Once the containers are up and running, you can access the application:
    *   **Web UI**: Open your browser and go to `http://localhost:8080`
    *   **API**: Use a tool like Postman or `curl` to interact with the API endpoints.

4.  **Stopping the application:**
    To stop all the running containers, press `Ctrl+C` in the terminal where `docker-compose` is running, and then run:
    ```bash
    docker-compose down
    ```
    To stop the containers and remove the database volume (deleting all data), run:
    ```bash
    docker-compose down -v
    ```

## API Endpoints

The primary API endpoints are available under the `/api/v1` path.

| Method | Endpoint                      | Description                                                               | Sample Request Body                                           |
| ------ | ----------------------------- | ------------------------------------------------------------------------- | ------------------------------------------------------------- |
| `POST` | `/api/v1/url/shorten`         | Creates a new short link. `customAlias` and `hoursToExpire` are optional. | `{"url": "...", "customAlias": "...", "hoursToExpire": 24}` |
| `GET`  | `/{shortCode}`                | Redirects to the original URL.                                            | N/A                                                           |
| `GET`  | `/api/v1/url/stats/{shortCode}` | Retrieves click statistics for a short link.                              | N/A                                                           |

## Configuration

The application's production configuration is managed through environment variables injected via the `docker-compose.yml` file. This includes:

*   `SPRING_PROFILES_ACTIVE=prod`: Activates the production profile.
*   `DB_USERNAME`: The username for the PostgreSQL database.
*   `DB_PASSWORD`: The password for the PostgreSQL database.

These values are defined in `docker-compose.yml` and are used to populate the settings in `src/main/resources/application-prod.properties`.
