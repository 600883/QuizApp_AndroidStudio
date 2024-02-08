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
  - Sort entries alphabetically.
  
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
