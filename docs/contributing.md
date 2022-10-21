# Contributing to fingerprint-android

## Working with the code

To get started with a project, just open it using Android Studio after cloning.

### How to build

To build the library, run:

```bash
./gradlew fingerprint:assemble
```

The files will appear at `fingerprint/build/outputs/aar`.

To build the Playground app, run:

```bash
./gradlew app:assemble
```

Also you can perform these actions with Android Studio.

### How to test

At the moment, library contains unit-tests, instrumented tests and lint-checks. To perform the checks, run:

```bash
./gradlew fingerprint:lint
./gradlew fingerprint:testDebug
./gradlew fingerprint:connectedCheck
```

### Code style

The code style is the default for _Kotlin_.

Also, add 2 lines of indentation before and after import sections.
