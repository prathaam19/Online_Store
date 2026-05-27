# FoodMart Backend Setup Guide

## Environment Configuration

The application now uses environment variables for sensitive configuration. Copy `.env.example` to `.env` and update the values:

```bash
cp .env.example .env
```

## Required Configuration

### Email Password
The email password in `.env` is set to `your-app-password`. To enable email functionality:

1. Go to Google Account settings
2. Enable 2-Factor Authentication
3. Generate an App Password at https://myaccount.google.com/apppasswords
4. Replace `your-app-password` in `.env` with the generated app password

### Google OAuth (Frontend)
The frontend requires a Google OAuth Client ID. To enable Google sign-in:

1. Go to Google Cloud Console: https://console.cloud.google.com
2. Create a new project or select existing
3. Enable Google+ API
4. Create OAuth 2.0 credentials (Web application)
5. Add your frontend URL to authorized JavaScript origins (e.g., http://localhost:5173)
6. Copy the Client ID and update `frontend/src/main.jsx`:
   ```javascript
   <GoogleOAuthProvider clientId="YOUR_ACTUAL_GOOGLE_CLIENT_ID">
   ```

### Razorpay
The application uses Razorpay test credentials by default. For production:
- Replace the test credentials with your production keys in `.env`
- Update `razorpay.mode` to `live`

### Database
- Ensure MySQL is running on localhost:3306
- Create the `foodmart` database:
  ```sql
  CREATE DATABASE foodmart;
  ```
- Update DB credentials in `.env` if different from defaults

### JWT Secret
- Change the default JWT secret in `.env` to a secure random string for production

### Role Invite Codes
- Update `APP_SELLER_INVITE_CODE` and `APP_ADMIN_INVITE_CODE` in `.env` with secure codes

## Running the Application

### Using Maven Wrapper (Windows)
```bash
./mvnw.cmd spring-boot:run
```

### Using Maven
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Documentation

Swagger UI available at: `http://localhost:8080/swagger-ui.html`

## Testing

Run tests with H2 in-memory database:
```bash
./mvnw.cmd clean test
```
