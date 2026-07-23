# Kotlin Android Client

An Android client app built with Kotlin and Jetpack Compose,
consuming the Spring Boot microservice API with AI chat support.

## Features

- JWT authentication (login with email and password)
- Bottom tab navigation (Home / Users / Orders / AI Chat)
- User list screen
- Order list screen
- AI chat screen with real-time streaming responses (SSE)
- Tab state saving and restoration when switching screens

## Tech Stack

- **Kotlin**
- **Jetpack Compose** — Modern declarative UI
- **Navigation Compose** — Bottom tab navigation with `NavigationBar` (Material3)
- **Retrofit + OkHttp** — HTTP client with timeout configuration
- **Coroutines** — Async operations
- **Material3** — UI components

## Screens

| Screen   | Description                                               |
|----------|-----------------------------------------------------------|
| Login    | Email and password login, JWT token stored locally        |
| Home     | App home screen                                           |
| Users    | Displays all users fetched from API                       |
| Orders   | Displays all orders with color-coded status               |
| AI Chat  | Streaming AI assistant powered by Ollama via Spring AI    |

## Navigation

Uses `Scaffold` + `NavigationBar` (Material3) for bottom tab navigation.

Key navigation strategy in `MainActivity.kt`:

```kotlin
navController.navigate(screen.route) {
    popUpTo(navController.graph.findStartDestination().id) {
        saveState = true   // save scroll position and input state
    }
    launchSingleTop = true  // avoid duplicate instances on repeated tap
    restoreState = true     // restore state when re-selecting a tab
}
```

- `popUpTo + saveState` — prevents back stack buildup across tabs
- `launchSingleTop` — avoids duplicate screen instances on rapid taps
- `restoreState` — restores scroll position and state when re-selecting a tab

## AI Chat

Connects to the Spring AI service and supports:

- Real-time streaming responses (SSE via `BufferedReader`)
- Session-based conversation memory (`android_session_<timestamp>`)
- Queries about real order and user data via Function Calling (RAG)

## Status Colors

- 🟢 COMPLETED
- 🟡 PENDING
- 🔵 PROCESSING

## Getting Started

### Prerequisites

- Android Studio
- Android device or emulator (API 26+)
- [spring-microservice-demo](https://github.com/leon-huang-tech/spring-microservice-demo) backend running locally

### Connect to local backend

Use `adb reverse` to forward device traffic to your development machine:

```bash
adb reverse tcp:8080 tcp:8080
```

Then build and run the app on your device or emulator.

### Build

```bash
./gradlew assembleDebug
```

## Gradle

- Gradle Wrapper: **9.6.0**
- Compile SDK: **35**
- Target SDK: **36**
- Min SDK: **26** (Android 8.0+)

## Demo Accounts

| Email                  | Password    |
|------------------------|-------------|
| alice@example.com      | password123 |
| bob@example.com        | password123 |
| charlie@example.com    | password123 |

## Related Projects

| Project | Description |
|---------|-------------|
| [spring-microservice-demo](https://github.com/leon-huang-tech/spring-microservice-demo) | Backend microservices (Spring Boot 4, Spring Cloud 2025, Spring AI) |
| [golang-http-benchmark](https://github.com/leon-huang-tech/golang-http-benchmark) | HTTP load testing tool for benchmarking the backend API |