# 💊 MedReminder

**MedReminder** is a modern Android application designed to help users manage their medications effectively. Built with Jetpack Compose and Material 3, it provides a seamless experience for scheduling reminders, tracking dosages, and monitoring refill dates to ensure you never miss a dose.

## ✨ Key Features

- **📋 Medicine Management** - Add, edit, and organize your medication list
- **⏰ Smart Reminders** - Set customizable reminders for each medicine at specific times
- **💊 Dosage Tracking** - Track daily dosage instructions and schedules  
- **🔔 Refill Alerts** - Monitor days remaining until refills with visual urgency indicators
- **📝 Personal Notes** - Add custom notes and instructions for each medication
- **🔐 Permission Handling** - Seamless notification permission setup for Android 13+
- **🌙 Dark Mode** - Full dark theme support that adapts to system preferences
- **📱 Modern UI** - Clean, intuitive interface with empty state management

## 🛠️ Technology Stack

### **Core Technologies**
- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern declarative UI toolkit  
- **Material 3** - Latest Material Design components

### **Architecture & Libraries**
- **Android Jetpack**
  - *Navigation Compose* - Type-safe navigation
  - *ViewModel* - Lifecycle-aware UI data management
  - *Room Database* - Local data persistence
  - *WorkManager* - Background task scheduling for reliable notifications
  - *Lifecycle Components* - Lifecycle management
- **Hilt** - Dependency injection framework
- **Coroutines & Flow** - Asynchronous programming and reactive streams
- **Accompanist Permissions** - Runtime permission handling
- **Kotlinx Serialization** - Type-safe data serialization

## 📱 App Screens

### Home Screen
The main dashboard displaying your medicine list with quick access to add new medications.

| Medicine List | Empty State |
|:-------------:|:-----------:|
| <img width="240" alt="Home with medicines" src="https://github.com/user-attachments/assets/be28d549-bcdc-4569-afa3-d3e355b05bbe" /> | <img width="240" alt="Empty state" src="https://github.com/user-attachments/assets/9e4c5e71-e874-448a-926a-2706e817ab85" /> |

### Add/Edit Medicine Flow
A streamlined multi-step process for entering complete medication details.

| Medicine Details | Reminder Setup | Refill Information |
|:----------------:|:--------------:|:------------------:|
| <img width="240" alt="Refill setup" src="https://github.com/user-attachments/assets/150e1ea8-849a-4bb0-8b54-25e65d5de7cb" /> | <img width="240" alt="Image" src="https://github.com/user-attachments/assets/e407fdef-12a4-40fe-845b-bf4b3020a963" /> | <img width="240"  alt="Image" src="https://github.com/user-attachments/assets/34044a14-b02c-402a-81a0-b7b295b28475" /> |

### Permission Management
User-friendly guidance for enabling notification permissions.

| Permission Request | Settings Guide |
|:-----------------:|:---------------:|
| <img width="240" alt="Medicine details" src="https://github.com/user-attachments/assets/17c82334-ce20-492e-8cfd-c79312350f99" /> | <img width="240" alt="Settings guide" src="https://github.com/user-attachments/assets/bd3bbca5-4137-4bb2-871a-b5876b058e8f" /> | <img width="240" alt="Permission request" src="https://github.com/user-attachments/assets/0ed72e4e-1ba7-42f4-8fd3-3d96cee05956" /> |

## 🚀 Getting Started

### Installation

1. **Clone the repository**
- git clone https://github.com/CodePandaaAI/medreminder.git

2. **Open in Android Studio**
- Launch Android Studio
- Select "Open an Existing Project"
- Navigate to the cloned directory

3. **Build and Run**
- Let Gradle sync and download dependencies
- Connect an Android device or start an emulator
- Click **Run** or press `Ctrl+R`

## 🔮 Planned Enhancements

- **☁️ Backup & Sync** - Cloud backup and restore functionality
- **📊 History Tracking** - Medication adherence history and analytics  
- **👥 Family Sharing** - Manage medications for family members

## 📜 License

[![License: AGPL v3](https://img.shields.io/badge/License-AGPL_v3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0)

This project is licensed under the **GNU Affero General Public License v3.0** (AGPL-3.0).

### What This Means:

✅ **You can:**
- View and study the source code
- Use the app for personal purposes
- Modify and improve the code
- Distribute modified versions

❌ **But you must:**
- Keep the same AGPL-3.0 license
- Share your source code publicly if you distribute the app
- Provide attribution to the original author
- Make your modifications available to users (even if running as a service)

📄 **Full license:** See [LICENSE](LICENSE) file or visit [GNU AGPL-3.0](https://www.gnu.org/licenses/agpl-3.0.en.html)

### For Commercial Use:
If you're interested in using this code under different terms, please contact: **romitsharmakv@gmail.com**

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

By contributing to this project, you agree that your contributions will be licensed under the same AGPL-3.0 license.

---

<div align="center">

**Made with ❤️ and ☕ for better health management**

Copyright © 2025 [Romit Sharma]

⭐ *If this project helps you, please consider giving it a star!*

</div>
