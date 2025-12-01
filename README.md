# 🌟 Echo - Express Yourself

![Echo Banner](pathlight-icon-filled-256.png)

Echo is a modern social media platform that empowers users to express themselves through engaging posts with text and images. Built with cutting-edge technologies like Jetpack Compose and Firebase, Echo delivers a seamless, real-time social experience on Android.

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)
[![Android](https://img.shields.io/badge/Platform-Android-brightgreen.svg)](https://android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org)

## ✨ Features

- 🔐 **Secure Authentication**: Seamless login and registration powered by Firebase
- 📝 **Rich Post Creation**: Share your thoughts with text and images
- 🏠 **Dynamic Home Feed**: Real-time updates of posts from your network
- 🎨 **Modern UI**: Beautiful, responsive interface built with Jetpack Compose
- 🚀 **Real-time Updates**: Instant content delivery via Firebase Firestore
- 💫 **Engaging Animations**: Smooth transitions and loading states

## 📱 video

<div align="center">

https://github.com/user-attachments/assets/b9726d6d-8fd5-4219-b555-c874460540db

</div>
## 🚀 Getting Started

### Prerequisites

- Android Studio Arctic Fox or newer
- JDK 11 or newer
- Android SDK 21+

### Installation

1. **Clone the repository**
```bash
git clone git@github.com:rohitlokhande47/Echo-Social-Media-Application.git
cd Echo-Social-Media-Application
```

2. **Open in Android Studio**
- Launch Android Studio
- Select `File > Open`
- Navigate to the cloned repository
- Click `OK`

3. **Build and Run**
- Wait for the Gradle sync to complete
- Click `Run > Run 'app'` or press `Shift+F10`
- Select your device/emulator
  
## Cloudinary Integration

Echo uses Cloudinary for storing and managing images. Cloudinary provides a robust and scalable solution for image storage, allowing users to upload and retrieve images efficiently.

### Setup Cloudinary

1. **Add Cloudinary dependency**:
    ```kotlin
    implementation("com.cloudinary:cloudinary-android:3.0.2")
    ```

2. **Initialize Cloudinary**:
    ```kotlin
    val config = mapOf(
        "cloud_name" to "your_cloud_name",
        "api_key" to "your_api_key",
        "api_secret" to "your_api_secret"
    )
    val cloudinary = Cloudinary(config)
    ```

3. **Upload an image**:
    ```kotlin
    val file = File("path_to_image")
    val uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap())
    ```

## 📱 Usage Guide

### 1. Getting Started
- Launch Echo
- Sign up with email or log in to your existing account
- Complete your profile setup

### 2. Creating Posts
- Tap the `+` button on the home screen
- Add your thoughts in text
- Optionally attach images
- Hit post to share with your network

### 3. Exploring Content
- Scroll through your personalized feed
- Interact with posts through likes and comments
- Follow topics and users that interest you

## 🛠️ Built With

- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern Android UI toolkit
- [Firebase](https://firebase.google.com/)
  - Authentication
  - Cloud Firestore
  - Cloud Storage
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) - Asynchronous programming
- [Material Design 3](https://m3.material.io/) - UI/UX framework

## 🤝 Contributing

We welcome contributions to Echo! Here's how you can help:

1. **Fork the Repository**
2. **Create your Feature Branch**
```bash
git checkout -b feature/AmazingFeature
```
3. **Commit your Changes**
```bash
git commit -m 'Add some AmazingFeature'
```
4. **Push to the Branch**
```bash
git push origin feature/AmazingFeature
```
5. **Open a Pull Request**

## 📄 License

Echo is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👏 Acknowledgments

- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Firebase Documentation](https://firebase.google.com/docs)
- [Material Design Guidelines](https://material.io/design)

## 📬 Contact

Rohit Lokhande - [@rohitlokhande47](https://github.com/rohitlokhande47)

Project Link: [https://github.com/rohitlokhande47/Echo-Social-Media-Application](https://github.com/rohitlokhande47/Echo-Social-Media-Application)

---

<p align="center">Made with ❤️ by the Echo Team</p>
