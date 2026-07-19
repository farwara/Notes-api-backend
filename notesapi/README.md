# NotesAPI

## Project Description

NotesAPI is a RESTful backend application developed using Spring Boot. The application allows users to create, manage, organize, and search notes. Users can assign categories and tags, upload attachments, create reminders, and securely access the application using Keycloak authentication.

This project was developed as the final backend assignment for NOVI Hogeschool.

---

## Features

- User authentication with Keycloak
- JWT-based authorization
- CRUD operations for Notes
- CRUD operations for Categories
- CRUD operations for Tags
- CRUD operations for Reminders
- File attachment upload and download
- Validation of request data
- Global exception handling
- PostgreSQL database
- Swagger/OpenAPI documentation
- Unit and integration testing

---

## Technologies

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Keycloak
- PostgreSQL
- Hibernate
- Maven
- Swagger / OpenAPI
- JUnit 5
- Mockito

---

## Project Structure

```
src
├── controller
├── dto
├── entity
├── exception
├── mapper
├── repository
├── security
├── service
└── config
```

---

## Installation

Clone the repository.

```
git clone <repository-url>
```

Open the project in IntelliJ IDEA.

Install Maven dependencies.

```
mvn clean install
```

---

## Database

Create a PostgreSQL database.

Update your `application.properties` with your own database credentials.

Example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/notesapi
spring.datasource.username=your_username
spring.datasource.password=your_password
```

---

## Keycloak

Start Keycloak.

Import the provided Realm JSON.

Configure the client and users.

---

## Running the Application

Start PostgreSQL.

Start Keycloak.

Run the Spring Boot application.

The API will become available on:

```
http://localhost:8080
```

Swagger (if enabled):

```
http://localhost:8080/swagger-ui/index.html
```

---

## Running Tests

Run all tests using:

```
mvn test
```

or directly inside IntelliJ.

---

## API Documentation

API endpoints can be tested using:

- Postman Collection
- Swagger UI (when enabled)

---

## Security

Authentication is implemented using Keycloak and JWT Bearer Tokens.

Endpoints are protected using Spring Security and role-based authorization.

---

## Author

Farwa Rafique
