# Youki DEX Simple — No Accessibility

A minimal Android HOME launcher inspired by Youki DEX. It intentionally has no Accessibility Service, Shizuku, root, overlay, notification listener, or external libraries.

## What it does
- Opens directly to a simple DeX-style desktop.
- Lists installed launcher apps.
- Tapping an icon launches the app normally.
- Can be selected as the Android default Home app.
- Uses only the Android SDK; no third-party runtime dependencies.

## What it does not do
- It does not force other apps into floating windows.
- It does not use Accessibility to control other apps.
- It does not enable Android freeform windows by itself.

## Build
Open in Android Studio or run `./gradlew assembleDebug` with internet access. The APK is `app/build/outputs/apk/debug/app-debug.apk`.
