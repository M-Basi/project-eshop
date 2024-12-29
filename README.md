# project-eshop
This is my final Project for Coding Factory - Eshop-backend with Springboot(gradle) 

# Eshop Demo Project

## Overview
This is a demo e-commerce backend application built using **Spring Boot** and **Gradle**. It provides REST APIs for managing e-commerce functionalities such as authentication, product management, and order processing. The project integrates with a MySQL database and includes Swagger for API documentation.

---

## Features
- REST API for CRUD operations on products, orders, and user accounts.
- JWT-based authentication and authorization.
- MySQL integration for persistent data storage.
- Input validation using Spring Validation.
- Comprehensive Swagger API documentation.
- Global exception handling with custom exceptions.

---

## Prerequisites
To run this project, ensure you have the following installed:

- **Java 17** (Amazon Corretto recommended)
- **Gradle** (version 7.0 or higher)
- **MySQL**
- **Git**

---

## Setup Instructions

1. **Clone the Repository:**
   ```bash
   git clone <repository-url>
   cd cf6-eshop
   ```

2. **Configure the Database:**
   Update the `application.properties` file in `src/main/resources/` with your database credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/eshop_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Prepare the Database Schema:**
   Create a dataschema at mySql
   Use the provided script or annotations to set up the required schema. At the `application-prod.properties` file,
   configure the following:
   ```properties
   spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:
         ${MYSQL_PORT:'YOUR MYSQL PORT as 3306'}/${MYSQL_DB:'YOUR MYSQL SCHEMA as eshopdb'}?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8
   spring.datasource.username='Your Username of schema'
   spring.datasource.password=
   spring.jpa.hibernate.ddl-auto='Your Password of schema'
   ```
   Notice: Please check the comments at `application-prod.properties` file.

4. **Build the Project:**
   Run the following command to download dependencies and build the project:
   ```bash
   ./gradlew build
   ```

5. **Run the Application:**
   Start the Spring Boot application:
   ```bash
   ./gradlew bootRun
   ```

6. **Access Swagger:**
   Once the application is running, open [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) to view the Swagger API documentation.

---

## Build and Run

### Create a JAR File
To create a runnable JAR file:
```bash
./gradlew clean build
```
The JAR file will be available in the `build/libs/` directory. You can run it using:
```bash
java -jar build/libs/cf6-eshop-0.0.1-SNAPSHOT.jar
```


---

## REST API Endpoints
##BaseUrl = http://localhost:8080
### Authentication Endpoints
| HTTP Method | Endpoint                  | Description                   | Request Body          | Response Body         |
|-------------|---------------------------|-------------------------------|-----------------------|-----------------------|
| POST        | `/api/auth`               | Authenticate user             | `{ "username": "string", "password": "string" }` | `{ "token": "string" }` |
| POST        | `/authenticate`           | Get JWT for authentication    | `{ "username": "string", "password": "string" }` | `{ "token": "string" }` |
| POST        | `/refreshToken`           | Refresh authentication token  | `{ "refreshToken": "string" }` | `{ "token": "string" }` |

### Product Management Endpoints
| HTTP Method | Endpoint                  | Description                   | Request Body          | Response Body         |
|-------------|---------------------------|-------------------------------|-----------------------|-----------------------|
| GET         | `/api/products`           | Get all products              | None                  | `[ { "id": "number", "name": "string", "price": "number" } ]` |
| POST        | `/api/products/save`      | Save a product                | `{ "name": "string", "price": "number" }` | `{ "id": "number", "name": "string", "price": "number" }` |
| PUT         | `/api/products/update`    | Update a product              | `{ "id": "number", "name": "string", "price": "number" }` | `{ "id": "number", "name": "string", "price": "number" }` |
| DELETE      | `/api/products/delete`    | Delete a product              | `{ "id": "number" }` | `{ "message": "Product deleted successfully" }` |
| GET         | `/api/products/pageable`  | Get paginated products        | `{ "page": "number", "size": "number" }` | `[ { "id": "number", "name": "string", "price": "number" } ]` |

### Order Management Endpoints
| HTTP Method | Endpoint                  | Description                   | Request Body          | Response Body         |
|-------------|---------------------------|-------------------------------|-----------------------|-----------------------|
| POST        | `/api/orders/save`        | Save an order                 | `{ "productId": "number", "quantity": "number" }` | `{ "orderId": "number", "status": "string" }` |
| GET         | `/api/orders/all`         | Get all orders                | None                  | `[ { "orderId": "number", "status": "string" } ]` |
| GET         | `/api/orders/order`       | Get specific order by ID      | `{ "orderId": "number" }` | `{ "orderId": "number", "status": "string" }` |

### Product Management Endpoints
| HTTP Method | Endpoint                  | Description                   | Request Body          |
|-------------|---------------------------|-------------------------------|-----------------------|
| GET         | `/api/brands`             | Get all brands                | None                  |
| GET         | `/api/categories`         | Get all categories            | None                  |
| GET         | `/api/regions`            | Get all regions               | None                  |

### Customer Endpoints
| HTTP Method | Endpoint                  | Description                   | Request Body          |
|-------------|---------------------------|-------------------------------|-----------------------|
| POST        | `/api/customerInfo/save`  | Save customer information     | `{ "name": "string", "address": "string" }` |
| PUT         | `/api/customerInfo/update`| Update customer information   | `{ "id": "number", "name": "string", "address": "string" }` |
| GET         | `/api/customerInfo/get`   | Get customer information      | None                  |
| GET         | `/api/customerInfo/getByCustomer` | Get customer by ID     | `{ "id": "number" }` |
| DELETE      | `/api/customerInfo/delete`| Delete customer information   | `{ "id": "number" }` |

### Additional Customer Endpoints
| HTTP Method | Endpoint                  | Description                   | Request Body          |
|-------------|---------------------------|-------------------------------|-----------------------|
| GET         | `/api/customers/all`      | Get all customers             | None                  |
| GET         | `/api/customers/allPaginated`| Get customers with pagination | `{ "page": "number", "size": "number" }` |
| POST        | `/api/customers/customer/save` | Save customer               | `{ "name": "string", "address": "string" }` |
| PUT         | `/api/customers/customer/update` | Update customer           | `{ "id": "number", "name": "string", "address": "string" }` |
| DELETE      | `/api/customers/customer/delete` | Delete customer           | `{ "id": "number" }` |
| GET         | `/api/customers/customer/userUuid` | Get customer by UUID    | `{ "uuid": "string" }` |
### Payment Information Endpoints
| HTTP Method | Endpoint                  | Description                   | Request Body          | Response Body         |
|-------------|---------------------------|-------------------------------|-----------------------|-----------------------|
| POST        | `/api/paymentInfo/save`   | Save payment information      | `{ "orderId": "number", "paymentMethod": "string" }` | `{ "paymentId": "number", "status": "string" }` |
| PUT         | `/api/paymentInfo/update` | Update payment information    | `{ "paymentId": "number", "status": "string" }` | `{ "paymentId": "number", "status": "string" }` |
| GET         | `/api/paymentInfo/get`    | Get payment information       | `{ "paymentId": "number" }` | `{ "paymentId": "number", "status": "string" }` |
| GET         | `/api/paymentInfo/getByCustomer` | Get payment by customer | `{ "customerId": "number" }` | `[ { "paymentId": "number", "status": "string" } ]` |
| DELETE      | `/api/paymentInfo/delete` | Delete payment information    | `{ "paymentId": "number" }` | `{ "message": "Payment deleted successfully" }` |

### User Management Endpoints
| HTTP Method | Endpoint                  | Description                   | Request Body          | Response Body         |
|-------------|---------------------------|-------------------------------|-----------------------|-----------------------|
| POST        | `/api/users/register`     | Register a new user           | `{ "username": "string", "password": "string" }` | `{ "userId": "number", "username": "string" }` |
| PUT         | `/api/users/user/update`  | Update user information       | `{ "userId": "number", "username": "string" }` | `{ "userId": "number", "username": "string" }` |
| DELETE      | `/api/users/user/delete`  | Delete a user                 | `{ "userId": "number" }` | `{ "message": "User deleted successfully" }` |
| GET         | `/api/users/user/get`     | Get user information          | `{ "userId": "number" }` | `{ "userId": "number", "username": "string" }` |

---

## Project Structure

```
cf6-eshop/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── gr/eshop/marios/EshopApp/
│   │   │       ├── authentication/        # JWT-based authentication
│   │   │       ├── config/                # Swagger, CORS, and other configs
│   │   │       ├── controller/            # REST controllers
│   │   │       ├── core/                  # Core utilities, exceptions, filters
│   │   │       ├── model/                 # Entity models
│   │   │       ├── repository/            # Data access layer
│   │   │       ├── service/               # Business logic layer
│   │   │       ├── rest/                  # REST API implementations
│   │   └── resources/
│   │       ├── application.properties     # Spring Boot configuration
│   │       └── static/                    # Static resources
│   │       └── templates/                 # Thymeleaf templates (if any)
├── build.gradle                           # Gradle build file
├── settings.gradle                        # Gradle settings
└── README.md
```

---

## Testing

This project does not currently include unit or integration tests. You can add testing support with JUnit and Mockito.
All project checked through postman

---

## Troubleshooting

- **Database Connection Issues:**
    - Ensure your database server is running and credentials in `application.properties` are correct.

- **Gradle Build Fails:**
    - Verify that all dependencies are correctly declared in `build.gradle`.

- **Port Conflicts:**
    - If the default port `8080` is in use, modify the port in `application.properties`:
      ```properties
      server.port=9090
      ```

---

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.

---

## Additional Notes
- Replace placeholder text (e.g., `<repository-url>`) with actual values.
- Update the database credentials as needed for your local environment.
- Access the Swagger documentation at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).
