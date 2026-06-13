# Kotlin Android Client

An Android client app built with Kotlin and Jetpack Compose, 
consuming the Spring Boot microservice API with AI chat support.

## Features

- JWT authentication (login with email and password)
- User list screen
- Order list screen
- AI chat screen with streaming responses
- Navigation between screens
- Logout support

## Tech Stack

- **Kotlin**
- **Jetpack Compose** — Modern declarative UI
- **Navigation Compose** — Screen navigation
- **Retrofit + OkHttp** — HTTP client with timeout configuration
- **Coroutines** — Async operations
- **Material3** — UI components

## Screens

| Screen | Description |
|--------|-------------|
| Login | Email and password login, JWT token stored locally |
| User List | Displays all users fetched from API |
| Order List | Displays all orders with color-coded status |
| AI Chat | Streaming AI assistant powered by Ollama via Spring AI |

## AI Chat

The AI chat screen connects to the Spring AI service and supports:
- Real-time streaming responses
- Session-based conversation memory
- Queries about real order and user data via RAG

## Status Colors

- 🟢 COMPLETED
- 🟡 PENDING
- 🔵 PROCESSING

## Getting Started

### Prerequisites
- Android Studio
- Android device or emulator (API 26+)
- [spring-microservice-demo](https://github.com/leon-huang-tech/spring-microservice-demo) backend running

### Run on physical device

Use ADB reverse to connect to local backend:
```bash
adb reverse tcp:8080 tcp:8080
```

Then run the app on your device.

### Run on emulator

The app uses `localhost:8080` which works with ADB reverse.
For Genymotion, use `10.0.3.2` as the base URL in `ApiClient.kt`.

## Demo Accounts

| Email | Password |
|-------|----------|
| alice@example.com | password123 |
| bob@example.com | password123 |
| charlie@example.com | password123 |

## Related Projects

- [spring-microservice-demo](https://github.com/leon-huang-tech/spring-microservice-demo) — Backend API with AI service
- [golang-http-benchmark](https://github.com/leon-huang-tech/golang-http-benchmark) — HTTP benchmark tool