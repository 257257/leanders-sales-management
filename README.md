" Leander's Sales Management System "

A RESTful backend application built with Java and Spring Boot to manage customers, products, sales transactions, and sales reporting.

1. Project Overview

Leander's Sales Management System provides REST APIs for managing business data and sales transactions. It supports product data uploads, date-range sales reports, and PDF report generation.

The application uses MySQL for data persistence and Flyway for database schema migrations. Swagger UI provides interactive API documentation, while JUnit and SpotBugs support automated testing and static code analysis.

2. Key Features

- Customer management REST APIs
- Product management REST APIs
- Sales transaction management
- Product file upload using pipe-delimited data
- Sales report retrieval by date range
- PDF sales report generation and download
- MySQL database integration
- Database schema migrations using Flyway
- Interactive API documentation using Swagger UI
- Automated unit tests using JUnit
- Static code analysis using SpotBugs
- Request logging

3. Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Spring Boot | Backend application framework |
| Spring Web | REST API development |
| Spring Data JPA / Hibernate | Database access and ORM |
| MySQL | Relational database |
| Flyway | Database schema versioning and migrations |
| Maven | Build and dependency management |
| JasperReports | PDF sales report generation |
| Swagger / OpenAPI | Interactive API documentation |
| JUnit | Automated testing |
| SpotBugs | Static code analysis |

4. Prerequisites

Ensure the following are installed:

- Java Development Kit (JDK) 17
- Apache Maven
- MySQL Server
- Git (optional, for version control)

5. Database Configuration

Create the database

Create the application database in MySQL:

sql
CREATE DATABASE leanders_sales_management;


### Configure the application

Update `src/main/resources/application.properties` with your local database credentials:

`properties
spring.application.name=leanders-sales-management

spring.datasource.url=jdbc:mysql://localhost:3306/leanders_sales_management
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:root}

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.open-in-view=false

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
```

Replace the example credentials with your own local MySQL credentials. For environments outside local development, configure credentials through environment variables rather than committing passwords to version control.

Flyway applies the versioned database migrations from `src/main/resources/db/migration` when the application starts.

6. How to Run the Application

# Clone the repository

If the project is hosted on GitHub:

```bash
git clone <your-github-repository-url>
cd leanders-sales-management
```

### Build the application

```bash
mvn clean package
```

### Run the application

```bash
mvn spring-boot:run
```

The application runs at:

`http://localhost:8080`

Alternatively, import the project into Eclipse as an existing Maven project and run the Spring Boot application from the IDE.

7. API Documentation

### Base URL

`http://localhost:8080`

### Swagger UI

**Interactive API documentation:**

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

Start the application and open Swagger UI in a browser to inspect available API operations, review parameters and request schemas, and execute requests.

7.1 Customer APIs

| HTTP Method | Endpoint | Description |
|---|---|---|
| GET | `/api/customers` | Retrieve all customers |
| GET | `/api/customers/{id}` | Retrieve a customer by ID |
| POST | `/api/customers` | Create a customer |
| PUT | `/api/customers/{id}` | Update a customer |

7.2 Product APIs

| HTTP Method | Endpoint | Description |
|---|---|---|
| GET | `/api/products` | Retrieve all products |
| GET | `/api/products/{id}` | Retrieve a product by ID |
| POST | `/api/products` | Create a product |
| PUT | `/api/products/{id}` | Update a product |
| PATCH | `/api/products/{id}` | Partially update a product |
| POST | `/api/products/upload` | Upload product data using a pipe-delimited file |

7.3 Sales APIs

| HTTP Method | Endpoint | Description |
|---|---|---|
| POST | `/api/sales` | Create a sale |
| GET | `/api/sales` | Retrieve all sales |
| GET | `/api/sales/{id}` | Retrieve a sale by ID |
| GET | `/api/sales/customer/{customerId}` | Retrieve sales for a customer |
| GET | `/api/sales/product/{productId}` | Retrieve sales for a product |
| GET | `/api/sales/report` | Retrieve sales within a date range in JSON format |
| GET | `/api/sales/report/pdf` | Generate and download a sales report in PDF format |

For both reporting endpoints, provide the `startDate` and `endDate` query parameters in ISO date-time format.

Example:

`/api/sales/report?startDate=2026-10-21T00:00:00&endDate=2026-10-21T23:59:59`

8. Example API Requests

### Create a sale

**Endpoint:** `POST /api/sales`

**Content-Type:** `application/json`

Request body:

```json
{
  "customerId": 1,
  "productId": 1,
  "quantity": 2
}
```

The customer and product IDs must refer to existing records.

### Retrieve sales by date range

**Endpoint:** `GET /api/sales/report`

Example request:

```text
http://localhost:8080/api/sales/report?startDate=2026-10-21T00:00:00&endDate=2026-10-21T23:59:59
```

The response contains sales matching the requested date range.

### Download the PDF sales report

**Endpoint:** `GET /api/sales/report/pdf`

Example request:

```text
http://localhost:8080/api/sales/report/pdf?startDate=2026-10-21T00:00:00&endDate=2026-10-21T23:59:59
```

The endpoint returns a PDF attachment named `sales-report.pdf`.

### Upload product data

**Endpoint:** `POST /api/products/upload`

Upload the pipe-delimited product file using the multipart form-data field expected by the endpoint. Refer to Swagger UI for the exact request schema and required field name.

9. Testing

Run the automated test suite:

```bash
mvn test
```

To clean, compile, and run tests as part of the package build:

```bash
mvn clean package
```

Review the Maven console output to confirm the tests pass without failures or errors.

10. Code Quality Analysis

This project uses SpotBugs for static code analysis.

Run the SpotBugs check:

```bash
mvn spotbugs:check
```

The check should finish with `BUILD SUCCESS` and report no findings under the configured SpotBugs rules.

11. Project Structure

```text
leanders-sales-management/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/leander/salesmanagement/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── entity/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       └── LeandersSalesManagementApplication.java
│   │   └── resources/
│   │       ├── db/migration/
│   │       ├── reports/
│   │       └── application.properties
│   └── test/
│       └── java/
├── pom.xml
└── README.md
```

12. Notes for Reviewers

1. Ensure MySQL is running and the database credentials are configured before starting the application.
2. Start the application before accessing Swagger UI.
3. Use Swagger UI to explore and test the REST APIs interactively.
4. Create or verify the required customer and product records before testing sales creation.
5. Use valid ISO date-time parameters when testing sales reports.
6. Run the JUnit test suite and SpotBugs check before submitting changes.

13. Future Enhancements

Potential improvements include automated CI/CD using GitHub Actions, authentication and authorization, and deployment to a cloud environment.

---

**Project:** Leander's Sales Management System  
**Purpose:** Customer, product, sales transaction, and reporting management  
**API Documentation:** [Swagger UI](http://localhost:8080/swagger-ui/index.html) (available when the application is running locally)