# RN-expo-monorepo
The application mobile offline-first build in Native Kotlin + React Native with monorepo
Project tablet-app shell: Native Core-Heavy SDK
Tablet-app shell is a high-performance Offline-First mobile framework built on the Thick Client philosophy. Instead of a typical thin-wrapper app, Tablet-app shell centralizes all business logic, data persistence, and hardware orchestration into a Headless Native Core (Kotlin), providing a seamless, type-safe interface to a React Native App Shell.****


## 🏗️ System Architecture
The system follows a Decoupled Architecture to ensure maximum reliability in mission-critical environments (e.g., POS tablets, field equipment):

**Headless Native Core (Kotlin)**: The "Brain." It manages the Room Database, Atomic Transactions, Background Sync (WorkManager), and low-level Hardware APIs (NFC/Bluetooth).

**App Shell (React Native/Expo)**: The "Face." A lightweight UI layer that consumes the Core's capabilities through a structured Expo Modules Bridge.

##  ✨ Key Features
**Reliable Offline-First (Outbox Pattern)**: Guarantees zero data loss. Every user action is captured in a local queue and synchronized once connectivity is restored.

**Low-Level NFC Orchestration**: Utilizes Android's Reader Mode for high-speed, deterministic tag scanning, bypassing standard intent-based limitations.

**Atomic Transactions**: Leveraging Room's withTransaction to ensure that business data and synchronization metadata (Outbox) are updated as a single, indivisible unit.

**Dependency Injection**: Powered by Koin for clean, testable, and modular component management within the Native layer.

**Standardized Error Handlin**: A unified error propagation system that maps Kotlin BaseCoreException classes directly to TypeScript error codes.

## 🛠️ Technical Stack
| Layer | Technology |
| :--- | :--- |
| **Logic & Persistence** | Kotlin 1.9+, Coroutines |
| **Bridge Engine** | Expo Modules SDK (JSI) |
| **Database** | Room Persistence Library (with `@Upsert` support) |
| **Dependency Injection** | Koin |
| **Background Processing** | Android WorkManager |
| **UI Framework** | React Native |
| **Testing Suite** | MockK, Coroutines Test, JUnit 4 |

## 🚀 Getting Started
This project is managed as a Monorepo using pnpm Workspaces and Turborepo for optimized build pipelines and caching.

**1. Prerequisites**
- Node.js (v18+) & pnpm (v8+)
- Android Studio & Android SDK (API 34+)
- Java Development Kit (JDK) 17
- Ensure you have pnpm installed
<br>

**2. Installation**

Install all dependencies for the entire monorepo (Core + App Shell) from the root directory:

```
pnpm install
```
<br>

**3. Lint**
```
pnpm lint:fix
```
<br>

**4. Running the App**
```
cd apps/tablet-app
pnpm android  
```

