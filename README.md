# MedReminder

MedReminder is an Android application designed to help users manage their medications effectively. It allows users to schedule reminders for their medicines, track dosages, and monitor refill dates, ensuring they never miss a dose.

## Features

*   **Medicine Tracking:** Add and manage a list of your medications.
*   **Customizable Reminders:** Set up reminders for each medicine at specific times.
*   **Dosage Information:** Keep track of dosage instructions per day.
*   **Refill Tracking:** Monitor the number of days left before a refill is needed, with visual cues for urgency.
*   **Notes:** Add personal notes for each medicine.
*   **Notification Permissions:** Prompts users to enable notifications (especially for Android 13+).
*   **Empty State:** User-friendly interface when no medicines are added.
*   **Dark Mode Support:** Adapts to the system's dark theme.

## Tech Stack & Libraries

*   **Kotlin:** Primary programming language.
*   **Jetpack Compose:** Modern toolkit for building native Android UI.
*   **Material 3:** Latest design components from Material Design.
*   **Android Jetpack:**
    *   **Navigation Compose:** For in-app navigation.
    *   **ViewModel:** To store and manage UI-related data in a lifecycle-conscious way.
    *   **Room:** For local data persistence (storing medicine details).
    *   **Lifecycle:** For managing activity and fragment lifecycles.
    *   **WorkManager:** (Likely used for scheduling reliable background notifications - *assumption, please verify*)
*   **Hilt:** For dependency injection.
*   **Coroutines & Flow:** For asynchronous programming.
*   **Accompanist Permissions:** For handling runtime permissions like `POST_NOTIFICATIONS`.
*   **Kotlinx Serialization:** For data serialization (used with type-safe navigation).

## Screens

*   **Home Screen:** Displays the list of added medicines or an empty state if none exist. Includes a Floating Action Button (FAB) to add new medicines.
*   **Add/Edit Medicine Screen (Flow):** A multi-step process to input medicine details:
    *   Medicine Name, Dosage
    *   Reminder Times
    *   Refill Information
    *   Notes
*   **Permission Request Screen:** A dedicated UI to guide the user in granting notification permissions.

## Setup

1.  Clone the repository:
    ```bash
    git clone <your-repository-url>
    ```
2.  Open the project in Android Studio.
3.  Let Gradle sync and download the necessary dependencies.
4.  Build and run the application on an Android device or emulator.

## Screenshots

*(Placeholder: You can add screenshots of your app's main screens here.)*

*   *Home Screen (with medicines)*
*   *Home Screen (empty state)*
*   *Add Medicine Screen*
*   *Edit Medicine Screen*
*   *Notification Permission Request*

## Future Enhancements (Optional)

*   Backup and Restore functionality.
*   Medicine history tracking.
*   Snooze options for reminders.

---