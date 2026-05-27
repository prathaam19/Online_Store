# 🍔 FoodMart - Full Stack E-Commerce Backend Application

FoodMart is a Spring Boot based e-commerce backend application that provides secure REST APIs for authentication, product management, cart operations, order processing, and online payments.

The project follows a layered architecture using Spring Boot, Spring Security, JWT Authentication, JPA/Hibernate, and MySQL.

---

# 🚀 Features

## 🔐 Authentication & Security

* User Registration & Login
* JWT-based Authentication
* Role-Based Authorization
* BCrypt Password Encryption
* Secure REST APIs with Spring Security

## 🛍 Product Management

* Add / Update / Delete Products
* Product Search & Filtering
* Product Categories
* Product Image Upload
* Pagination & Sorting

## 🛒 Cart & Order Management

* Add Products to Cart
* Update Product Quantity
* Remove Items from Cart
* Place Orders
* View Order History

## 💳 Payment Integration

* Razorpay Payment Gateway Integration
* Payment Verification
* Secure Payment Flow

## 📧 Email Service

* Registration Email Notifications
* SMTP Mail Integration

## 🗄 Database & Persistence

* MySQL Database Integration
* Spring Data JPA / Hibernate ORM
* Entity Relationships & Mappings

---

# 🛠 Tech Stack

| Technology      | Purpose                        |
| --------------- | ------------------------------ |
| Java 17         | Programming Language           |
| Spring Boot     | Backend Framework              |
| Spring Security | Authentication & Authorization |
| JWT             | Secure Token Authentication    |
| Spring Data JPA | ORM Layer                      |
| Hibernate       | Database Mapping               |
| MySQL           | Relational Database            |
| Maven           | Dependency Management          |
| Razorpay        | Payment Gateway                |
| Swagger/OpenAPI | API Documentation              |
| Lombok          | Boilerplate Code Reduction     |

---

# 📂 Project Architecture

The project follows a layered backend architecture:

```text
Controller
   ↓
Service
   ↓
Repository
   ↓
Database
```

## 📁 Package Structure

```text
src/main/java/com/foodmart/food
│
├── controller
├── service
├── repository
├── entity
├── dto
├── config
├── security
├── exception
└── util
```

---

# ⚙️ Prerequisites

Before running the project, make sure you have:

* Java 17+
* Maven 3.6+
* MySQL 8+
* Git

---

# 🗄 Database Setup

Login to MySQL:

```bash
mysql -u root -p
```

Create database:

```sql
CREATE DATABASE foodmart;
```

---

# 🔧 Configure Application

Update the `application.properties` file:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/foodmart
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update

jwt.secret=YourSecretKey
jwt.expiration-ms=3600000
```

---

# 📦 Build Project

```bash
mvn clean install
```

---

# ▶️ Run Application

```bash
mvn spring-boot:run
```

OR

```bash
java -jar target/food-0.0.1-SNAPSHOT.jar
```

Application will start on:

```text
http://localhost:8080
```

---

# 📘 API Documentation

After starting the application:

## Swagger UI

```text
http://localhost:8080/swagger-ui.html
```

## OpenAPI Docs

```text
http://localhost:8080/v3/api-docs
```

---

# 🔐 Authentication Flow

```text
User Login/Register
        ↓
JWT Token Generated
        ↓
Frontend Stores Token
        ↓
Token Sent in Request Header
        ↓
JWT Validation
        ↓
Authorized API Access
```

---

# 📌 Important APIs

## Authentication APIs

| Method | Endpoint             | Description   |
| ------ | -------------------- | ------------- |
| POST   | `/api/auth/register` | Register User |
| POST   | `/api/auth/login`    | Login User    |

## Product APIs

| Method | Endpoint             | Description      |
| ------ | -------------------- | ---------------- |
| GET    | `/api/products/get`  | Get All Products |
| POST   | `/api/products/add`  | Add Product      |
| PUT    | `/api/products/{id}` | Update Product   |
| DELETE | `/api/products/{id}` | Delete Product   |

## Cart APIs

| Method | Endpoint             | Description |
| ------ | -------------------- | ----------- |
| POST   | `/api/cart/add/{id}` | Add to Cart |
| GET    | `/api/cart`          | View Cart   |

## Order APIs

| Method | Endpoint      | Description |
| ------ | ------------- | ----------- |
| POST   | `/api/orders` | Place Order |
| GET    | `/api/orders` | View Orders |

---

# 🧪 Sample Authentication Request

## Register User

```json
POST /api/auth/register

{
  "username": "john123",
  "email": "john@example.com",
  "password": "password123"
}
```

## Login Response

```json
{
  "token": "jwt_token_here"
}
```

---

# 🗃 Database Tables

The following tables are automatically generated:

* users
* roles
* user_roles
* products
* categories
* carts
* cart_products
* orders
* order_items
* addresses

---

# 🧩 Key Backend Concepts Implemented

* RESTful API Development
* Layered Architecture
* JWT Authentication
* Role-Based Access Control
* Exception Handling
* Validation
* Dependency Injection
* DTO Pattern
* ORM Mapping
* Transaction Management
* Pagination & Sorting
* Secure Password Hashing

---

# ⚠️ Common Issues & Fixes

## MySQL Connection Error

```text
Unable to acquire JDBC Connection
```

### Solution

* Verify MySQL service is running
* Check username/password
* Verify database exists

---

## Unauthorized Error (401)

```text
Invalid or Missing JWT Token
```

### Solution

Add JWT token in request header:

```text
Authorization: Bearer your_token
```

---

## Maven Dependency Error

```text
package does not exist
```

### Solution

```bash
mvn clean install
```

or reload Maven dependencies in IntelliJ.

---

# ✅ Verification Checklist

After running the application, verify:

* Application starts successfully
* Swagger UI loads correctly
* User registration works
* Login returns JWT token
* Products can be added
* Cart operations work
* Orders can be placed
* Payment integration works

---

# 📚 Learning Outcomes

This project helped in understanding:

* Spring Boot Backend Development
* Secure API Development
* JWT Authentication Flow
* Database Design & ORM
* REST API Architecture
* Full Stack Backend Workflow
* Payment Gateway Integration

---

# 👨‍💻 Author

Developed as a Full Stack Backend Learning Project using Spring Boot and MySQL.

---
