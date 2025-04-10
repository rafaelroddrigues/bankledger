# Bank Ledger API

The **Bank Ledger API** is a Spring Boot application that provides RESTful endpoints for managing bank accounts, deposits, and withdrawals. It includes OpenAPI documentation for easy integration.

## Features

- **Account Management**: Create new bank accounts.
- **Deposits**: Deposit money into accounts.
- **Withdrawals**: Withdraw money from accounts with balance validation.

## Technologies Used

- **Java 21**
- **Spring Boot 3.4.2**
    - Spring Web
    - Spring Data JPA
    - Spring Validation
- **H2 Database** (in-memory database for development)
- **Gradle 8.13** (build tool)
- **SpringDoc OpenAPI 2.7.0** (for API documentation)

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/rafaelroddrigues/bank-ledger-api.git
cd bank-ledger-api
```

### Build and Run the Application

1. Build the project:

```bash
./gradlew clean build
```

2. Run the application:

```bash
./gradlew bootRun
```

3. The application will start on http://localhost:8080.

## API Endpoints

Endpoint | Method | Description |
--- |--------| --- |
/api/ledger/account | POST   | Create a new account |
/api/ledger/deposit | POST | Deposit money into an account |
/api/ledger/withdraw | POST | Withdraw money from an account |

## OpenAPI Documentation

* Swagger UI: http://localhost:8080/swagger-ui.html
* OpenAPI JSON: http://localhost:8080/v3/api-docs

## Example Requests

### Create Account 

```
POST /api/ledger/account
Content-Type: application/json

{
  "accountNumber": "123456789"
}
```

### Deposit

```
POST /api/ledger/deposit
Content-Type: application/json

{
  "accountNumber": "123456789",
  "amount": "100.00"
}
```

### Withdraw

```
POST /api/ledger/withdraw
Content-Type: application/json

{
  "accountNumber": "123456789",
  "amount": "50.00"
}
```

## Running Tests

```bash
./gradlew test
```

## License

This project is licensed under the MIT License. See the LICENSE file for details.