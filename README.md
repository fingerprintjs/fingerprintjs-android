<p align="center">
    <picture>
      <source media="(prefers-color-scheme: dark)" srcset="resources/logo_light.svg" />
      <source media="(prefers-color-scheme: light)" srcset="resources/logo_dark.svg" />
      <img src="resources/logo_dark.svg" alt="Fingerprint logo" width="312px" />
    </picture>
</p>

<p align="center">
  <a href="https://jitpack.io/#fingerprintjs/fingerprint-android">
    <img src="https://jitpack.io/v/fingerprintjs/fingerprint-android.svg" alt="Latest release">
  </a>
  <a href="https://github.com/fingerprintjs/fingerprint-android/actions?workflow=Test">
    <img src="https://github.com/fingerprintjs/fingerprint-android/workflows/Test/badge.svg" alt="Build status">
  </a>
  <a href="https://android-arsenal.com/api?level=21">
    <img src="https://img.shields.io/badge/API-21%2B-brightgreen.svg" alt="Android minAPI status">
  </a>
</p>

<p align="center">
  <a href="https://discord.gg/39EpE2neBg">
    <img src="https://img.shields.io/discord/852099967190433792?style=for-the-badge&label=Discord&logo=Discord&logoColor=white" alt="Discord server">
  </a>
</p>

<p align="center">
 	<a href='https://play.google.com/store/apps/details?id=com.fingerprintjs.android.playground'>
 		<img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png' width="240px/>
 	</a>
 </p>

<p align="center">
    <img src="resources/playground-app.png" alt="PlaygroundApp" width="780px" />
</p>			

# FingerprintJS Android

Lightweight library for device identification and fingerprinting.

Fully written in Kotlin. **100% Crash-free**. 

Creates a device identifier from all available platform signals.
									     								     
The identifier is fully stateless and will remain the same after reinstalling or clearing application data. 

[Check the FingeprintJS iOS](https://github.com/fingerprintjs/fingerprintjs-ios) – an iOS library for device fingerprinting.

## Table of Contents
1. [Quick start](#quick-start)
2. [Usage](#3-get-deviceids-and-fingerprints)
3. [Playground App](#playground-app)


## Quick start

### 1. Add repository

Add these lines to your `build.gradle`.


```gradle
allprojects {	
  repositories {
  ...
  maven { url 'https://jitpack.io' }	
}}
```

### 2. Add dependency

Add these lines to `build.gradle` of a module.

This library depends on [kotlin-stdlib](https://kotlinlang.org/api/latest/jvm/stdlib/).

If your application is written in Java, add `kotlin-stdlib` dependency first (it's lightweight and has excellent backward and forward compatibility).

```gradle
dependencies {
  // Add this line only if you use this library with Java
  implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"

  implementation "com.github.fingerprintjs:fingerprint-android:1.3.0"
}


```


#### deviceId vs fingerprint

The library operates with two entities. 

1. `deviceId` - is a random and unique device identifier.

Can be used by developers to identify devices to deliver personalized content, detect suspicious activity, and perform fraud detection.
Internally it will use Google Service Framework ID if it's available and ANDROID_ID, if GSF ID is not available. 
This identifier is stable, i.e. it will remain the same even after reinstalling your app. 
But it will be different after factory reset of the device.

2. `fingerprint` is a digital device fingerprint. It works by combining all available device signals and attributes into a single identifier. There is a probability that two identical devices will have the same `fingerprint`.


#### Which one should I use?

`deviceId` is guaranteed to be random and should be your first choice for device identification. This identifier can be spoofed though and shouldn't be used in security-focused or fraud detection scenarios.

`fingerprint` is much harder to spoof and is a safer choice in security-focused use cases.

See the [table](docs/stability.md) about stability of each.



### 3. Get deviceIDs and fingerprints

Kotlin

```kotlin

// Initialization
 val fingerprinter = FingerprinterFactory
		.getInstance(applicationContext, Configuration(version = 4))


// Usage
fingerprinter.getFingerprint { fingerprintResult ->
  val fingerprint = fingerprintResult.fingerprint
}

fingerprinter.getDeviceId { result ->
  val deviceId = result.deviceId
}


```

Java

```java

// Initialization
Fingerprinter fingerprinter = FingerprinterFactory
				.getInstance(getApplicationContext(), new Configuration(4));


// Usage
fingerprinter.getFingerprint(new Function1<FingerprintResult, Unit>() {
        @Override
        public Unit invoke(FingerprintResult fingerprintResult) {
        	String fingerprint = fingerprintResult.getFingerprint();
        	    return null;
            }
        });
        
fingerprinter.getDeviceId(new Function1<DeviceIdResult, Unit>() {
            @Override
            public Unit invoke(DeviceIdResult deviceIdResult) {
            	String deviceId = deviceIdResult.getDeviceId();
                return null;
            }
        });

```

`getFingerprint` and `getDeviceId` methods execute on a separate thread. Keep this in mind when using results on the main thread.

Also the results are cached, so subsequent calls will be faster. TODO: this must be deleted.

## Versioning

`fingerprint` is versioned incrementatlly; the version should be set explicitly to avoid unexpected `fingerprint` changes when updating the library.

The `version` is set while the initialization of the library with `Configuration` class.

```kotlin

val fingerprinter = FingerprinterFactory
		.getInstance(applicationContext, Configuration(version = 4))

```

See full [Kotlin reference](docs/kotlin_reference.md)/[Java reference](docs/java_reference.md).

## Playground App

Try all the library features in the [Playground App](https://github.com/fingerprintjs/fingerprint-android/releases/download/1.2/Playground-release-1.2.apk).

## Android API support

fingerprint-android supports API versions from 21 (Android 5.0) and higher.


## Contributing
Feel free to ask questions and request features.
Just create an issue with a clear explanation of what you'd like to have in the library.
For code contributions, please see the [contributing guideline](docs/contributing.md).

## Testimonials

>Just tested on HUAWEI Y6p, with factory reset, we can retrieve the same result with `StabilityLevel.STABLE` setting.
>
>Thank you
>
> **_GitHub user_**

## License

This library is MIT licensed.
Copyright FingerprintJS, Inc. 2020-2022.

