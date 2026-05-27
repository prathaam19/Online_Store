# FoodMart - Quick Start Guide

## 🚀 Build & Run

### Prerequisites
- Java 17 or higher
- MySQL 8.0+
- Maven 3.6+

### Step 1: Database Setup
```bash
# Login to MySQL
mysql -u root -p

# Create database
CREATE DATABASE foodmart;

# Exit MySQL
exit
```

### Step 2: Configure Application
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/foodmart
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=create-drop  # Use 'update' for production

jwt.secret=YourVeryLongSecretKeyWith32CharactersMinimumLength
jwt.expiration-ms=3600000  # 1 hour
```

### Step 3: Build Project
```bash
cd d:\food
mvn clean package
```

### Step 4: Run Application
```bash
mvn spring-boot:run
```

Or:
```bash
java -jar target/food-0.0.1-SNAPSHOT.jar
```

The application starts on **http://localhost:8080**

---

## 📱 Test the API

### 1. Access Swagger UI
Open browser: **http://localhost:8080/swagger-ui.html**

### 2. Register a User
```bash
POST /api/auth/register
Content-Type: application/json

{
  "username": "john123",
  "email": "john@example.com",
  "password": "password123"
}
```

**Response (Copy the token):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### 3. Add Authorization Token
In Swagger UI:
1. Click **"Authorize"** button (top right)
2. Enter: `Bearer {your_token_here}`
3. Click **"Authorize"**

### 4. Create a Category
```bash
POST /api/categories
{
  "name": "Fast Food"
}
```

### 5. Add a Product
```bash
POST /api/products/add
{
  "name": "Burger",
  "price": 5.99,
  "quantity": 100,
  "categoryId": 1
}
```

### 6. View All Products
```bash
GET /api/products/get
```

### 7. Add to Cart
```bash
POST /api/cart/add/1
```

### 8. View Cart
```bash
GET /api/cart
```

### 9. Place Order
```bash
POST /api/orders
{
  "shippingAddress": {
    "street": "123 Main St",
    "city": "New York",
    "state": "NY",
    "postalCode": "10001",
    "country": "USA"
  },
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

### 10. View Orders
```bash
GET /api/orders
```

---

## 🔍 Using cURL Commands

### Register
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

### Get All Products (with token)
```bash
curl -X GET http://localhost:8080/api/products/get \
  -H "Authorization: Bearer {your_token}"
```

---

## 📊 Database Tables

The following tables are automatically created:

| Table | Purpose |
|-------|---------|
| `users` | User accounts |
| `roles` | User roles (CUSTOMER) |
| `user_roles` | User-role mapping |
| `categories` | Product categories |
| `products` | Product catalog |
| `carts` | Shopping carts |
| `cart_products` | Cart items |
| `orders` | Customer orders |
| `order_items` | Items in orders |
| `addresses` | Shipping addresses |

---

## 🐛 Troubleshooting

### Issue: Connection refused (MySQL)
```
Error: Unable to acquire a Connection
```
**Solution:**
- Verify MySQL is running: `mysql --version`
- Check connection string in `application.properties`
- Verify database exists: `SHOW DATABASES;`

### Issue: No authorization header
```
Error: Unauthorized - 401
```
**Solution:**
- Get JWT token from `/api/auth/login` or `/api/auth/register`
- Add header: `Authorization: Bearer {token}`

### Issue: Validation error
```
Error: Validation failed for argument
```
**Solution:**
- Check request payload matches DTO requirements
- Ensure required fields are not null/empty
- See error response for field-specific messages

### Issue: Product not found
```
Error: Product not found with ID: 999
```
**Solution:**
- Verify product ID exists: `GET /api/products/get`
- Create a product first before adding to cart

---

## 📝 Important Notes

1. **JWT Token Expiration:** Tokens expire after 1 hour (configurable in properties)
2. **File Uploads:** Product images are stored in `/uploads/` directory
3. **Cart Clearing:** Cart is automatically cleared after order placement
4. **Password Hashing:** Passwords are hashed with BCrypt (never stored in plain text)
5. **Transactional:** All critical operations are wrapped in @Transactional for data consistency

---

## 🎯 Common Use Cases

### Scenario 1: Customer Order Flow
```
1. Register → Get JWT token
2. Browse products → GET /api/products
3. Add to cart → POST /api/cart/add/{productId}
4. View cart → GET /api/cart
5. Place order → POST /api/orders
6. View order history → GET /api/orders
```

### Scenario 2: Admin Product Management
```
1. Login → Get JWT token
2. Create category → POST /api/categories
3. Add product → POST /api/products/add
4. Upload image → POST /api/products/{id}/image
5. Update product → PUT /api/products/{id}
6. Delete product → DELETE /api/products/{id}
```

---

## 📚 API Response Examples

### Success Response (200)
```json
{
  "id": 1,
  "name": "Burger",
  "price": 5.99,
  "quantity": 100,
  "category": {
    "id": 1,
    "name": "Fast Food"
  },
  "imageFilename": "123456-uuid-burger.jpg"
}
```

### Validation Error (400)
```json
{
  "timestamp": "2026-05-24T10:30:00.000Z",
  "status": 400,
  "errors": {
    "price": "Price must be positive",
    "name": "Product name is required"
  },
  "path": "/api/products/add",
  "message": "Validation failed"
}
```

### Not Found Error (404)
```json
{
  "timestamp": "2026-05-24T10:30:00.000Z",
  "status": 404,
  "error": "ResourceNotFoundException",
  "message": "Product not found with ID: 999",
  "path": "/api/cart/add/999"
}
```

### Server Error (500)
```json
{
  "timestamp": "2026-05-24T10:30:00.000Z",
  "status": 500,
  "error": "Exception",
  "message": "Internal server error",
  "path": "/api/orders"
}
```

---

## ✅ Verification Checklist

After starting the application, verify:

- [ ] Application starts without errors
- [ ] Swagger UI loads: http://localhost:8080/swagger-ui.html
- [ ] Can register new user
- [ ] Can login and get JWT token
- [ ] Can create category
- [ ] Can add product with image
- [ ] Can add product to cart
- [ ] Can view cart
- [ ] Can place order
- [ ] Cart is cleared after order
- [ ] Can view order history

---

## 🔗 Useful Links

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **API Docs JSON:** http://localhost:8080/v3/api-docs
- **MySQL Documentation:** https://dev.mysql.com/doc/
- **Spring Boot Documentation:** https://spring.io/projects/spring-boot
- **JWT Guide:** https://jwt.io/introduction

---

**Happy Testing! 🎉**

For issues or questions, check the error logs in the console output.
