CRBT  App
==================

Heyy, this is the repository for the **CRBT** android app. It is a **work in progress** 🚧.

**CRBT** is a fully functional Android app built entirely with Kotlin and Jetpack Compose. Just.
Like. That!
It follows Android design and development best practices. Its a classic, I'm telling you.

# Features

**CRBT** provides access to Ethio-Telecom service including, CRBT song subscription, packages
purchasing, airtime recharging & more. I did learn a lot of USSD handling while cooking this bad
boy.

# Development Environment

**CRBT** uses the Gradle build system and can be imported directly into Android Studio. Ha! you like
that?

Change the run configuration to `app`.

![image](https://user-images.githubusercontent.com/873212/210559920-ef4a40c5-c8e0-478b-bb00-4879a8cf184a.png)

The `demoDebug` and `demoRelease` build variants can be built and run on any device or emulator.

![image](https://user-images.githubusercontent.com/873212/210560507-44045dc5-b6d5-41ca-9746-f0f7acf22f8e.png)

# Architecture

The **CRBT** app follows
the [official architecture guidance](https://developer.android.com/topic/architecture). I'm not
playing around here.

# Modularization

The **CRBT** app has been fully modularized, find the detailed guidance and description of the
modularization strategy used in [modularization learning journey](docs/CRBTModularization.md).

# Build

The app contains the usual `debug` and `release` build variants.

In addition, the `benchmark` variant of `app` will be used to test startup performance and generate
a baseline profile. (I'm yet to do this)

# Testing

I haven't written any tests yet. I will be writing tests soon. Honestly, I don't know if I would
find the time to that.

However, to facilitate testing of components, **CRBT** uses dependency injection
with [Hilt](https://developer.android.com/training/dependency-injection/hilt-android).

# UI

This app was designed using [Material 3 guidelines](https://m3.material.io/), obviously.

The Screens, elements all of it are built entirely
using [Jetpack Compose](https://developer.android.com/jetpack/compose).

The app has two themes:

- Dynamic color - uses colors based on
  the [user's current color theme](https://material.io/blog/announcing-material-you) (if supported)
- Default theme - uses predefined colors when dynamic color is not supported

Each theme also supports dark mode as it should be. It is LAW!

# Performance

## Benchmarks

Yes, there's the module but its void. I hate to break it to you. I will be writing benchmarks soon.
I promise.
Anyways, find all tests written
using [`Macrobenchmark`](https://developer.android.com/topic/performance/benchmarking/macrobenchmark-overview)
in the `benchmarks` module. I also intend to include in this module the test to generate the
Baseline profile.

# License

Well, I have been given this yet, except for the cash i took to build this guy.
