# Mutual Fund Transaction System

A backend application built using Spring Boot that simulates real-world mutual fund operations such as **buying units, selling units, and tracking portfolio holdings**.
The system is designed with a focus on **financial correctness, data consistency, idempotent APIs, audit logging, and secure authentication using JWT**.

---

## 🚀 Features

* Buy mutual fund units based on NAV
* Sell mutual fund units with proper validation
* Portfolio tracking with real-time valuation
* JWT-based authentication & authorization
* Idempotent transaction handling to prevent duplicate requests
* Audit logging for tracking user actions
* Transaction management using `@Transactional`
* RESTful API design with proper status codes

---

## 🛠️ Tech Stack

* Java
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Spring Security (JWT Authentication)
* Swagger (API Testing UI)

---

## 📌 Prerequisites

* Java 8 or above
* Maven
* PostgreSQL
* Postman or Swagger UI

---

## ⚙️ Setup Instructions

### 1. Clone Repository

```bash
git clone https://github.com/your-username/mutual-fund-transaction-system.git
cd mutual-fund-transaction-system
```

---

## 🗄️ Database Setup

### Step 1: Create Database

```sql
CREATE DATABASE mutual_fund_db;
```

---

### Step 2: Run Schema Script

Execute your database script (`schema.sql`) which creates:

* users
* funds
* transactions
* portfolio
* audit_logs

---

### Step 3: Configure Database

Update `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mutual_fund_db
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

## ▶️ Run Application

```bash
mvn spring-boot:run
```

---

## 🔐 Authentication Flow (JWT)

### Step 1: Register User

```http
POST /api/auth/register
```

```json
{
  "name": "Rohit Pawar",
  "email": "rohit@gmail.com",
  "password": "password123"
}
```

---

### Step 2: Login

```http
POST /api/auth/login
```

```json
{
  "email": "rohit@gmail.com",
  "password": "password123"
}
```

---

### Step 3: Get JWT Token

Response:

```json
{
  "token": "your_jwt_token_here"
}
```

---

### Step 4: Use Token

Add this header in Postman / Swagger:

```http
Authorization: Bearer <your_token>
```

---

## 📊 Swagger UI

Access Swagger:

```
http://localhost:8080/swagger-ui/index.html
```

👉 Use **Authorize button** to add JWT token

---

## 📊 Sample Data & Execution Flow

Follow this exact sequence to test the application:

---

### 🔹 Step 1: Insert Fund Data (MANDATORY)

```sql
INSERT INTO funds (fund_name, nav, last_updated)
VALUES 
('HDFC Equity Fund', 50.25, NOW()),
('ICICI Bluechip Fund', 42.75, NOW()),
('SBI Small Cap Fund', 65.10, NOW());
```

👉 Funds must exist before BUY or SELL operations.

---

### 🔹 Step 2: Register User

```http
POST /api/auth/register
```

```json
{
  "name": "Rohit Pawar",
  "email": "rohit@gmail.com",
  "password": "password123"
}
```

---

### 🔹 Step 3: Login & Get Token

```http
POST /api/auth/login
```

---

### 🔹 Step 4: Add Token in Header

```http
Authorization: Bearer <your_token>
```

---

### 🔹 Step 5: Test APIs

#### ✅ Buy Units

```http
POST /api/buy
```

```json
{
  "fundId": 1,
  "amount": 1000,
  "idempotencyKey": "buy-001"
}
```

---

#### ✅ Sell Units

```http
POST /api/sell
```

```json
{
  "fundId": 1,
  "units": 5,
  "idempotencyKey": "sell-001"
}
```

---

#### ✅ Get Portfolio

```http
GET /api/portfolio
```

---

## 📦 API Summary

| API                | Method | Description     |
| ------------------ | ------ | --------------- |
| /api/auth/register | POST   | Register user   |
| /api/auth/login    | POST   | Login & get JWT |
| /api/buy           | POST   | Buy fund units  |
| /api/sell          | POST   | Sell fund units |
| /api/portfolio     | GET    | View holdings   |

---

## 🧾 Audit Logging

All BUY and SELL operations are recorded in `audit_logs` table:

* user_id
* action (BUY / SELL)
* request_data (stored as JSON)
* timestamp

---

## 📊 Database Overview

The system uses the following tables:

* **users** → user authentication
* **funds** → NAV and fund data
* **transactions** → buy/sell records
* **portfolio** → aggregated holdings
* **audit_logs** → activity tracking

The schema is designed to ensure:

* Financial accuracy
* Data consistency
* Idempotent transaction processing

---

## ⚠️ Important Guidelines

* Funds must be inserted before transactions
* User must be authenticated (JWT required)
* Always use unique idempotency keys
* Financial calculations use BigDecimal for precision

---

