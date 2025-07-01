# API Documentation

## Authentication Endpoints

### POST /api/auth/login
Login with email and password.

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "user": {
    "id": 1,
    "username": "johndoe",
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "avatarUrl": null,
    "bio": null,
    "active": true,
    "createdAt": "2025-01-01T00:00:00Z",
    "updatedAt": "2025-01-01T00:00:00Z"
  }
}
```

### POST /api/auth/register
Register a new user account.

**Request Body:**
```json
{
  "username": "johndoe",
  "email": "user@example.com",
  "password": "password123",
  "confirmPassword": "password123",
  "firstName": "John",
  "lastName": "Doe"
}
```

**Response:** Same as login response.

### POST /api/auth/logout
Logout the current user (requires authentication).

## User Endpoints

### GET /api/users/me
Get current user profile (requires authentication).

### GET /api/users/{id}
Get user profile by ID.

### GET /api/users/search?q={query}
Search users by username, email, or name.

### PUT /api/users/me
Update current user profile (requires authentication).

### DELETE /api/users/me
Deactivate current user account (requires authentication).

## Error Responses

All endpoints may return error responses in the following format:

```json
{
  "timestamp": "2025-01-01T00:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/auth/login"
}
```

## Authentication

Most endpoints require authentication via JWT token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

## Rate Limiting

API endpoints are rate-limited to prevent abuse. Current limits:
- Authentication endpoints: 5 requests per minute per IP
- General endpoints: 100 requests per minute per user

## Database Schema

### Users Table
- `id` (Primary Key)
- `username` (Unique)
- `email` (Unique)
- `password_hash`
- `first_name`
- `last_name`
- `avatar_url`
- `bio`
- `active`
- `created_at`
- `updated_at`

### Trips Table
- `id` (Primary Key)
- `name`
- `description`
- `location`
- `start_date`
- `end_date`
- `creator_id` (Foreign Key)
- `is_public`
- `share_token` (Unique)
- `estimated_budget`
- `currency`
- `active`
- `created_at`
- `updated_at`

### Trip_Members Table
- `id` (Primary Key)
- `trip_id` (Foreign Key)
- `user_id` (Foreign Key)
- `role` (CREATOR, ADMIN, MEMBER)
- `status` (PENDING, ACCEPTED, DECLINED)
- `joined_at`

### Groups Table
- `id` (Primary Key)
- `name`
- `description`
- `creator_id` (Foreign Key)
- `active`
- `created_at`
- `updated_at`

### Group_Members Table
- `id` (Primary Key)
- `group_id` (Foreign Key)
- `user_id` (Foreign Key)
- `role` (CREATOR, ADMIN, MEMBER)
- `joined_at`

### Friends Table
- `id` (Primary Key)
- `requester_id` (Foreign Key)
- `addressee_id` (Foreign Key)
- `status` (PENDING, ACCEPTED, DECLINED, BLOCKED)
- `created_at`
- `accepted_at`

### Availabilities Table
- `id` (Primary Key)
- `trip_id` (Foreign Key)
- `user_id` (Foreign Key)
- `start_date`
- `end_date`
- `notes`
- `created_at`

### Messages Table
- `id` (Primary Key)
- `sender_id` (Foreign Key)
- `trip_id` (Foreign Key, nullable for private messages)
- `recipient_id` (Foreign Key, nullable for trip messages)
- `content`
- `message_type` (TEXT, IMAGE, FILE, SYSTEM, NOTIFICATION)
- `edited`
- `edited_at`
- `created_at`

### Notifications Table
- `id` (Primary Key)
- `user_id` (Foreign Key)
- `type` (FRIEND_REQUEST, FRIEND_ACCEPTED, TRIP_INVITATION, TRIP_UPDATE, MESSAGE, AVAILABILITY_UPDATE, SYSTEM)
- `title`
- `content`
- `is_read`
- `action_url`
- `data` (JSON)
- `created_at`
- `read_at`