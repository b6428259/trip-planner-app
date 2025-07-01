#!/bin/bash

# Trip Planner Development Startup Script

echo "🚀 Starting Trip Planner Development Environment"

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker and try again."
    exit 1
fi

# Check if Docker Compose is available
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose and try again."
    exit 1
fi

# Start PostgreSQL database
echo "📦 Starting PostgreSQL database..."
docker-compose up -d postgres

# Wait for PostgreSQL to be ready
echo "⏳ Waiting for PostgreSQL to be ready..."
sleep 10

# Start backend
echo "🔧 Starting Spring Boot backend..."
cd backend
if [ ! -f ".env" ]; then
    echo "⚠️  .env file not found in backend directory. Copying from .env.example..."
    cp .env.example .env
fi

# Run backend in background
mvn spring-boot:run &
BACKEND_PID=$!
cd ..

# Start frontend
echo "🎨 Starting Next.js frontend..."
cd frontend
if [ ! -f ".env.local" ]; then
    echo "⚠️  .env.local file not found in frontend directory. Copying from .env.example..."
    cp .env.example .env.local
fi

# Install dependencies if node_modules doesn't exist
if [ ! -d "node_modules" ]; then
    echo "📦 Installing frontend dependencies..."
    npm install
fi

# Run frontend in background
npm run dev &
FRONTEND_PID=$!
cd ..

echo "✅ Development environment started!"
echo ""
echo "🌐 Frontend: http://localhost:3000"
echo "🔧 Backend: http://localhost:8080"
echo "📊 API Documentation: http://localhost:8080/api/swagger-ui.html"
echo "🗄️  Database: localhost:5432 (tripplanner/password)"
echo ""
echo "Press Ctrl+C to stop all services"

# Wait for Ctrl+C
trap 'echo "🛑 Stopping services..."; kill $BACKEND_PID $FRONTEND_PID; docker-compose down; exit' INT
wait