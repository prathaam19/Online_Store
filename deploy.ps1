# FoodMart Full Stack Deployment Script for Windows

Write-Host "🚀 Starting FoodMart deployment..." -ForegroundColor Green

# Check if Docker is installed
if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
    Write-Host "❌ Docker is not installed. Please install Docker Desktop first." -ForegroundColor Red
    exit 1
}

# Check if Docker Compose is installed
if (-not (Get-Command docker-compose -ErrorAction SilentlyContinue)) {
    Write-Host "❌ Docker Compose is not installed. Please install Docker Compose first." -ForegroundColor Red
    exit 1
}

# Create .env file if it doesn't exist
if (-not (Test-Path .env)) {
    Write-Host "📝 Creating .env file from .env.example..." -ForegroundColor Yellow
    Copy-Item .env.example .env
    Write-Host "⚠️  Please update .env with your configuration before running the application again." -ForegroundColor Yellow
    exit 0
}

# Create backend .env file if it doesn't exist
if (-not (Test-Path food\.env)) {
    Write-Host "📝 Creating food\.env file from food\.env.example..." -ForegroundColor Yellow
    Copy-Item food\.env.example food\.env
}

# Create frontend .env file if it doesn't exist
if (-not (Test-Path frontend\.env)) {
    Write-Host "📝 Creating frontend\.env file from frontend\.env.example..." -ForegroundColor Yellow
    Copy-Item frontend\.env.example frontend\.env
}

# Stop existing containers
Write-Host "🛑 Stopping existing containers..." -ForegroundColor Yellow
docker-compose down

# Build and start containers
Write-Host "🔨 Building and starting containers..." -ForegroundColor Yellow
docker-compose up -d --build

# Wait for services to be ready
Write-Host "⏳ Waiting for services to be ready..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

# Check container status
Write-Host "📊 Checking container status..." -ForegroundColor Yellow
docker-compose ps

Write-Host "✅ Deployment complete!" -ForegroundColor Green
Write-Host ""
Write-Host "🌐 Frontend: http://localhost:5173" -ForegroundColor Cyan
Write-Host "🔧 Backend API: http://localhost:8080" -ForegroundColor Cyan
Write-Host "📚 API Documentation: http://localhost:8080/swagger-ui.html" -ForegroundColor Cyan
Write-Host ""
Write-Host "To view logs: docker-compose logs -f" -ForegroundColor Gray
Write-Host "To stop: docker-compose down" -ForegroundColor Gray
