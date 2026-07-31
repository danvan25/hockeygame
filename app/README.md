# Air Hockey Android

A modern Air Hockey game for Android written in **Java**, featuring **sensor-based controls**, a custom **2D physics engine**, and a modular architecture.

> ⚠️ This project is currently under active development.

---

## Features

- 📱 Phone tilt controls using the Android Rotation Vector sensor
- 🏒 Real-time mallet movement
- ⚙️ Custom game loop with delta-time updates
- 💥 Circle-to-circle collision detection
- 🧱 Realistic wall collisions
- 🚀 Mallet momentum transferred to the puck
- 🏗️ Modular architecture for future multiplayer support

---

## Technology

- Java
- Android SDK
- Canvas API
- Android Sensors
- Git & GitHub

---

## Project Structure

```text
com.example.hockeygame
├── activity
├── common
└── game
    ├── engine
    ├── input
    ├── model
    ├── network
    ├── physics
    ├── renderer
    ├── util
    └── view
```

---

## Architecture

```text
SensorInputController
          │
          ▼
       GameView
          │
          ▼
      GameEngine
      │        │
      ▼        ▼
PhysicsEngine  Models
```

Each module has a single responsibility:

- **GameView** – Rendering and game loop
- **GameEngine** – Game state management
- **PhysicsEngine** – Physics simulation
- **SensorInputController** – Phone sensor input
- **Models** – Game objects (Puck, Mallet, Score)

---

## Current Progress

### ✔ Completed

- Puck movement
- Wall collision detection
- Sensor-controlled mallet
- Mallet movement constraints
- Puck–mallet collision detection
- Momentum transfer from mallet to puck

### 🚧 In Progress

- Goal detection
- Score system
- Round reset
- Game rules

### 📋 Planned

- Multiplayer
- Online matchmaking
- Player statistics
- Profiles
- Sound effects
- Improved graphics
- AI opponent

---

## Purpose

The goal of this project is not only to build a playable Air Hockey game, but also to explore:

- Android game development
- Physics simulation
- Sensor programming
- Clean architecture
- Object-oriented design

---

## License

This project is developed for learning and portfolio purposes.