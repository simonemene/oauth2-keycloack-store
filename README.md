# OAUTH2/OPENID KEYCLOAK STORE

A demo project showcasing integration of Spring Security with **Keycloak** for stateless authentication using OAuth2 and OpenID Connect. This is a simple online store where users can register, authenticate, and purchase articles.

## Features

- Centralized authentication via Keycloak  
- OAuth2/OpenID Connect for identity and session management  
- Password hashing and user management delegated to Keycloak  
- Role-based access control managed through Keycloak roles  
- Stateless session management without JSESSIONID (tokens handled by Keycloak)  
- User roles and accounts configured inside Keycloak  

| Role  | Description                   | Email             | Password |
|-------|-------------------------------|-------------------|----------|
| USER  | Regular users (self-register)  | -                 | -        |
| ADMIN | Administrator                | admin@example.it  | admin    |
| TRACK | Order tracking operator       | track@example.it  | track    |

## Overview

This project demonstrates a modern authentication flow using **Keycloak**:

- Users authenticate via Keycloak’s OAuth2/OpenID Connect server  
- Upon successful login, Keycloak issues tokens (access and ID tokens)  
- Subsequent requests to the backend include the access token in the Authorization header  
- The backend validates the token with Keycloak and applies role-based access control  
- This approach removes the need for traditional session tracking (e.g., JSESSIONID), improving scalability with stateless authentication  

## Security Notice

- Credentials are sent only once during authentication via Keycloak  
- Always use HTTPS in production  
- Store tokens securely (e.g., in memory, or HttpOnly cookies if applicable)  
- Consider token expiration and refresh policies configured in Keycloak for added security  

> **Note:** This is a demo project and not production-ready. For production use, ensure secure token handling, refresh token usage, and proper Keycloak server hardening.

## Getting Started

### Prerequisites

- Java 17+  
- Maven  
- A configured relational database (e.g., MySQL or H2)  
- Node.js & npm (for the frontend)  
- A running Keycloak server configured with realms, clients, and roles  

### Running the Application

#### Option 1: Manual Run

```bash
# Clone the repository
git clone https://github.com/simonemene/oauth2-keycloak-store.git
cd oauth2-keycloak-store

# Run the Spring Boot backend
./mvnw spring-boot:run
# or, if Maven is installed globally
mvn spring-boot:run

# Run the Angular frontend
cd frontend
npm install
npm start
Make sure to configure the backend and frontend to connect to your Keycloak server.

Option 2: Docker Compose
bash
Copia
Modifica
# Build the backend JAR file
cd backend
./mvnw clean package

# Build the frontend production bundle
cd ../frontend
npm install
npm run build

# Start the application stack with Docker Compose
docker compose up --build
This will build and start the backend, MySQL database, frontend containers. Ensure Keycloak is running and properly configured.

Contributing
Feel free to open issues or submit pull requests if you have suggestions or improvements!
