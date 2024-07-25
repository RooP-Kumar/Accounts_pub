
https://github.com/RooP-Kumar/Accounts/assets/69110669/28f8467a-f3d9-443a-be99-bfcdfa091690
# Accounts - Expense Tracker Application

Welcome to Accounts, an expense tracker application designed to help you manage your finances efficiently. This project was created to enhance my knowledge of Android WorkManager and Android Permissions.

## Features

- **User Authentication:** Securely log in with your credentials.
- **Expense Tracking:** Add, edit, and delete expenses.
- **Expense Categories:** Organize expenses into categories.
- **Reports:** View detailed reports of your expenses.
- **Profile Management:** Upload and manage your profile picture.
- **Offline Support:** Access your data even without an internet connection.

## Technologies Used

- **Android Studio:** Integrated Development Environment (IDE)
- **Android WorkManager:** For background task management, including:
  - **WorkRequest Chaining:** Used 3 different WorkRequests (create, update, delete) to practice request chaining.
  - **Custom Worker Factory:** Implemented custom worker factories and used these dynamically to choose different factory for different operations.
  - **PeriodicWorkRequest:** Used for automatic data backup (Daily, Weekly, Monthly).
- **Android Permissions:** To manage app permissions.
- **Room Database:** For local data storage.
- **Firebase Authentication:** For secure user authentication.
- **Firebase Realtime Database:** For data storage and synchronization.
- **MVVM Architecture:** For organized and maintainable code.
- **Coil:** For image loading and caching in Jetpack Compose.
- **Material Design:** For a modern and intuitive UI/UX.
- **Adobe XD/Figma:** For UI/UX design.
- **Adobe Illustrator:** For creating vectors used in the promo video.
- **Adobe After Effects:** For creating the promo video.
- **ImageCropper:** For picking and cropping images ([ImageCropper Repository](https://github.com/CanHub/Android-Image-Cropper)).

## Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/RooP-Kumar/Accounts_pub.git
    ```
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Create a new Firebase Project.
5. Download the google-service.json file of your own firbase project.
6. Paste it inside of your app folder.
7. Run the app on an emulator or a physical device.

   That's it :relaxed: !! enjoy your own expense tracker.

## Usage

1. **Sign Up:** Create a new account with your email and password.
2. **Log In:** Use your credentials to log in.
3. **Add Expense:** Click on the "Add Expense" button to record a new expense.
4. **View Reports:** Navigate to the "Reports" section to view your spending.
5. **Profile Management:** Go to the "Profile" section to upload or change your profile picture.

## Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes.

## License

This project is licensed under the MIT License.

## Contact

For any questions or feedback, feel free to reach out at roopkm12@gmail.com.

