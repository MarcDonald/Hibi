![Header](/.github/assets/header.png?raw=true "Header")

# Hibi 「日々」
An app for Android designed to aid your Japanese learning through keeping a journal.

## Table of Contents

- [Download](#download)
- [Features](#features)
- [Screenshots](#screenshots)
- [Tech and Architecture](#tech-and-architecture)
- [Open Source Libraries Used](#open-source-libraries-used)
- [Acknowledgments](#acknowledgments)
- [License](#license)

## Download

[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png"
     alt="Get it on F-Droid"
     height="80">](https://f-droid.org/packages/com.marcdonald.hibi/)

[Or Click Here to Go To the Latest Release](https://github.com/MarcDonald/Hibi/releases/latest)

## Features
- Add entries at any date and time
- Search for words through Jisho.org right from the editing screen without having to switch app
- Save new words used in an entry
- Tag entries (e.g. Holiday, Day Out, Birthday)
- Add entries to books (e.g. Trip to Tokyo 2018, Trip to Shirakawa-go 2019)
- Add locations to entries
- Add images to entries
- Search your entries based on date, content, location, tags or books
- Daily reminder to add an entry
- Ability to backup and restore your data
- Dark theme and Light theme
- Easily see entries made on this day last month, and in previous years, using the Throwback feature

## Screenshots
| Main Screen | Main Screen Dark | Add Entry |
|:-:|:-:|:-:|
| ![Main Screen](/.github/assets/main-display-light.png?raw=true) | ![Dark Theme](/.github/assets/main-display-dark.png?raw=true) |![Add entry](/.github/assets/add-entry.png?raw=true)

| Search Jisho | More Info | Add New Words |
|:-:|:-:|:-:|
| ![Search Jisho](/.github/assets/search-jisho.png?raw=true) | ![More Info](/.github/assets/search-jisho-more.png?raw=true) | ![Add New Words](/.github/assets/add-words.png?raw=true)

| Add Tags | Search Entries |
|:-:|:-:|
| ![Add Tags](/.github/assets/add-tags.png?raw=true) | ![Search Entries](/.github/assets/search-entries.png?raw=true) |

## Tech and Architecture
![Architecture Diagram](/.github/assets/architecture-diagram.png?raw=true "Architecture Diagram")

Hibi is written entirely in Kotlin and employs a single activity MVVM architecture pattern using [AndroidX View Model](https://developer.android.com/topic/libraries/architecture/viewmodel) components. 
The activity contains a single NavHostFragment, part of the [AndroidX Navigation Components](https://developer.android.com/guide/navigation/), which hosts all other fragments such as the MainScreenFragment.
Each fragment observes data in a View Model which may retrieve data from various repositories and API services and properly format it for display. All business logic is handled in the View Models and fragments are kept as minimal as possible (only having code for setting up Observers, click listeners, etc...).
Data is stored using a [Room](https://developer.android.com/jetpack/androidx/releases/room) database and queries are provided in a DAO in the form of functions. 
Data is then retrieved asynchronously within repositories using Kotlin coroutines and [LiveData](https://developer.android.com/topic/libraries/architecture/livedata). 
[Kodein](https://github.com/Kodein-Framework/Kodein-DI) is used for dependency injection due to it's native support for Kotlin and it's ease of writing. 
API calls are made using [Retrofit](https://github.com/square/retrofit) and then converted into Kotlin objects using [Moshi](https://github.com/square/moshi).
Design inspired by [Material Design](https://material.io/) and implemented using [Material Components for Android](https://github.com/material-components/material-components-android).

## Open Source Libraries Used
### [Timber](https://github.com/JakeWharton/timber)
Used for logging

Apache 2 License

### [Kodein](https://github.com/Kodein-Framework/Kodein-DI)
Used for dependency injection

MIT License

### [Retrofit](https://github.com/square/retrofit)
Used for API calls

Apache 2 License

### [Moshi](https://github.com/square/moshi)
Used for converting API responses into Kotlin objects

Apache 2 License

### [Android File Picker](https://github.com/DroidNinja/Android-FilePicker)
Used for selecting file to restore from

Apache 2 License

### [Android Image Picker](https://github.com/esafirm/android-image-picker)
Used for selecting images

[License](https://github.com/esafirm/android-image-picker/blob/master/LICENSE)

### [M PLUS Rounded 1c Bold](https://fonts.google.com/specimen/M+PLUS+Rounded+1c)
Used as the icon font

Open Font License

### [Open Sans](https://fonts.google.com/specimen/Open+Sans)
Used throughout the application

Apache 2 License

### [Google Material Design Icons](https://material.io/tools/icons/)
Used throughout the application

Apache 2 License

### [Material Design Icons](https://materialdesignicons.com/)
Used throughout the application

SIL Open Font License 1.1

### [Glide](https://github.com/bumptech/glide)
Used for image loading and caching

[License](https://github.com/bumptech/glide/blob/master/LICENSE)

### [Simple License Display](https://github.com/MarcDonald/SimpleLicenseDisplay)
Used for displaying OSS licenses

MIT License

## Acknowledgments
Thanks to [Jisho.org](https://jisho.org/) for making the API used for searching up words.

Thanks to [this gist](https://gist.github.com/filipkowicz/1a769001fae407b8813ab4387c42fcbd) for providing the basis of the sticky month headers.

## License
```   
Copyright 2020 Marc Donald

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
