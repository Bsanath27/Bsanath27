# Design Spec: Root Makefile Facade for SkyRide App
Date: 2026-03-31

## Goal
Create a `Makefile` in the project root (`claude/`) that delegates Android build and deployment commands to the existing `build/Makefile`. This provides a single entry point for developers working in the root directory.

## Architecture
The root `Makefile` will use the `-C` (change directory) flag of `make` to execute targets within the `build/` directory.

## Targets
The following targets will be exposed in the root `Makefile`:

| Target | Description | Delegation Command |
| :--- | :--- | :--- |
| `help` | Show available targets | `make -C build help` |
| `setup` | First-time setup (permissions, local.properties) | `make -C build setup` |
| `build` | Assemble debug APK | `make -C build build` |
| `install` | Build and install debug APK on device | `make -C build install` |
| `run` | Install and launch the app on device | `make -C build run` |
| `clean` | Clean build outputs | `make -C build clean` |
| `test` | Run all unit tests | `make -C build test` |
| `lint` | Run Android Lint | `make -C build lint` |
| `devices` | List connected devices | `make -C build devices` |
| `logcat` | Show app-specific logs | `make -C build logcat` |
| `open` | Open the project in Android Studio | `make -C build open` |

## Prerequisites
- The `build/` directory must contain a valid `Makefile`.
- `adb` must be in the system PATH for `run`, `install`, and `logcat` targets.
- `Android Studio` (or `studio` command) should be available for the `open` target.

## Success Criteria
- Running `make run` from the `claude/` directory successfully builds, installs, and launches the app on a connected phone.
- Running `make help` shows all delegated targets correctly.
