# QuizApp_AndroidStudio

# QuizApp_AndroidStudio

## Overview
This Quiz app implements a simple game where users match names and photos. It consists of two core activities:

1. **Gallery**: Displays a list of names and associated photos. Users can scroll through the list, add new entries, remove existing ones, and sort entries alphabetically.
2. **Quiz**: Presents a random photo from the gallery along with three name options, one of which is correct. Users select the correct name, and the app provides feedback on whether the answer is correct.

## Features
- **Gallery Activity**: 
  - Add new entries (name and associated photo).
  - Remove existing entries by clicking on the photo.
  - Sort entries alphabetically and also reversed.
  
- **Quiz Activity**:
  - Randomly select a photo from the gallery.
  - Display three name options, one of which is correct.
  - Provide feedback on the correctness of the selected name.
  - Keep track of the user's score.

## Implementation Details
- **Data Structure**: Uses a simple data structure from the Collections interface to maintain photos and names.
- **Initialization**: Initializes the database with three default photos and names from the resource folder.
- **Persistent Storage**: Does not persistently store new entries or the score on the phone.
- **Memory Management**: Ensures proper memory management to prevent memory leaks.
- **Navigation**: Ensures correct navigation between activities and updates the gallery after adding or removing entries.

## Additional Notes
- **Database**: Does not use databases like SQLite or Rooms; instead, utilizes a simple data structure.
- **Image Size**: Ensures that images are not too large to avoid issues with Git.
- **Camera Feature**: Considers camera functionality as an optional feature for adding new entries.
- **Data Sharing**: Uses the Application class to share the data structure throughout the app.

## Usage
1. Clone the repository.
2. Open the project in Android Studio.
3. Build and run the app on an emulator or physical device.

## Future Improvements
- Implement persistent storage for new entries and scores.
- Enhance UI/UX for a more engaging user experience.
- Add additional features such as image editing or sharing options.
<img width="266" alt="Skjermbilde 2024-02-08 kl  10 52 10" src="https://github.com/600883/QuizApp_AndroidStudio/assets/89355523/d8695d1d-ad40-4472-a84d-ac694b06e720">
<img width="268" alt="Skjermbilde 2024-02-08 kl  10 52 23" src="https://github.com/600883/QuizApp_AndroidStudio/assets/89355523/cd85e332-96a6-433b-bc38-96731862ffc3">
<img width="265" alt="Skjermbilde 2024-02-08 kl  10 52 41" src="https://github.com/600883/QuizApp_AndroidStudio/assets/89355523/1c5721dc-e800-4f3c-980c-fac9a76dcbff">
<img width="266" alt="Skjermbilde 2024-02-08 kl  10 53 01" src="https://github.com/600883/QuizApp_AndroidStudio/assets/89355523/2f0f5f0c-60ed-4db2-b1e0-56fe3f10fca7">




