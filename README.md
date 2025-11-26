# Plana Microservices Monorepo

A monorepo containing all microservices for the Plana application built with Spring Boot and Spring Cloud.

## 📁 Project Structure

```
plana/
├── gateway/              # API Gateway service
├── document_service/     # Document management service
├── loction_service/      # Location service (note: typo in name)
├── todo/                 # Todo service
├── trip/                 # Trip service
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

## 📦 Modules

### Gateway
API Gateway that routes requests to appropriate microservices.

### Document Service
Handles document management operations including upload and storage.

### Location Service
Manages location-related functionality.

### Todo Service
Provides todo list management capabilities.

### Trip Service
Handles trip planning and management features.

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
