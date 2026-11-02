# Android Application

A native Android app built with **Kotlin** and **Jetpack
Compose**, following **Clean Architecture** and **MVVM**.

The app displays listings, provides a details screen,
and lets users contact a dealer by phone.

## Features

-   Listing and Details screens
-   Modular Gradle architecture with separate `app`, `domain`, and
    `data` modules
-   Clean Architecture with domain use cases and repository abstraction
-   MVVM with reactive UI state using sealed interfaces and `StateFlow`
-   Retrofit for remote API communication
-   Room for local persistence and offline-friendly caching
-   Room as the local source of truth for UI-observed data
-   Kotlin Coroutines and Flow for asynchronous and reactive operations
-   Hilt for dependency injection
-   Loading, empty, error, and success UI states
-   Animated navigation transitions
-   Dealer calling functionality

## Tech Stack

  Technology            Purpose
  --------------------- --------------------------------------------
  Kotlin                Primary programming language
  Jetpack Compose       Declarative UI
  MVVM                  Presentation-layer architecture
  Clean Architecture    Separation of responsibilities
  Gradle modules        Separation of app, domain, and data layers
  Coroutines and Flow   Asynchronous and reactive data handling
  Retrofit              Network requests
  Room                  Local database and caching
  Hilt                  Dependency injection
  StateFlow             Observable UI state

## Architecture

The project is organized into three Gradle modules:

``` text
:app       Presentation layer: Compose UI, ViewModels, navigation
:domain    Domain layer: models, repository contracts, use cases
:data      Data layer: repository implementations, API, Room, mappings
```

### Data flow

1.  The presentation layer invokes a domain use case.
2.  The use case calls the repository contract defined in the domain
    module.
3.  The data module implements the repository and coordinates remote and
    local data sources.
4.  Retrofit retrieves remote data, and Room stores the data
    locally.
5.  The UI observes data exposed through Flow and updates through
    StateFlow-backed UI state.

This structure keeps the domain layer independent of Android UI and
data-source implementation details.

## Getting Started

1.  Clone or download this repository.
2.  Open the project in Android Studio.
3.  Allow Gradle to sync and resolve dependencies.
4.  Select the `app` run configuration.
5.  Run the app on an emulator or a connected Android device.

> **Note:** Network access is required to retrieve remote data.
> The app can use locally cached data when available, depending on the
> current data and error-handling implementation.

## Testing

Run the available unit and instrumentation tests from Android Studio, or
use Gradle:

``` bash
./gradlew test
./gradlew connectedAndroidTest
```

The first command runs local unit tests. The second runs instrumentation
tests and requires a connected device or emulator. These commands may
need to be adjusted if the project uses different test tasks or source
sets.

## Project Goals

This project demonstrates modular Android development, separation of
concerns, reactive UI state management, dependency injection, network
and database integration, and a user-friendly Compose interface.




