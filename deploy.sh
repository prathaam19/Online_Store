#!/bin/bash

# FoodMart Full Stack Deployment Script

echo "🚀 Starting FoodMart deployment..."

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker first."
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi

# Create .env file if it doesn't exist
if [ ! -f .env ]; then
    echo "📝 Creating .env file from .env.example..."
    cp .env.example .env
    echo "⚠️  Please update .env with your configuration before running the application again."
    exit 0
fi

# Create backend .env file if it doesn't exist
if [ ! -f food/.env ]; then
    echo "📝 Creating food/.env file from food/.env.example..."
    cp food/.env.example food/.env
fi

# Create frontend .env file if it doesn't exist
if [ ! -f frontend/.env ]; then
    echo "📝 Creating frontend/.env file from frontend/.env.example..."
    cp frontend/.env.example frontend/.env
fi

# Stop existing containers
echo "🛑 Stopping existing containers..."
docker-compose down

# Build and start containers
echo "🔨 Building and starting containers..."
docker-compose up -d --build

# Wait for services to be ready
echo "⏳ Waiting for services to be ready..."
sleep 10

# Check container status
echo "📊 Checking container status..."
docker-compose ps

echo "✅ Deployment complete!"
echo ""
echo "🌐 Frontend: http://localhost:5173"
echo "🔧 Backend API: http://localhost:8080"
echo "📚 API Documentation: http://localhost:8080/swagger-ui.html"
echo ""
echo "To view logs: docker-compose logs -f"
echo "To stop: docker-compose down"
