## _Project Kunigami_ / OctoMeter: Empowering Smart Electricity Usage<br/>![Gradle Build](https://github.com/ryanw-mobile/OctoMeter/actions/workflows/main_build.yml/badge.svg) [![codecov](https://codecov.io/gh/ryanw-mobile/OctoMeter/graph/badge.svg?token=fGinpoyVUp)](https://codecov.io/gh/ryanw-mobile/OctoMeter)

### Production-grade Kotlin Multiplatform App targeting Android, iOS, Desktop

<br />
<p><img src="app_banner_240518.webp" style="width: 100%; max-width: 1000px; height: auto;" alt="cover image" style="width: 100%; max-width: 1000px; height: auto;"></p>
<br />

This app is for Octopus Energy customers in the UK who have a smart meter installed. You can still
try out the app if you don't, because the App will run under the demo mode by default by showing
fake user data when authentication is required.

### It works for me

**This is not a sample app or a demo app**. This is a real app I developed for myself, and I use it
every day to monitor energy consumption and save money. Technically it should work for other Octopus
Energy customers. However, I am unable to imagine other scenarios such as the tariffs I do not use,
or multiple meter installations which may return data that the App cannot process. Therefore, it is
not guaranteed that it works for you when it should work for me.

This app is still in rapid development. The first release is not ready yet but is expected around
June 2024, as I will need to secure my next full-time job in the UK soon. For the same reason, this
app was intentionally built to reach production-level quality as possible, such that you can have a
better idea where I am at technically.

This README will be updated with more technical details when the app is close to release. Stay
tuned!
<br /><br />

## Switch to Octopus!

Switch to Octopus Energy using [this link](https://share.octopus.energy/best-shell-168), both of us will get £50 (or £100 for business).

<br /><br />

## To-do lists

Planned enhancements are logged as [issues](https://github.com/ryanw-mobile/OctoMeter/issues).

### Current limitations

There are the known issues to be sorted, since they are not affecting me, it will be queued to be
improved later:

* Properly handle dual-rate tariffs (day unit rate and night unit rates)

<br /><br />

## Running the app

I use Android Studio Koala for Android and Deskop apps. For iOS, I use Xcode 15.4.

* The easiest way to run on Android is to download the apk.
* To export the desktop app into MacOS distributable, execute `./gradlew packageDmg` (I don't use
  Windows)
* To run the desktop app, execute `./gradlew runReleaseDistrubutable` or just `./gradlew run`
* You can't export the Jar alone. To export the Jar for desktop. Jar doesn't comes with the native
  Skiko library so it won't run.
* iOS TestFlight is coming soon.

<br /><br />

## Some draft technical details

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
    - `commonMain` is for code that’s common for all targets.
    - Other folders are for Kotlin code that will be compiled for only the platform indicated in the
      folder name.
      For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
      `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for
  your project.

### Dependencies

* [Kermit](https://github.com/touchlab/Kermit) - Apache 2.0 - A Kotlin Multiplatform logging library
* [KoalaPlot](https://github.com/koalaplot/koalaplot-core) - MIT - A plotting library for Kotlin
  Multiplatform
* [Kotlin](https://kotlinlang.org/) - Apache 2.0 - Kotlin programming language
* [Jetpack Compose](https://developer.android.com/jetpack/androidx/releases/compose) - Apache 2.0 -
  Modern toolkit for building native UI
* [AndroidX](https://developer.android.com/jetpack/androidx) - Apache 2.0 - AndroidX libraries
* [Kotlinx](https://github.com/Kotlin) - Apache 2.0 - Kotlin extensions and libraries
* [Ktor](https://ktor.io/) - Apache 2.0 - Framework for building asynchronous servers and clients in
  connected systems
* [Kotest](https://kotest.io/) - Apache 2.0 - Kotlin test framework
* [Material3](https://github.com/material-components/material-components-android) - Apache 2.0 -
  Material Design components
* [Koin](https://insert-koin.io/) - Apache 2.0 - Dependency Injection framework for Kotlin
* [Multiplatform Settings](https://github.com/russhwolf/multiplatform-settings) - Apache 2.0 -
  Multiplatform settings library for Kotlin
* [Theme Detector](https://github.com/Dansoftowner/jSystemThemeDetector) - MIT - Detect system
  theme (light/dark mode)

### Plugins

* [Android Application Plugin](https://developer.android.com/studio/build/gradle-plugin-3-0-0-migration) -
  Google - Plugin for building Android applications
* [Android Library Plugin](https://developer.android.com/studio/build/gradle-plugin-3-0-0-migration) -
  Google - Plugin for building Android libraries
* [Jetbrains Compose Plugin](https://github.com/JetBrains/compose-jb) - JetBrains - Plugin for
  Jetpack Compose
* [Kotlin Multiplatform Plugin](https://kotlinlang.org/docs/multiplatform.html) - JetBrains - Plugin
  for Kotlin Multiplatform projects
* [Kover Plugin](https://github.com/Kotlin/kotlinx-kover) - JetBrains - Code coverage tool for
  Kotlin
* [Ktlint Plugin](https://github.com/JLLeitschuh/ktlint-gradle) - JLLeitschuh - Plugin for Kotlin
  linter
* [Serialization Plugin](https://github.com/Kotlin/kotlinx.serialization) - JetBrains - Plugin for
  Kotlin serialization
* [Kotlin JVM Plugin](https://kotlinlang.org/docs/gradle.html) - JetBrains - Plugin for Kotlin JVM
  projects
* [BuildConfig Plugin](https://github.com/gmazzo/gradle-buildconfig-plugin) - gmazzo - Plugin for
  generating BuildConfig classes

<br /><br />

## Data Security and Privacy

First thing first: This app can run under the demo mode without requiring any credentials.

To pull real smart meter data from your Octopus Energy account, you need to generate an API key for
your account
at [https://octopus.energy/dashboard/new/accounts/personal-details/api-access](https://octopus.energy/dashboard/new/accounts/personal-details/api-access).
This app never asks for your Octopus customer account password, and you can always generate a new
API key to invalidate the old keys.

This app stores your API key, account number, MPAN and meter serial number
using `EncryptedSharedPreferences` on Android, or the Keychain on iOS. On desktop, these credentials
are currently unencrypted, but expected to do so when the library we use supports it.

<br /><br />

## License

```
   Copyright 2024 Ryan Wong, RW MobiMedia UK Limited, and open-source contributors

   Permission is hereby granted, free of charge, to any person obtaining a copy
   of this software and associated documentation files (the "Software"), to deal
   in the Software without restriction, including without limitation the rights
   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
   copies of the Software, and to permit persons to whom the Software is
   furnished to do so, subject to the following conditions:

   The above copyright notice and this permission notice shall be included in all
   copies or substantial portions of the Software.

   Additional License Terms:
   - Distributing the binary of this App on App Stores is prohibited.

   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
   SOFTWARE.

```
