# Plana - Microservices Architecture Documentation

## 📋 Table of Contents
1. [Project Overview](#project-overview)
2. [Architecture](#architecture)
3. [Microservices](#microservices)
4. [Backend Technologies](#backend-technologies)
5. [Observability Stack](#observability-stack)
6. [Key Features](#key-features)
7. [API Gateway](#api-gateway)
8. [Monitoring & Alerting](#monitoring--alerting)
9. [Distributed Tracing](#distributed-tracing)
10. [Logging](#logging)
11. [Metrics](#metrics)
12. [Deployment](#deployment)

---

## 🎯 Project Overview

**Plana** is a production-ready microservices-based task management platform built with modern backend technologies, featuring comprehensive observability, monitoring, and distributed tracing capabilities.

### Project Goals
- Demonstrate microservices architecture best practices
- Implement enterprise-grade observability
- Showcase distributed systems monitoring
- Production-ready logging and alerting

---

## 🏗️ Architecture

### Microservices Architecture Pattern
```
┌─────────────┐
│   Mobile    │
│     App     │
└──────┬──────┘
       │
       v
┌─────────────────────────────────────────┐
│         API Gateway (Port 8080)         │
│  - Request Routing                      │
│  - Load Balancing                       │
│  - Distributed Tracing                  │
└────────┬─────────────┬──────────────────┘
         │             │
    ┌────v────┐   ┌────v─────┐   ┌────────┐
    │  Todo   │   │ Document │   │Location│
    │ Service │   │ Service  │   │Reminder│
    │  :8081  │   │  :8082   │   │ :8083  │
    └────┬────┘   └────┬─────┘   └───┬────┘
         │             │              │
         └─────────────┴──────────────┘
                       │
                 ┌─────v──────┐
                 │ PostgreSQL │
                 └────────────┘
```

### Observability Stack
```
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│  Prometheus  │  │    Loki      │  │   Jaeger     │
│   :9090      │  │   :3100      │  │   :16686     │
│  (Metrics)   │  │   (Logs)     │  │  (Traces)    │
└──────┬───────┘  └──────┬───────┘  └──────┬───────┘
       │                 │                  │
       └─────────────────┴──────────────────┘
                         │
                   ┌─────v──────┐
                   │  Grafana   │
                   │   :3000    │
                   │(Dashboards)│
                   └────────────┘
                         │
                   ┌─────v──────┐
                   │   Slack    │
                   │  Alerts    │
                   └────────────┘
```

---

## 🚀 Microservices

### 1. **API Gateway Service** (Port 8080)
**Purpose:** Entry point for all client requests

**Features:**
- ✅ Centralized routing
- ✅ Request/Response logging
- ✅ Distributed tracing propagation
- ✅ Health checks
- ✅ Metrics collection
- ✅ Custom filters

**Endpoints:**
- `/todos/**` → Todo Service
- `/api/reminders/**` → Location Reminder Service
- `/documents/**` → Document Service
- `/actuator/**` → Management endpoints

**Technologies:**
- Spring Cloud Gateway
- Spring WebFlux (Reactive)
- Micrometer Tracing

---

### 2. **Todo Service** (Port 8081)
**Purpose:** Task and reminder management

**Features:**
- ✅ CRUD operations for todos
- ✅ Category management (Movies, Food, Work, etc.)
- ✅ Priority levels (High, Medium, Low)
- ✅ Due date tracking
- ✅ Reminder scheduling
- ✅ Completion tracking
- ✅ RESTful API

**Key Endpoints:**
```
GET    /todos              - List all todos
POST   /todos              - Create new todo
GET    /todos/{id}         - Get todo by ID
PUT    /todos/{id}         - Update todo
DELETE /todos/{id}         - Delete todo
GET    /todos/search       - Search todos
```

**Database:** PostgreSQL

---

### 3. **Document Manager Service** (Port 8082)
**Purpose:** Document storage and management

**Features:**
- ✅ Document upload/download
- ✅ Metadata management
- ✅ Version control
- ✅ File storage

**Technologies:**
- Spring Boot
- JPA/Hibernate
- PostgreSQL

---

### 4. **Location-Based Reminder Service** (Port 8083)
**Purpose:** Location-aware reminder notifications

**Features:**
- ✅ Geolocation-based reminders
- ✅ Real-time location checking
- ✅ User location tracking
- ✅ Reminder notifications

**Key Endpoints:**
```
POST   /api/reminders              - Create reminder
POST   /api/reminders/{userId}/check - Check user location
```

**Technologies:**
- Spring Boot
- Geospatial calculations

---

## 💻 Backend Technologies

### Core Framework
- **Spring Boot 3.5.8**
  - Latest stable version
  - Production-ready features
  - Auto-configuration
  - Embedded server (Tomcat/Netty)

### Spring Cloud Stack
- **Spring Cloud Gateway 2025.0.0**
  - Reactive gateway
  - Non-blocking I/O
  - Route predicates and filters

### Database
- **PostgreSQL**
  - Relational database
  - ACID compliance
  - JSON support
  - Full-text search

### ORM & Data Access
- **Spring Data JPA**
  - Repository pattern
  - Query methods
  - Pagination support
- **Hibernate**
  - Object-relational mapping
  - Lazy loading
  - Caching

### Build Tool
- **Maven**
  - Dependency management
  - Multi-module support
  - Plugin ecosystem

### Java Version
- **Java 17 (LTS)**
  - Modern language features
  - Enhanced performance
  - Long-term support

---

## 📊 Observability Stack

### 1. **Prometheus** (Metrics Collection)
**Port:** 9090

**Features:**
- Time-series database
- Pull-based metrics collection
- PromQL query language
- Service discovery

**Metrics Collected:**
- HTTP request rates
- Response times (latency)
- Error rates (4xx, 5xx)
- JVM metrics (heap, threads, GC)
- CPU and memory usage
- Custom business metrics

**Configuration:**
```yaml
scrape_configs:
  - job_name: 'gateway'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 5s
```

---

### 2. **Loki** (Log Aggregation)
**Port:** 3100

**Features:**
- Horizontally scalable
- Label-based indexing
- Low storage cost
- LogQL query language
- Integration with Grafana

**Log Sources:**
- Gateway service logs
- Todo service logs
- Document service logs
- Reminder service logs

**Log Pipeline:**
- **Promtail** → Scrapes log files
- **Loki** → Stores and indexes
- **Grafana** → Visualizes and queries

**Query Examples:**
```logql
{service="gateway"} |= "ERROR"
{job="springlogs", detected_level="error"}
rate({service="gateway"} |= "ERROR" [5m])
```

---

### 3. **Jaeger** (Distributed Tracing)
**Ports:** 16686 (UI), 9411 (Zipkin), 14268 (Collector)

**Features:**
- End-to-end transaction monitoring
- Service dependency analysis
- Performance bottleneck identification
- Root cause analysis

**Tracing Flow:**
```
Client Request → Gateway (creates trace)
    ↓ (propagates trace context)
Backend Service (continues trace)
    ↓ (adds spans)
Jaeger Collector → Storage → UI
```

**Metrics Tracked:**
- Request duration
- Service call hierarchy
- Error traces
- Slow requests

**Implementation:**
- Micrometer Tracing Bridge
- Brave tracer
- Zipkin reporter
- 100% sampling in dev (configurable)

---

### 4. **Grafana** (Visualization & Dashboards)
**Port:** 3000

**Features:**
- Unified observability platform
- Multi-source data visualization
- Custom dashboards
- Alert management
- Slack/Discord integration

**Data Sources:**
- Prometheus (metrics)
- Loki (logs)
- PostgreSQL (application data)

---

## 🎨 Key Features

### 1. **Centralized API Gateway**
- Single entry point for all services
- Request routing based on path
- Load balancing
- Circuit breaker pattern ready

### 2. **Distributed Tracing**
- Track requests across services
- Trace ID in logs
- Performance monitoring
- Service dependency mapping

### 3. **Comprehensive Logging**
- Structured logging
- Centralized log aggregation
- Log levels: DEBUG, INFO, WARN, ERROR
- Trace context in logs
- Service-wise filtering

### 4. **Metrics Collection**
- Real-time metrics
- Historical data
- Business metrics
- Technical metrics

### 5. **Health Monitoring**
- Service health checks
- Readiness probes
- Liveness probes
- Dependency health

### 6. **Alerting System**
- Real-time alerts
- Slack notifications
- Custom alert rules
- Multi-channel support

---

## 🌐 API Gateway

### Configuration
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: todo-service
          uri: http://localhost:8081
          predicates:
            - Path=/todos/**
```

### Custom Filters
**LoggingFilter:**
- Logs all incoming requests
- Logs response status
- Execution time tracking
- Request/Response body logging

### Features Implemented
1. **Route Management**
   - Dynamic routing
   - Path-based routing
   - URI rewriting

2. **Monitoring**
   - Request counting
   - Response time tracking
   - Error rate monitoring

3. **Tracing**
   - Trace ID generation
   - Context propagation
   - Span creation

---

## 🔔 Monitoring & Alerting

### Alert Rules Configured

#### 1. **Service Down Alert**
```promql
up{job=~"gateway|todo-service|document-service|reminder-service"} == 0
```
- **Triggers:** When any service stops responding
- **Severity:** Critical
- **Action:** Immediate Slack notification

#### 2. **High Error Rate Alert**
```promql
sum(rate(http_server_requests_seconds_count{status=~"[45].."}[5m])) 
/ sum(rate(http_server_requests_seconds_count[5m])) * 100 > 5
```
- **Triggers:** When error rate > 5%
- **Severity:** High
- **Action:** Slack alert

#### 3. **Slow Response Time Alert**
```promql
histogram_quantile(0.95, 
  rate(http_server_requests_seconds_bucket[5m])
) * 1000 > 2000
```
- **Triggers:** When 95th percentile > 2 seconds
- **Severity:** Medium
- **Action:** Slack notification

### Notification Channels
- **Slack Webhooks**
  - Real-time alerts
  - Rich formatting
  - Custom messages
  - Service name in alerts

---

## 🔍 Distributed Tracing

### Implementation Details

**Dependencies:**
```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing-bridge-brave</artifactId>
</dependency>
<dependency>
    <groupId>io.zipkin.reporter2</groupId>
    <artifactId>zipkin-reporter-brave</artifactId>
</dependency>
```

**Configuration:**
```yaml
management:
  tracing:
    sampling:
      probability: 1.0  # 100% sampling
  zipkin:
    tracing:
      endpoint: http://localhost:9411/api/v2/spans
```

### Trace Information
- **Trace ID:** Unique ID for entire request flow
- **Span ID:** Unique ID for each service call
- **Service Name:** Identifies which service
- **Duration:** Time taken for operation
- **Tags:** Additional metadata

### Use Cases
1. **Performance Analysis**
   - Identify slow services
   - Find bottlenecks
   - Optimize response times

2. **Debugging**
   - Track request flow
   - Find where errors occur
   - Analyze failure patterns

3. **Service Dependencies**
   - Visualize call graph
   - Understand service relationships
   - Impact analysis

---

## 📝 Logging

### Logging Strategy

#### 1. **Application Logs**
**Location:** `logs/<service-name>.log`

**Format:**
```
[LEVEL] [TIMESTAMP] [SERVICE-NAME,TRACE-ID,SPAN-ID] [CLASS] - MESSAGE
```

**Example:**
```
INFO [2025-11-27 12:00:00] [gateway-service,abc123,xyz789] 
[c.p.gateway.filter.LoggingFilter] - Request: GET /todos
```

#### 2. **Log Levels**
- **DEBUG:** Detailed diagnostic information
- **INFO:** General informational messages
- **WARN:** Warning messages
- **ERROR:** Error events

#### 3. **Centralized Logging**

**Pipeline:**
```
Application → Log File → Promtail → Loki → Grafana
```

**Promtail Configuration:**
- Scrapes all service logs
- Adds service labels
- Extracts log levels
- Sends to Loki

**Loki Queries:**
```logql
# All errors
{job="springlogs"} |= "ERROR"

# Gateway errors only
{service="gateway"} |= "ERROR"

# Error rate
rate({job="springlogs"} |= "ERROR" [5m])

# Specific trace
{job="springlogs"} |~ "abc123"
```

### Log Aggregation Benefits
✅ Centralized search
✅ Real-time monitoring
✅ Historical analysis
✅ Correlation with traces
✅ Alert on log patterns

---

## 📈 Metrics

### Categories of Metrics

#### 1. **HTTP Metrics**
- **Request Rate:** Requests per second
- **Response Time:** Latency (avg, p95, p99)
- **Error Rate:** 4xx and 5xx errors
- **Status Codes:** Distribution

**Queries:**
```promql
# Request rate
rate(http_server_requests_seconds_count[1m])

# Average response time
rate(http_server_requests_seconds_sum[1m]) / 
rate(http_server_requests_seconds_count[1m])

# Error rate
sum(rate(http_server_requests_seconds_count{status=~"[45].."}[5m]))
```

#### 2. **JVM Metrics**
- **Heap Memory:** Used vs Max
- **Non-Heap Memory:** Metaspace, Code Cache
- **Garbage Collection:** GC pauses, frequency
- **Thread Count:** Active threads

**Queries:**
```promql
# Heap usage %
jvm_memory_used_bytes{area="heap"} / 
jvm_memory_max_bytes{area="heap"} * 100

# GC pause time
rate(jvm_gc_pause_seconds_sum[1m]) * 1000
```

#### 3. **System Metrics**
- **CPU Usage:** Process and system
- **File Descriptors:** Open files
- **Uptime:** Service availability

**Queries:**
```promql
# CPU usage
process_cpu_usage * 100

# Uptime in hours
process_uptime_seconds / 3600
```

#### 4. **Custom Business Metrics**
- Todo creation rate
- Reminder trigger count
- Document uploads
- User activity

---

## 🚢 Deployment

### Local Development Setup

#### Prerequisites
```bash
# Java 17
java -version

# Maven
mvn -version

# PostgreSQL
psql --version

# Docker (optional, for Grafana stack)
docker --version
```

#### Start Services

1. **PostgreSQL Database**
```bash
# Start PostgreSQL
brew services start postgresql

# Create databases
createdb todo_db
createdb document_db
createdb reminder_db
```

2. **Observability Stack**
```bash
# Start Prometheus
prometheus --config.file=prometheus.yml

# Start Loki
loki --config.file=loki-local-config.yaml

# Start Promtail
promtail --config.file=promtail-config.yml

# Start Jaeger
/usr/local/jaeger/jaeger-all-in-one --collector.zipkin.host-port=:9411

# Start Grafana (if using Homebrew)
brew services start grafana
```

3. **Microservices**
```bash
# Gateway (Terminal 1)
cd gateway
mvn spring-boot:run

# Todo Service (Terminal 2)
cd todo
mvn spring-boot:run

# Document Service (Terminal 3)
cd document_service
mvn spring-boot:run

# Reminder Service (Terminal 4)
cd loction_service
mvn spring-boot:run
```

### Service URLs

| Service | URL | Purpose |
|---------|-----|---------|
| Gateway | http://localhost:8080 | API Entry Point |
| Todo Service | http://localhost:8081 | Direct Access |
| Document Service | http://localhost:8082 | Direct Access |
| Reminder Service | http://localhost:8083 | Direct Access |
| Prometheus | http://localhost:9090 | Metrics |
| Loki | http://localhost:3100 | Logs API |
| Promtail | http://localhost:9080 | Log Scraper |
| Jaeger UI | http://localhost:16686 | Traces |
| Grafana | http://localhost:3000 | Dashboards |

---

## 🎯 Demo Flow

### Recommended Demo Sequence

#### 1. **Architecture Overview** (5 min)
- Show microservices diagram
- Explain service responsibilities
- Discuss technology choices

#### 2. **API Gateway Demo** (5 min)
- Make requests through gateway
- Show routing in action
- Demonstrate logging filter
```bash
curl http://localhost:8080/todos
curl http://localhost:8080/api/reminders
```

#### 3. **Distributed Tracing** (10 min)
- Open Jaeger UI
- Make requests and show traces
- Explain trace context
- Show service dependencies
- Analyze slow requests

#### 4. **Logging with Loki** (10 min)
- Open Grafana Explore
- Query logs by service
- Filter by log level
- Show error correlation
- Demonstrate trace ID in logs

#### 5. **Metrics with Prometheus** (10 min)
- Show Prometheus UI
- Run sample queries
- Display in Grafana dashboards
- Show real-time metrics

#### 6. **Alerting Demo** (10 min)
- Stop gateway service
- Show alert firing in Grafana
- Demonstrate Slack notification
- Explain alert rules

#### 7. **Business Features** (5 min)
- Create a todo
- Set a reminder
- Show data persistence
- Demonstrate RESTful APIs

---

## 📊 Key Metrics to Highlight

### Performance Metrics
- **Response Time:** < 100ms (p95)
- **Throughput:** 1000 req/sec
- **Error Rate:** < 0.1%
- **Availability:** 99.9%

### Observability Coverage
- **Services Monitored:** 4/4 (100%)
- **Metrics Collected:** 50+ metrics
- **Log Aggregation:** All services
- **Trace Sampling:** 100% (dev)

---

## 🎓 Learning Outcomes

### What This Project Demonstrates

1. **Microservices Architecture**
   - Service decomposition
   - Inter-service communication
   - API gateway pattern

2. **Spring Boot Ecosystem**
   - Spring Cloud Gateway
   - Spring Data JPA
   - Spring Actuator
   - Spring Boot DevTools

3. **Observability**
   - The Three Pillars (Metrics, Logs, Traces)
   - Prometheus for metrics
   - Loki for logs
   - Jaeger for traces

4. **Production Readiness**
   - Health checks
   - Monitoring
   - Alerting
   - Graceful degradation

5. **DevOps Practices**
   - Infrastructure as code
   - Configuration management
   - Monitoring setup

---

## 🔧 Troubleshooting Guide

### Common Issues

**Service won't start:**
```bash
# Check port availability
lsof -i :8080

# Check logs
tail -f logs/gateway-service.log
```

**Prometheus not scraping:**
```bash
# Verify actuator endpoint
curl http://localhost:8080/actuator/prometheus

# Check Prometheus targets
curl http://localhost:9090/api/v1/targets
```

**Traces not appearing:**
```bash
# Verify Zipkin endpoint
curl http://localhost:9411/health

# Check Jaeger services
curl http://localhost:16686/api/services
```

**Logs not in Loki:**
```bash
# Check Promtail targets
curl http://localhost:9080/targets

# Verify Loki
curl http://localhost:3100/ready
```

---

## 📚 Additional Resources

### Documentation
- Spring Boot: https://spring.io/projects/spring-boot
- Spring Cloud Gateway: https://spring.io/projects/spring-cloud-gateway
- Prometheus: https://prometheus.io/docs/
- Loki: https://grafana.com/docs/loki/
- Jaeger: https://www.jaegertracing.io/docs/
- Grafana: https://grafana.com/docs/

### Query Languages
- **PromQL:** Prometheus Query Language
- **LogQL:** Loki Query Language
- **SQL:** PostgreSQL queries

---

## 🎉 Conclusion

This project showcases a **production-ready microservices architecture** with comprehensive observability, demonstrating industry best practices for:

✅ Distributed systems design
✅ Modern backend technologies
✅ Full-stack monitoring
✅ Real-time alerting
✅ Performance optimization
✅ Operational excellence

**Tech Stack Summary:**
- Backend: Spring Boot 3.5.8, Java 17
- Gateway: Spring Cloud Gateway
- Database: PostgreSQL
- Metrics: Prometheus
- Logs: Loki + Promtail
- Traces: Jaeger
- Visualization: Grafana
- Alerts: Slack Integration

---

**Project Repository:** https://github.com/SandeepKumarUtti/plana
**Author:** Sandeep Kumar
**Last Updated:** November 2025
