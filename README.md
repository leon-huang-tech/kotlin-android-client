# Kotlin Android Client

An Android client app built with Kotlin and Jetpack Compose, consuming the Spring Boot microservice API.

## Features

- JWT authentication (login with email and password)
- User list screen
- Order list screen
- Navigation between screens

## Tech Stack

- **Kotlin**
- **Jetpack Compose** — Modern declarative UI
- **Navigation Compose** — Screen navigation
- **Retrofit** — HTTP client
- **Coroutines** — Async operations
- **Material3** — UI components

## Screens

| Screen | Description |
|--------|-------------|
| Login | Email and password login, JWT token stored locally |
| User List | Displays all users fetched from API |
| Order List | Displays all orders with color-coded status |

## Status Colors

- 🟢 COMPLETED
- 🟡 PENDING
- 🔵 PROCESSING

## Getting Started

### Prerequisites
- Android Studio
- Android device or emulator (API 26+)
- [spring-microservice-demo](https://github.com/hllld/spring-microservice-demo) backend running

### Run locally

1. Clone this repository
2. Open in Android Studio
3. Update `BASE_URL` in `ApiClient.kt` if needed
4. Run on emulator or device

## Demo Accounts

| Email | Password |
|-------|----------|
| alice@example.com | password123 |
| bob@example.com | password123 |
| charlie@example.com | password123 |

## Related Projects

- [spring-microservice-demo](https://github.com/hllld/spring-microservice-demo) — Backend API
- [golang-http-benchmark](https://github.com/hllld/golang-http-benchmark) — HTTP benchmark tool
