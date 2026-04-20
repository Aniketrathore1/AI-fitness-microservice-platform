# AI-Based Fitness Microservices Platform

## Architecture Overview

```
Client
  └─► API Gateway (port 8080)
        ├─► UserService    (port 8081) → PostgreSQL
        ├─► ActivityService (port 8082) → MongoDB + Kafka Producer
        └─► AIService      (port 8083) → MongoDB + Kafka Consumer + Gemini API
                                    ↑
                              Kafka (port 9092)

All services register with Eureka Server (port 8761)
```

## Flow: How it works

1. User **registers** via UserService → saved to PostgreSQL
2. User **logs an activity** via ActivityService → saved to MongoDB
3. ActivityService **publishes** the activity to Kafka topic `activity-logged`
4. AIService **consumes** the Kafka event
5. AIService calls **Google Gemini API** to generate a fitness recommendation
6. Recommendation is **saved to MongoDB** in AIService
7. User can **fetch recommendations** via AIService REST API

---

## Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL running on port 5432
- MongoDB running on port 27017
- Apache Kafka running on port 9092
- Google Gemini API key (free at https://aistudio.google.com/app/apikey)

---

## Database Setup

### PostgreSQL
```sql
CREATE DATABASE fitnessusers;
```

### MongoDB
MongoDB creates databases automatically when data is first inserted.
The two databases used are:
- `fitnessactivities` (ActivityService)
- `airecommendationfitness` (AIService)

### Kafka
```bash
# Start Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# Start Kafka
bin/kafka-server-start.sh config/server.properties

# Create the topic (optional - auto-created on first use)
bin/kafka-topics.sh --create --topic activity-logged --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

---

## Add Your Gemini API Key

Edit `aiservice/src/main/resources/application.yml`:
```yaml
gemini:
  api:
    key: YOUR_GEMINI_API_KEY_HERE   # <-- Replace this
```

---

## Start Order (important!)

Start services in this order:
1. **Eureka Server** → `cd eureka-server && mvn spring-boot:run`
2. **UserService**   → `cd userservice && mvn spring-boot:run`
3. **ActivityService** → `cd activityservice && mvn spring-boot:run`
4. **AIService**     → `cd aiservice && mvn spring-boot:run`
5. **API Gateway**   → `cd api-gateway && mvn spring-boot:run`

---

## API Endpoints (all via API Gateway on port 8080)

### User Service

| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/users/register` | Register a new user |
| GET  | `/api/users/{userId}` | Get user profile |

**Register Request Body:**
```json
{
  "name": "Raj Kumar",
  "email": "raj@example.com",
  "password": "secret123",
  "age": 25,
  "fitnessGoal": "weight_loss",
  "fitnessLevel": "beginner"
}
```

---

### Activity Service

| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/activities` | Log a new activity (triggers AI recommendation) |
| GET  | `/api/activities/user/{userId}` | Get all activities for a user |
| GET  | `/api/activities/{activityId}` | Get a single activity |

**Log Activity Request Body:**
```json
{
  "userId": "YOUR_USER_ID",
  "type": "RUNNING",
  "duration": 30,
  "caloriesBurned": 250,
  "notes": "Morning jog in the park"
}
```

Supported activity types: `RUNNING`, `CYCLING`, `STRENGTH_TRAINING`, `YOGA`, `SWIMMING`, etc.

---

### AI Service

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/api/ai/recommendations/user/{userId}` | Get all AI recommendations for a user |
| GET | `/api/ai/recommendations/activity/{activityId}` | Get recommendation for one activity |

**Sample AI Recommendation Response:**
```json
{
  "id": "...",
  "userId": "...",
  "activityId": "...",
  "activityType": "RUNNING",
  "activityDuration": 30,
  "recommendation": "Great job on your 30-minute run! Make sure to stretch your calves and quads to prevent soreness. Hydrate well with at least 500ml of water post-run. For your next session, try adding 5 minutes to gradually build endurance.",
  "status": "SUCCESS",
  "createdAt": "2024-01-15T10:30:00"
}
```

---

## Eureka Dashboard

Once all services are running, visit: http://localhost:8761

You should see all 4 services registered:
- USERSERVICE
- ACTIVITYSERVICE
- AISERVICE
- API-GATEWAY

---

## Tech Stack Summary

| Technology | Purpose |
|-----------|---------|
| Spring Boot 3.2 | Microservice framework |
| Spring Cloud Eureka | Service discovery & registration |
| Spring Cloud Gateway | API Gateway & routing |
| Apache Kafka | Async event-driven communication |
| PostgreSQL | User data storage (structured/relational) |
| MongoDB | Activity & AI recommendation storage (flexible/document) |
| Google Gemini API | AI-powered fitness recommendations |
| WebClient | Non-blocking HTTP calls between services |
| Lombok | Reduces boilerplate code |
