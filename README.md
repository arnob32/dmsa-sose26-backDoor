
# Scenario 3: Citizen-based Road Condition Monitoring

# Objective:
The aim is to develop a software platform for road condition monitoring that provides a costeffective and user-friendly way to monitor and improve the condition of road infrastructure.
The platform will enable citizens to quickly and easily report the condition of roads, allowing
potential problems to be identified and rectified more quickly. By involving citizens in the
monitoring process, it aims to increase community engagement and participation,
contributing to improved road safety and road conditions.

# Basic Requirements: 

User registration: The platform must allow users to register and create an account so
that they can submit reports and provide feedback.

Submission of reports: Users should be able to submit reports on the condition of road
infrastructure by taking a photo and adding a description of the condition and problem.

Location data: The platform needs to capture the exact location data of reports to
enable quick and effective resolution of reported issues.
Categorization of reports: Users should be able to classify their reports into different
categories, e.g. potholes, broken traffic lights, missing signage, etc.

Notifications: The platform should automatically send notifications to the relevant Monitoring
authorities as soon as a report is submitted.

Processing of reports: Responsible authorities must be able to review and process
reports to ensure they are correctly categorized and prioritized.

Status update: The platform should allow users to view the status of their reported
issues, e.g. whether the issue has been resolved or whether it is still in progress.

Feedback function: Users should be able to provide feedback on the reported problems
and track the progress of their resolution.

Analysis function: The platform should enable statistical analysis of reported problems
and their resolution in order to identify trends and bottlenecks and develop more
effective strategies to improve road conditions.

Security and privacy: The platform must ensure that users' data and the reports they
submit are secure and protected and comply with applicable data protection regulations.




##  the platform plan to implemented as a **microservice architecture** using **Java** and the **Spring framework**. The following patterns and technologies are used:

- **Microservice architecture** — the system is decomposed into independently deployable services
- **API Gateway pattern** — a single entry point routes requests to downstream services
- **Domain-Driven Design (DDD)** — bounded contexts define service boundaries
- **RESTful APIs** — synchronous HTTP communication between services and clients
- **Spring Boot** — application framework for each microservice
- **Spring Security + JWT** — authentication and authorisation
- **Spring Data JPA + PostgreSQL** — persistent storage per service
- **Docker + Docker Compose** — containerisation and local orchestration
- **Spring Cloud (Eureka, Gateway)** — service discovery and API gateway


# Services & Ports

| Module | Bounded Context | Port | Status |
|---|---|---|---|
| `config-server` | Centralized configuration (Spring Cloud Config) | 8888 | Implemented |
| `eureka-server` | Service discovery (Eureka) | 8761 | Implemented |
| `User_Service` | Identity & Access Management | 8082 | Implemented |
| `Dispatch_Service` | Maintenance Dispatch | 8081 | Implemented |
| `IssueReport_Service` | Issue Reporting | — | Scaffold |
| `Notification_Service` | Notification | — | Scaffold |
| `FeedBack_Service` | FeedBack | — | Scaffold |
| `Media_Service` | Media / Photo Management | — | Scaffold |

# Running the System

Start each module with `mvn spring-boot:run` in this order (infrastructure first):

1. **config-server** (`config-server/`) → http://localhost:8888
2. **eureka-server** (`eureka-server/eureka-server/`) → http://localhost:8761
3. **business services** (e.g. `Dispatch_Service/`, `User_Service/`)

Services can also run standalone: the config import is `optional:`, and the Eureka
client can be turned off via `eureka.client.enabled=false`.

# Dispatch_Service (Maintenance Dispatch)

Implements the Maintenance Dispatch context: receives reports handed over from Issue
Reporting, then reviews, prioritises, assigns work, tracks status history and resolves
or rejects them. Built with Spring Boot + Spring MVC + REST and an H2 database.

- **DDD building blocks:** `Report` (aggregate root), `WorkAssignment` & `StatusHistory`
  (entities), `ReportStatus` / `Priority` / `AssignmentStatus` (value objects),
  `DispatchService` (domain service), three repositories, `RestClient` (publisher).
- **Integration (Assignment 5 Task 2):** registers with **Eureka**; calls `user-service`
  for technicians through a **Resilience4j circuit breaker** (with fallback); pulls
  centralized config from the **config-server**.
- **Demo UI:** http://localhost:8081/

Key REST endpoints (base `/api`):

| Method | Path | Purpose |
|---|---|---|
| GET | `/reports`, `/reports/open`, `/reports/{id}` | List / read reports |
| POST | `/reports` | Ingest a report (mock hand-over from Issue Reporting) |
| POST | `/reports/{id}/priority` | Set priority |
| POST | `/reports/{id}/assign` | Assign work (→ IN_PROGRESS) |
| POST | `/reports/{id}/status` | Change status |
| POST | `/reports/{id}/resolve`, `/reports/{id}/reject` | Close report |
| GET | `/reports/{id}/assignments`, `/reports/{id}/history` | Work assignments / status audit trail |
| GET | `/technicians` | Technician list from user-service (circuit-breaker guarded) |
| GET | `/info` | Shows config loaded from the config-server |




# For funcitional requirementam and Diagrams follow this links: https://github.com/arnob32/SIS-2026/wiki

# Team Descriptions:  backDoor!

  1.Raju Naidu - 7213668
  
  2.Junaid Ahmed - 7222074
  
  3.Helmi Zaki Fadhali - 7225540
  
  4.Nawshad Fahim - 7216629
 

 # Dortmund University Of Applied Science
 #  Course :  Design and Modelling of complex Software Architecture (SOSE26)
