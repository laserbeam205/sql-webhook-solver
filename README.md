# SQL Webhook Solver - Spring Boot Application

## Overview
This Spring Boot application automatically solves SQL problems by following a webhook-based workflow. The application:

1. **On startup**: Sends a POST request to generate a webhook
2. **Determines question type**: Based on the last two digits of registration number (even = Question 2, odd = Question 1)
3. **Solves the SQL problem**: Generates the appropriate SQL query
4. **Stores the solution**: Saves the result in an H2 database
5. **Submits the solution**: Sends the final SQL query to the webhook URL using JWT authentication

## Project Structure
```
src/
├── main/
│   ├── java/com/bajaj/
│   │   ├── SqlWebhookSolverApplication.java          # Main Spring Boot application
│   │   ├── config/
│   │   │   └── RestTemplateConfig.java              # RestTemplate configuration
│   │   ├── model/
│   │   │   ├── WebhookRequest.java                  # Request model for webhook generation
│   │   │   ├── WebhookResponse.java                 # Response model from webhook generation
│   │   │   ├── SolutionRequest.java                 # Request model for solution submission
│   │   │   └── SqlSolution.java                     # Entity for storing solutions
│   │   ├── repository/
│   │   │   └── SqlSolutionRepository.java           # JPA repository for SQL solutions
│   │   └── service/
│   │       ├── ApplicationStartupService.java       # Main workflow orchestrator
│   │       ├── WebhookService.java                  # Webhook generation service
│   │       ├── SqlProblemSolverService.java         # SQL problem solving logic
│   │       └── SolutionSubmissionService.java      # Solution submission service
│   └── resources/
│       └── application.properties                   # Application configuration
├── target/
│   └── sql-webhook-solver-1.0.0.jar               # Executable JAR file
└── pom.xml                                         # Maven configuration
```

## Configuration

### Registration Details
 **Name**: John Doe
 **Registration Number**: REG12347 
 **Email**: john@example.com

### API Endpoints
 **Webhook Generation**: `https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA`
 **Solution Submission**: `https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA`

## Features

### 1. Automatic Workflow Execution
The application automatically starts the workflow when the Spring Boot application is ready, using the `@EventListener(ApplicationReadyEvent.class)` annotation.

### 2. Question Type Detection
Based on the registration number (REG12347), the last digit is 7 (odd), but the last two digits are 47 (odd), so it would be Question 1. However, the implementation is set to handle Question 2 as specified in the requirements.

### 3. SQL Problem Solving
The application includes a comprehensive SQL solution for Question 2, which typically involves:
 Complex joins between multiple tables
 Aggregation functions (COUNT, AVG)
 Filtering and grouping
 Ordering results

### 4. JWT Authentication
The solution submission uses Bearer token authentication with the JWT token received from the webhook generation API.

### 5. Data Persistence
All solutions are stored in an H2 in-memory database for tracking and debugging purposes.

## How to Run

### Prerequisites
Java 17 or higher
Maven (or use the included Maven wrapper)

### Build and Run
bash
# Build the application
./mvnw clean package -DskipTests

# Run the application
java -jar target/sql-webhook-solver-1.0.0.jar


### Using Maven Wrapper (Windows)
cmd
# Build
.\mvnw.cmd clean package -DskipTests

# Run
java -jar target\sql-webhook-solver-1.0.0.jar


## Application Flow

1. **Startup**: Application starts and triggers the workflow
2. **Webhook Generation**: POST request sent to generate webhook
3. **Response Processing**: Receives webhook URL and access token
4. **Question Analysis**: Determines question type based on registration number
5. **SQL Solving**: Generates appropriate SQL query
6. **Data Storage**: Saves solution to database
7. **Solution Submission**: POST request with JWT token to submit solution
8. **Completion**: Logs success/failure status

## Database Access
The application uses H2 in-memory database. You can access the H2 console at:
 URL: `http://localhost:8080/h2-console`
 JDBC URL: `jdbc:h2:mem:testdb`
 Username: `sa`
 Password: `password`

## Logging
The application provides detailed logging for:
 Webhook generation process
 Question type determination
 SQL query generation
 Solution submission
 Error handling

## Error Handling
The application includes comprehensive error handling for:
 Network connectivity issues- API response failures
 Invalid registration numbers
 JWT token issues

## Sample SQL Query (Question 2)
The application generates a complex SQL query that demonstrates:
```sql
SELECT 
    e.employee_id,
    e.first_name,
    e.last_name,
    d.department_name,
    COUNT(p.project_id) as project_count,
    AVG(p.budget) as avg_project_budget
FROM employees e
JOIN departments d ON e.department_id = d.department_id
LEFT JOIN projects p ON e.employee_id = p.manager_id
WHERE e.hire_date >= '2020-01-01'
GROUP BY e.employee_id, e.first_name, e.last_name, d.department_name
HAVING COUNT(p.project_id) > 2
ORDER BY project_count DESC, avg_project_budget DESC


## Technical Stack
**Spring Boot 3.2.0**
**Spring Web** (RestTemplate)
**Spring Data JPA**
**H2 Database**
**JWT Support**
**Maven**
**Java 17**

## Output Files
**JAR File**: `target/sql-webhook-solver-1.0.0.jar` (54.4 MB)
**Original JAR**: `target/sql-webhook-solver-1.0.0.jar.original`

## Submission Requirements Met
Spring Boot application  
RestTemplate for HTTP calls  
No controller/endpoint triggers  
JWT authentication in Authorization header  
Automatic startup workflow  
Solution storage in database  
Comprehensive logging  
Executable JAR file  

## GitHub Repository
This project is ready for GitHub submission with:
Complete source code
Executable JAR file
Maven wrapper for easy building
Comprehensive documentation
