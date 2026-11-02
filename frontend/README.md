Investment Tracker — Frontend
Frontend application for Investment Tracker, built with React, TypeScript, and Vite.

The frontend is maintained within the Investment Tracker monorepo and is independent of the Spring Boot microservices.
Technology Stack
React
TypeScript
Vite
ESLint
npm
Prerequisites
Node.js (LTS)
npm
Local Development
Navigate to the frontend directory:
Bash
cd frontend
Install dependencies:
npm install
Start the development server:
npm run dev
Open the local URL displayed in the terminal (usually http://localhost:5173).
Production Build
npm run build
The command checks TypeScript and generates production assets in the dist/ directory.
Code Quality
Run ESLint:
npm run lint
Environment Configuration
Vite environment variables are used for environment-specific configuration.

The .env.development file contains:
Bash
VITE_API_BASE_URL=http://localhost:8086
Environment variables are accessed through src/config/env.ts.

Only variables prefixed with VITE_ are exposed to client-side code. Never store secrets or credentials in these variables.
Project Structure
Bash
src/
├── app/          # Application-level setup
├── components/   # Reusable UI components
├── config/       # Environment configuration
├── hooks/        # Custom React hooks
├── types/        # Shared TypeScript types
├── App.tsx       # Root component
└── main.tsx      # Application entry point
Current Scope
The initial frontend setup provides the foundation for future development.

Authentication, API integration, application navigation, and business pages will be implemented in subsequent stories.