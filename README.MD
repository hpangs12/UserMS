# User Microservice (UserMS)

Handles user registration, authentication, account management, and admin controls.

---

## 🚀 Features

- User Registration & Admin Creation  
- JWT-based Authentication  
- Account Activation / Deactivation / Deletion  
- Admin Ban / Unban Users  
- Role-Based Access Control (RBAC)  

---

## 📋 API Endpoints

| HTTP Method | Path                     | Description                       | Auth Required |
|------------|--------------------------|-----------------------------------|-------------|
| POST       | /users/register          | Register a new user              | No          |
| POST       | /users/register/admin    | Create an admin user             | No          |
| POST       | /users/login             | Login and get JWT token          | No          |
| POST       | /users/auth/validate     | Validate JWT token               | No          |
| GET        | /users/{username}        | Get user by username             | Yes         |
| POST       | /users/deactivate        | Deactivate own account           | Yes         |
| POST       | /users/delete            | Delete own account               | Yes         |
| POST       | /users/login/activate    | Reactivate own account           | Yes         |
| POST       | /users/admin/ban/{username}   | Ban user account (Admin only) | Yes (Admin) |
| POST       | /users/admin/unban/{username} | Unban user account (Admin only)| Yes (Admin) |

---

## 📊 HTTP Status Codes

| Status Code | Meaning                     |
|------------|-----------------------------|
| 200 OK     | Success                     |
| 201 Created| Resource created            |
| 204 No Content | Successful without content |
| 400 Bad Request | Validation error         |
| 401 Unauthorized | Invalid credentials     |
| 403 Forbidden | Insufficient role          |
| 500 Internal Server Error | Server issue    |

---

## ⚙️ Technologies

- Spring Boot  
- Spring Security (JWT)  
- Spring Data JPA  
- MySQL  
- Lombok  

---

## 🚧 Future Plans

- Refresh Token mechanism  
- Rate Limiting  
- Email Verification during registration  
- User Activity Audit Logging  
- OAuth2 / External Identity Provider Integration  

---

## ⚡ Notes

- Stateless authentication (JWT)  
- Roles stored as `ROLE_USER`, `ROLE_ADMIN` in DB  
- Passwords hashed with BCrypt
