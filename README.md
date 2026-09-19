# Novels

A Spring Boot REST API for managing authors and their novels, backed by MySQL.

## Tech Stack

- Java 17
- Spring Boot 3.5.16 (Web, Data JPA, Validation)
- Hibernate / JPA
- MySQL 8
- Maven
- JUnit 5 + Mockito

## Features

- Create, update, and delete authors
- Add, update, and delete novels
- Fetch novels by author name or by author name + year
- One-to-many relationship between authors and novels with cascade delete
- Centralized exception handling
- Bean validation on request bodies and path variables

## Getting Started

### 1. Set up the database

Run `src/main/resources/tableScript.sql` against MySQL to create the `novelsdb` schema and seed data.

### 2. Configure the connection

Update `src/main/resources/application.properties`:
```
spring.datasource.url=jdbc:mysql://localhost:3306/novelsdb
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
server.port=8081
```
### 3. Run
```powershell
.\mvnw spring-boot:run
```
The API will be available at http://localhost:8081.

## API Endpoints

### Authors
Method	Path	Description
GET	/novels/authors	List all authors with their novels
POST	/novels/authors	Add a new author
PUT	/novels/authors/{authorId}/{authorName}	Update an author's name
DELETE	/novels/authors/{authorId}	Delete an author and their novels
GET	/novels/authors/{authorName}	Get novels by author name
GET	/novels/authors/{authorName}/{year}	Get novels by author name and year
Novels
Method	Path	Description
POST	/novels	Add a novel
PUT	/novels	Update a novel
DELETE	/novels/{novelId}	Delete a novel

### Example — get novels by author

```
GET /novels/authors/Charles
```

Response:
```json
[
  {
    "id": 14,
    "title": "New Novel",
    "year": 2020,
    "authId": 1001
  }
]
```

### Example — add a novel

```
POST /novels
Content-Type: application/json
```
Request:

```json
{
  "title": "Bleak House",
  "year": 1853,
  "authId": 1001
}
```

Response (201):

```json
{
  "id": 50,
  "title": "Bleak House",
  "year": 1853,
  "authId": 1001
}
```

## Running Tests

```powershell
.\mvnw test
```