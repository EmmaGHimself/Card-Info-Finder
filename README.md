# Card Info Finder
A simple card info finder android application which make use of BINLIST API to get card details and display the card information.

[![Kotlin Version](https://img.shields.io/badge/Kotlin-1.4.30-blue.svg)](https://kotlinlang.org)
[![AGP](https://img.shields.io/badge/AGP-4.1.2-blue?style=flat)](https://developer.android.com/studio/releases/gradle-plugin)
[![Gradle](https://img.shields.io/badge/Gradle-6.5.1-blue?style=flat)](https://gradle.org)

A simple card info finder project that presents a modern, 2020 approach to
[Android](https://en.wikipedia.org/wiki/Android_(operating_system)) application development with up to date tech-stack.

The goal of the project is to demonstrate best practices by using up to date tech-stack and presenting modern Android application
[Architecture](#architecture) that is monolithic, scalable, maintainable, and testable.

## Project characteristics

This project brings to table set of best practices, tools, and solutions:

* 100% [Kotlin](https://kotlinlang.org/)
* Modern architecture (Model-View-ViewModel)
* [Android Jetpack](https://developer.android.com/jetpack)
* Testing (Unit, UI)
* OCR Scanner
* Material design

The application project is made of two (2) modules. They are:
* `app`
* `restapi`

## Tech-stack
Min API level is set to [`21`](https://android-arsenal.com/api?level=21), so the presented approach is suitable for over
[85% of devices](https://developer.android.com/about/dashboards) running Android. This project takes advantage of many
popular libraries and tools of the Android ecosystem. Most of the libraries are in the stable version unless there is a
good reason to use non-stable dependency.

* Tech-stack
    * [Kotlin](https://kotlinlang.org/) + [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) + [Flow](https://developer.android.com/kotlin/flow) - perform background operations
    * [Retrofit](https://github.com/square/retrofit) - networking.
    * [Jetpack](https://developer.android.com/jetpack)
        * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - notify views about database change
        * [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - perform an action when lifecycle state changes
        * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - store and manage UI-related data in a lifecycle conscious way
  *   [Picasso](https://github.com/square/picasso) - image loading library with memory management
* Architecture
    * Clean Architecture (at module level)
    * MVVM in MainActivity.kt
    * [Android Architecture components](https://developer.android.com/topic/libraries/architecture) ([ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel), [LiveData](https://developer.android.com/topic/libraries/architecture/livedata))
* Tests
    * [Unit Tests](https://en.wikipedia.org/wiki/Unit_testing) ([JUnit](https://junit.org/junit4/))
    
    

## Getting started

There are a few ways to open this project.

### Android Studio

1. `Android Studio` -> `File` -> `New` -> `From Version control` -> `Git`
2. Enter `https://github.com/emmag13/CardInfoFinder.git` into URL field
3. Provide `APP_ID="<value goes here>"` in the `gradle.properties`

### Command-line + Android Studio

1. Run `git clone https://github.com/emmag13/CardInfoFinder.git` to clone project
2. Go to `Android Studio` -> `File` -> `Open` and select cloned directory
3. Provide `productionApiBaseUrl="<value goes here>"` in the `gradle.properties` for Release
3. Provide `dev.api.base.url="<value goes here>"` in the `local.properties` for Debug

What you should see when you compile and run the project:

<img src="https://drive.google.com/file/d/1GiJIZbOgngr2Udi0Vorf75DfqnkGRCEZ/view?usp=sharing" width="300" height="650">

## Inspiration

This is project is a sample, to inspire you and should handle most of the common cases, but please take a look at
additional resources.

