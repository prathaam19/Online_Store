# FoodMart Frontend

React + Vite frontend for the FoodMart e-commerce application.

## Prerequisites

- Node.js 18+
- npm or yarn
- Backend API running on http://localhost:8080

## Setup

1. Install dependencies:
```bash
npm install
```

2. Copy environment variables:
```bash
cp .env.example .env
```

3. Update `.env` with your configuration:
- `VITE_GOOGLE_CLIENT_ID`: Your Google OAuth Client ID (see Setup Guide)
- `VITE_API_BASE_URL`: Backend API URL (default: http://localhost:8080)

## Google OAuth Setup

To enable Google sign-in:

1. Go to Google Cloud Console: https://console.cloud.google.com
2. Create a new project or select existing
3. Enable Google+ API
4. Create OAuth 2.0 credentials (Web application)
5. Add your frontend URL to authorized JavaScript origins (e.g., http://localhost:5173)
6. Copy the Client ID and add it to your `.env` file

## Run Development Server

```bash
npm run dev
```

The application will start on `http://localhost:5173`

## Build for Production

```bash
npm run build
```

## Tech Stack

- React 18
- Vite
- Redux Toolkit
- React Router
- Material UI
- Tailwind CSS
- Google OAuth
- Axios
