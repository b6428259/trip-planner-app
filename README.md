# Trip Planner Web Application

A comprehensive trip planning web application for groups of friends with modern tech stack.

## Tech Stack

### Frontend
- **Next.js 14** with App Router
- **TypeScript** for type safety
- **Tailwind CSS** for styling
- **Zustand** for state management
- **WebSocket** for real-time communication

### Backend
- **Java Spring Boot** with REST APIs
- **Multiple Database Support** (PostgreSQL, MySQL, H2, SQLite)
- **JWT Authentication** with bcrypt
- **WebSocket** for real-time messaging
- **Docker** for containerization

## Features

### 🔐 Authentication System
- User registration and login with bcrypt password encryption
- JWT token-based authentication
- Guest access via shared trip links
- Protected routes and middleware

### 👥 User Management
- User profiles with avatars
- Friend system (send/accept friend requests)
- Group creation and management
- User search functionality

### 🗺️ Trip Management
- Create trips with details (name, description, dates, location)
- Add friends to trips with role-based permissions
- View personal and joined trips
- Public trip sharing via unique links
- Trip editing and deletion

### 📅 Availability System
- Add/remove available date ranges
- Visual calendar interface
- Smart analysis to find optimal dates
- Conflict resolution suggestions

### 💬 Communication System
- Individual private messaging
- Group chat for each trip
- Real-time messaging with WebSocket
- Message history and notifications

### 🤖 AI Integration
- Optimal date recommendations
- Weather forecast analysis
- Distance calculations
- Fuel cost estimation
- Trip cost breakdown and splitting

### 🔔 Notification System
- Customizable notification preferences
- Real-time notifications
- Notification history and management

## Project Structure

```
├── frontend/           # Next.js 14 application
├── backend/           # Spring Boot application
├── docker/            # Docker configurations
├── docs/             # Documentation
└── README.md
```

## Quick Start

### Prerequisites
- Node.js 18+ and npm/yarn
- Java 17+
- Database (PostgreSQL 14+, MySQL 8.0+, or use H2/SQLite for development)
- Docker (optional, for containerized development)

### Development Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd trip-planner-app
   ```

2. **Setup Backend**
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```

3. **Setup Frontend**
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

4. **Setup Database**
   ```bash
   # Create PostgreSQL database
   createdb tripplanner
   
   # Run migrations (handled automatically by Spring Boot)
   ```

### Docker Development (Alternative)

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f
```

## API Documentation

The API documentation is available at `http://localhost:8080/swagger-ui.html` when the backend is running.

## Database Options

The application supports multiple database options for different development and production needs:

### H2 (In-Memory) - Development
```bash
mvn spring-boot:run -Dspring.profiles.active=h2
# Access H2 Console: http://localhost:8080/h2-console
```

### SQLite (File-based) - Development
```bash
mvn spring-boot:run -Dspring.profiles.active=sqlite
```

### MySQL - Production
```bash
# Start MySQL with Docker
docker run --name mysql-trip -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=tripdb -p 3306:3306 -d mysql:8.0

# Run application
mvn spring-boot:run -Dspring.profiles.active=mysql
```

### PostgreSQL - Production (Default)
```bash
# Start PostgreSQL with Docker  
docker run --name postgres-trip -e POSTGRES_PASSWORD=password -e POSTGRES_DB=tripdb -p 5432:5432 -d postgres:15

# Run application
mvn spring-boot:run -Dspring.profiles.active=postgres
```

### Multiple Database Docker Compose
```bash
# Start both PostgreSQL and MySQL services
docker-compose up -d

# Run with PostgreSQL
mvn spring-boot:run -Dspring.profiles.active=postgres

# Run with MySQL  
mvn spring-boot:run -Dspring.profiles.active=mysql
```

## Environment Variables

### Frontend (.env.local)
```
NEXT_PUBLIC_API_URL=http://localhost:8080
NEXT_PUBLIC_WS_URL=ws://localhost:8080/ws
```

### Backend (application.yml)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/tripplanner
    username: ${DB_USERNAME:tripplanner}
    password: ${DB_PASSWORD:password}
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.