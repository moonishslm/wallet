# Digital Wallet

## Overview

This project is a Digital Wallet API that allows users to manage their digital finances. It provides functionalities for user authentication, account management, and various transaction types including deposits, withdrawals, and transfers.

## Features

- User Registration and Authentication
- Wallet Creation and Management
- Deposit Funds
- Withdraw Funds
- Transfer Funds Between Wallets
- Transaction History

## Technologies Used

- Java 17
- Spring Boot 3.3
- Spring Security
- Spring Data JPA
- PostgreSQL
- Maven
- JUnit 5 for testing

## Database and API Documentation

### PostgreSQL Tables

In the `postgres_tables` folder, you can find CSV files representing the output of our PostgreSQL tables.

### Postman Collection and Examples

In the `postman_details` folder, you'll find:

1. A JSON file containing the Postman collection for this API. This collection includes all the requests needed to interact with the Digital Wallet.
2. Images of Postman requests and their corresponding responses. These images serve as visual documentation of the API's behavior and can be particularly helpful for understanding the expected input and output of each endpoint.
 Note:  do not forget to enter the token validation of each user before using the requests. 

To use the Postman collection:

1. Open Postman
2. Click on "Import" in the top left corner
3. Select the JSON file from the `postman_details` folder
4. The collection will be imported, and you can now send requests to the API

## Getting Started

### Prerequisites

- JDK 17 or later
- Maven 3.6
- PostgreSQL 12

## API Endpoints

- POST `/api/v1/auth/register` - Register a new user
- POST `/api/v1/auth/login` - Authenticate a user
- GET `/api/v1/user/read` - Get user details
- PUT `/api/v1/user/update` - Update user information
- DELETE `/api/v1/user/delete` - Delete a user
- GET `/api/v1/user/history` - Get user's transaction history
- GET `/api/v1/wallet/read` - Get wallet details
- POST `/api/v1/transaction/deposit` - Make a deposit
- POST `/api/v1/transaction/withdraw` - Make a withdrawal
- POST `/api/v1/transaction/transfer` - Transfer funds

## Setting up PostgreSQL with Docker for Spring Boot

This project uses Docker to run PostgreSQL.

### Prerequisites

- Docker and Docker Compose installed on your machine

### Docker Compose Configuration

use `docker-compose.yml` file to define and run the PostgreSQL container.

This configuration:
Sets up a PostgreSQL container named container-pg
Exposes PostgreSQL on port 5432
Creates a database named test_db with user admin and password root
Persists PostgreSQL data using a named volume
Includes pgAdmin for database management, accessible at http://localhost:5050
Running PostgreSQL
To start the PostgreSQL container:
Navigate to the directory containing your docker-compose.yml file.
Run the following command: docker-compose up -d

This starts the containers in detached mode.
Connecting Spring Boot to PostgreSQL

Accessing pgAdmin
Open a web browser and go to http://localhost:5050
Log in with:
Email: admin@admin.com
Password: root

To add the PostgreSQL server in pgAdmin:
Right-click on 'Servers' and choose 'Create' > 'Server'
In the 'General' tab, give your server a name
In the 'Connection' tab:
Host name/address: postgres (this is the service name in docker-compose)
Port: 5432
Maintenance database: test_db
Username: admin
Password: root

## Database Schema Setup

This project requires specific tables in the PostgreSQL database.
You can find the Data Definition Language (DDL) statements to create these tables in the `DDL_postgres_create_table.sql` file in the repository.
Then Using pgAdmin to Create Tables. 
do not forget to create the sequences that generating IBAN and AccountNumber. you can have them in `iban_seq.sql` and `account_number.sql`

. In the pgAdmin interface:
- Expand the Servers dropdown
- Expand your PostgreSQL server (you may need to enter the password: root)
- Expand Databases
- Right-click on your database (test_db) and select "Query Tool"

