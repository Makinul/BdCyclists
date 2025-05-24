# 🚀 BDCyclists

This is an Android application developed using **Jetpack Compose**, aiming to replicate the core UI and navigation patterns of a fitness tracking app like Strava. The project focuses on modern Android development practices, including declarative UI, robust navigation, and dependency injection.

## ✨ Features

* **Core UI Structure:**
    * Dynamic **Top App Bar** with a title and action icons (👤 User Profile, 🔍 Search, ⚙️ Settings).
    * Conditional **Back Button** 🔙 in the Top App Bar based on navigation state.
    * Five-item **Bottom Navigation Bar** with custom icons and selected state highlighting.
    * Two-tab layout positioned below the Top App Bar (e.g., for "Progress" and "Activities").

* **Navigation & Animations:**
    * Implemented screen navigation using **Jetpack Compose Navigation**.
    * Smooth **⬅️ left-to-right and right-to-left ➡️ slide animations** for screen transitions.
    * Navigation triggered by clicks on both Bottom Navigation Bar items and Top App Bar action icons.

* **Dependency Injection:**
    * Integrated **Koin** ✨ for managing and injecting dependencies throughout the application.
    * Setup Koin modules for ViewModels, Repositories, and network services.

* **Networking:**
    * Configured **Ktor Client** 🌐 for performing network calls.
    * Utilizes **Kotlinx Serialization** for efficient JSON data handling.

* **Styling & Theming:**
    * Customized **Top App Bar color** and **Status Bar color** to align with the application's theme.
    * Configured distinct icon colors for selected and unselected states in the Bottom Navigation Bar.
    * Dedicated background color for the Profile screen.
    * Basic Search Bar 🔎 implemented within the Search screen.

## 🛠️ Technologies Used

* **Kotlin**
* **Jetpack Compose** (UI Toolkit)
* **Jetpack Compose Navigation** (Navigation Component)
* **Koin** (Dependency Injection Framework)
* **Ktor Client** (HTTP Client for Networking)
* **Kotlinx Serialization** (JSON Serialization/Deserialization)
* **AndroidX Lifecycle** (ViewModel)
