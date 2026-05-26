# Foodmart Application

A simple Spring Boot food ordering service built with Java 17 and Spring Boot 3.2.4.

## What changed

- Removed duplicate `com.razorpay:razorpay-java` dependency from `pom.xml`.
- Fixed ambiguity in `PaymentServiceImpl.java` between the application `Order` entity and `com.razorpay.Order`.
- Simplified test setup in `CartServiceIntegrationTest.java` so existing `ROLE_CUSTOMER` records are reused instead of duplicated.

## Prerequisites

- Java 17 SDK installed
- Git
- Internet access to download Maven dependencies
- MySQL running locally for application execution (tests run on in-memory H2 database)

## Setup

1. Clone the repository:

```bash
git clone <repo-url> food
cd food
```

2. Verify Java and Maven wrapper:

```bash
java -version
./mvnw.cmd -version
```

3. Create the MySQL database used by the app (or update `src/main/resources/application.properties`):

```sql
CREATE DATABASE foodmart;
```

4. If you need a different database user or password, update `src/main/resources/application.properties` accordingly.

## Run the application

From the project root:

```bash
./mvnw.cmd spring-boot:run
```

The application will start on `http://localhost:8080` by default.

## Run tests

The project includes integration tests that use the in-memory H2 database.

```bash
./mvnw.cmd clean test
```

## Configuration notes

- Database configuration is in `src/main/resources/application.properties`.
- Test database configuration is in `src/test/resources/application.properties`.
- JWT secret, email, and Razorpay configuration are also stored in `src/main/resources/application.properties`.

### Razorpay

The application currently includes demo Razorpay test credentials in `application.properties`.
You can override them with your own values by editing:

- `razorpay.key-id`
- `razorpay.key-secret`
- `razorpay.api.key`
- `razorpay.api.secret`

### Email

Gmail SMTP is configured by default. Update `spring.mail.username` and `spring.mail.password` with valid credentials, or change the mail provider settings as needed.

### User roles and registration codes

The app includes invite-code based role registration:

- `app.seller.invite-code`
- `app.admin.invite-code`

Change these values in `application.properties` to secure your registration flow.

## Notes for contributors

- Keep the code simple and readable.
- Prefer the Maven wrapper (`./mvnw.cmd`) so builds work consistently across machines.
- If a new dependency is added, verify the app still builds with `./mvnw.cmd clean test`.
- For any role-related test setup, reuse existing roles instead of inserting duplicates.
