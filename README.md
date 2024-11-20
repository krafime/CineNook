# CineNook

CineNook is a movie discovery application built using Jetpack Compose for Android, enabling users to explore and get detailed information about their favorite movies, including related movies, genres, and more.

## Features

- Display Now Playing and Upcoming Movies
- Movie Detail View
  - Genres, duration, and other details
  - Related Movies section
- Search functionality for movies
- Shimmer effect for loading UI

## Installation

1. Clone the repository:
    ```
    git clone https://github.com/krafime/CineNook.git
    ```
2. Open the project in Android Studio.
3. Build and run the project on an Android emulator or physical device.

## Dependencies

This project uses several libraries to ensure optimal performance and UI responsiveness:
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern UI toolkit for building native Android interfaces.
- [Retrofit](https://square.github.io/retrofit/) - For handling API requests.
- [Coil](https://coil-kt.github.io/coil/) - Image loading library for Kotlin.

## Getting Started

1. You will need to set up your API key for fetching movie data. Obtain a key from [The Movie Database (TMDb)](https://www.themoviedb.org/).
2. Add your API key to the `gradle.properties` file:
   ```
   API_KEY=your_api_key
   ```

## Contributing
Feel free to contribute by submitting a pull request or opening an issue if you encounter bugs or have suggestions.
