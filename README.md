# Plana Microservices Monorepo

A monorepo containing all microservices for the Plana application built with Spring Boot and Spring Cloud.

## 📁 Project Structure

```
plana/
├── gateway/              # API Gateway service
├── document_service/     # Document management service
├── loction_service/      # Location service (note: typo in name)
├── todo/                 # Todo service
└── pom.xml              # Parent POM for multi-module build
```

## 🛠 Tech Stack

- **Java**: 17
- **Spring Boot**: 3.5.8
- **Spring Cloud**: 2025.0.0
- **Build Tool**: Maven
- **Architecture**: Microservices

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Git

### Building the Project

Build all modules from the root directory:

```bash
mvn clean install
```

Build a specific module:

```bash
cd <module-name>
mvn clean install
```

### Running Services

Each service can be run independently:

```bash
cd <module-name>
mvn spring-boot:run
```

## 📦 Microservices

### 🌐 Gateway Service
**Port:** 8080  
**Path:** `/gateway`

The API Gateway acts as a single entry point for all client requests, routing them to the appropriate microservices. Built with Spring Cloud Gateway, it provides:

- **Route Management**: Routes requests based on path patterns to backend services
- **Load Balancing**: Distributes traffic across service instances
- **Monitoring**: Integrated with Prometheus for metrics collection
- **Logging**: Centralized logging with configurable log levels

**Key Routes:**
- `/documents/**` → Document Service (port 8082)
- `/api/reminders/**` → Location Service (port 8083)
- `/todos/**` → Todo Service (port 8081)
- `/api/trips/**` → Trip Service (port 8081)

**Technologies:** Spring Cloud Gateway, Actuator, Prometheus, Loki, Grafana

---

### 📄 Document Service
**Port:** 8082  
**Base Path:** `/documents`

A comprehensive document management service that handles file uploads, storage, and ID extraction from documents.

**Features:**
- **File Upload**: Upload documents with metadata (title, document number)
- **Search**: Search documents by title
- **ID Extraction**: Extract IDs from uploaded documents using OCR/parsing
- **Storage Management**: Manages file storage in the configured upload directory

**Key Endpoints:**
- `POST /documents/upload` - Upload a new document
- `GET /documents/search?title={title}` - Search documents by title
- `GET /documents/all` - Retrieve all documents
- `GET /documents/{id}/extract-id` - Extract ID from a specific document

**Technologies:** Spring Boot, Spring Data JPA, PostgreSQL, Multipart File Handling

---

### 📍 Location Service
**Port:** 8083  
**Base Path:** `/api/reminders`

A location-based reminder service that triggers notifications when users reach specific geographic locations.

**Features:**
- **Location-Based Reminders**: Create reminders tied to specific coordinates
- **Geofencing**: Automatically detect when users enter defined areas
- **Real-time Checking**: Check user location against active reminders
- **Radius-based Triggering**: Configure trigger radius for each reminder

**Key Endpoints:**
- `POST /api/reminders` - Create a new location reminder
- `GET /api/reminders` - Get all reminders
- `POST /api/reminders/{userId}/check` - Check if user is near any reminder locations

**Use Cases:**
- "Remind me to buy groceries when I'm near the supermarket"
- "Alert me when I arrive at the office"
- Location-based task notifications

**Technologies:** Spring Boot, Spring Data JPA, PostgreSQL, Geolocation calculations

---

### ✅ Todo Service
**Port:** 8081  
**Base Path:** `/api/todos`

A feature-rich todo management service with scheduling, reminders, and comprehensive task organization.

**Features:**
- **Task Management**: Create, read, update, delete tasks with rich metadata
- **Categories**: Organize todos by categories (GENERAL, WORK, PERSONAL, SHOPPING, etc.)
- **Priority Levels**: Set priority (LOW, MEDIUM, HIGH, URGENT)
- **Due Dates & Reminders**: Schedule tasks with automatic reminder notifications
- **Search & Filter**: Advanced filtering by category, priority, completion status, and due date
- **Statistics**: Get insights on task completion and pending items
- **Scheduled Reminders**: Automatic background job that sends reminders for upcoming tasks

**Key Endpoints:**
- `POST /api/todos` - Create a new todo
- `GET /api/todos` - List todos with pagination, filtering, and sorting
- `GET /api/todos/{id}` - Get a specific todo
- `PATCH /api/todos/{id}` - Update a todo
- `DELETE /api/todos/{id}` - Delete a todo
- `GET /api/todos/stats` - Get todo statistics
- `POST /api/todos/internal/run-reminders` - Manually trigger reminder scan

**Technologies:** Spring Boot, Spring Data JPA, Spring Scheduling, PostgreSQL, Bean Validation

## 🔧 Development

### Adding a New Module

1. Create the module directory
2. Add the module to the parent `pom.xml`:
   ```xml
   <modules>
       <module>your-new-module</module>
   </modules>
   ```
3. Configure the module's `pom.xml` with the parent reference

### Running Tests

Run tests for all modules:

```bash
mvn test
```

Run tests for a specific module:

```bash
cd <module-name>
mvn test
```

## 📝 Configuration

Each service has its own `application.properties` or `application.yml` in its `src/main/resources` directory.

## 🤝 Contributing

1. Create a feature branch from `main`
2. Make your changes
3. Run tests to ensure everything works
4. Submit a pull request

## 📄 License

[Add your license information here]

## 👥 Authors

[Add author information here]
