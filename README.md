# 📱 Mobile Prepaid Recharge System - Backend

## 📌 Overview
This repository contains the **backend service** for the Mobile Prepaid Recharge System.  
It provides **REST APIs** for:
- User & Admin authentication (JWT-based)
- Mobile recharge functionality
- Plan management
- Admin insights (expiring plans, recharge history)

Built using **Spring Boot, Spring Security, JPA, and MySQL**.

---

## 🚀 Features
- 🔑 **JWT Authentication & Authorization** (User / Admin roles)
- 📲 Recharge management with payment validation
- 📊 Admin insights: expiring subscribers, recharge history
- 📂 Categorized plan listing
- 📧 Email notifications after recharge
- ✅ 13 well-structured REST APIs

---

## 🛠️ Tech Stack
- **Backend**: Java 17, Spring Boot, Spring Security, JPA
- **Database**: MySQL
- **Authentication**: JWT
- **Tools**: Maven, Postman

---

## 📂 Project Structure
src/
├── main/java/com/techm/mobicom  
│ ├── controller # REST Controllers  
│ ├── dto # Data Transfer Objects  
│ ├── models # Entities (User, Plan, Recharge, etc.)  
│ ├── service # Business logic  
│ ├── repository # Spring Data JPA Repositories  
│ └── config # Security & JWT configs  
└── main/resources  
└── application.properties  

---

## ⚡ API Endpoints

### 🔹 Authentication
| Method | Endpoint              | Role  | Description |
|--------|-----------------------|-------|-------------|
| POST   | `/api/admin/register` | Public| Register an Admin |
| POST   | `/api/users/register` | Public| Register a User |
| POST   | `/api/users/validate` | Public| Validate a user (mobile) |
| POST   | `/api/login`          | Public| Login and receive JWT token |

---

### 🔹 Admin
| Method | Endpoint                                   | Role  | Description |
|--------|--------------------------------------------|-------|-------------|
| GET    | `/api/admin/subscribers/expiring`          | ADMIN | Get subscribers with expiring plans |
| GET    | `/api/admin/subscribers/{mobile}/rechargehistory` | ADMIN | Get recharge history of a subscriber |

---

### 🔹 Customers
| Method | Endpoint                        | Role  | Description |
|--------|---------------------------------|-------|-------------|
| POST   | `/api/customers/recharges`      | USER  | Create a recharge |
| GET    | `/api/customers/payment-modes`  | Public| Get available payment modes |
| GET    | `/api/customers/plans/{id}`     | Public| Get plan details by ID |
| GET    | `/api/customers/{mobile}/rechargehistory` | Public| Get recharge history by mobile number |

---

### 🔹 Plans
| Method | Endpoint     | Role  | Description |
|--------|-------------|-------|-------------|
| GET    | `/api/plans` | USER  | Get categorized plans |

---

### 🔹 Recharge
| Method | Endpoint              | Role  | Description |
|--------|-----------------------|-------|-------------|
| POST   | `/api/users/recharges`| USER  | Create recharge (alt. flow) |
| GET    | `/api/users/payment-modes` | Public | Get payment modes |

---

## ▶️ Getting Started
1. Clone the repository  
   ```bash
   git clone https://github.com/yourusername/mobile-prepaid-recharge-backend.git
   cd mobile-prepaid-recharge-backend
2. Update DB credentials in application.properties

3. Run with Maven  
     mvn spring-boot:run
---
## 🧪 Testing
Import the provided Postman collection (or test manually).  
Workflow:  
- Register User/Admin
- Login to get JWT
- Call protected endpoints with Authorization: Bearer <token>
---  
## 📌 Future Enhancements  
- Payment Gateway integration
- Pagination + sorting for plans/recharges
- Deployment on AWS/Heroku   
