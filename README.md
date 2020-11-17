<p align="center">
  <a href="https://fingerprintjs.com">
    <img src="resources/logo.svg" alt="FingerprintJS" width="300px" />
  </a>
</p>

[![](https://jitpack.io/v/fingerprintjs/fingerprint-android.svg)](https://jitpack.io/#fingerprintjs/fingerprint-android)&nbsp;&nbsp;![Test](https://github.com/fingerprintjs/fingerprint-android/workflows/Test/badge.svg)&nbsp;&nbsp;[![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=plastic)](https://android-arsenal.com/api?level=19)
# fingerprint android

Lightweight library for device identification and fingerprinting.

Fully written in Kotlin. 100% Crash-free. 

Creates a user identifier from all available platform signals.

The identifier is fully stateless and will remain the same after reinstalling or clearing application data.

# Table of Contents
1. [Quick start](#quick-start)
2. [API Reference](#api-reference)
3. [Initialization and usage](#initialization-and-usage)
4. [Advanced usage](#advanced-usage)


## Quick start

### Install as Gradle dependency

Add these lines to `build.gradle` file of your project.


```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }	
	}
}
```

### Add the dependency to your project

Add these lines to `build.gradle` file of your module.
This library depends on `kotlin-stdlib`. If your application is written in Java, add `kotlin-stdlib` dependency first (it's lightweight and has excellent backward and forward compatibility).

```gradle
dependencies {
	implementation 'com.github.fingerprintjs:fingerprint-android:1.0.0'
	
	// Add this line for java-project
	implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"	
}
```

## API Reference

### Device ID and Fingerprint

The library operates with two entities. 

1. `deviceId` - it is an ID of the device, based on system-provided IDs. Currently, it is based on Google Service Framework ID, and ANDROID_ID if the first is unavailable. It's strict, which means the uniqueness of the ID for every device. Also, it keeps the same after application reinstalling.
2. `fingerprint` - it is a digital fingerprint of a device. Basically, it is a hash, calculated from a lot of available platform signals. `fingerprint` **is not a strict ID**, so there is a probability that two different devices will have the same fingerprint. Also, there is a probability, that the same device will have a different fingerprint in different moments due to system update, settings changing, etc.

#### Why do we need them both?
It is a good question, most of use-cases can be covered with the `deviceId`. But the `GSF_ID` and `ANDROID_ID`can be spoofed much easier, than device data such as CPU info or sensors list. 

Another reason for using `fingerprint` is a possible restriction of access to `deviceId` due to privacy policy changes trend.



### Initialization and usage

Initialize the library and get `fingerprint` and `deviceId`.

In Kotlin

```kotlin

// Initialization
val fingerprinter =
            FingerprinterFactory.getInitializedInstance(applicationContext)

// Simple usage
val fingerprint = fingerprinter.fingerprint()
val deviceId = fingerprinter.deviceId()

```

In Java

```java

// Initialization
Fingerprinter fingerprinter =
                FingerprinterFactory.getInitializedInstance(getApplicationContext());
                
// Simple usage
String fingerprint = fingerprinter.fingerprint();
String deviceId = fingerprinter.deviceId();

```
### Advanced usage
The additional entities, provided by the library are `BaseFingerprinter` and `Configuration`.

#### BaseFingerprinter

`fingerprint` can be calculated using different platform signals. They are combined in several groups:

1. Hardware fingerprint. (e.g. CPU info, sensors list etc.)
2. Os Build fingerprint (the information about current ROM, its version etc.)
3. Device state fingerprint (the information about basic settings of the device)
4. Installed applications fingerprint


Each of them has a class, which is inherited from `BaseFingerprinter` class.

```kotlin

abstract class BaseFingerprinter<T> (
    val version: Int
) {
    abstract fun calculate(): String
    abstract fun rawData(): T
}

```

So the full public API of the library is following:

```kotlin
interface Fingerprinter {
    fun deviceId(): String

    fun fingerprint(): String

    fun fingerprint(mask: Int): String

    fun deviceIdProvider(): DeviceIdProvider

    fun hardwareFingerprinter(): HardwareFingerprinter

    fun osBuildFingerprinter(): OsBuildFingerprinter

    fun installedAppsFingerprinter(): InstalledAppsFingerprinter

    fun deviceStateFingerprinter(): DeviceStateFingerprinter
}
```

`DeviceIdProvider` allows you to manually get `GSF_ID` or `ANDROID_ID`:
```kotlin
interface DeviceIdProvider {
    fun getDeviceId(): String
    fun getAndroidId(): String
    fun getGsfId(): String?
}
```

#### Example: BaseFingerprinter usage and raw data access

To get separate fingerprints you can directly call fingerprinter methods, such as `calculate()` for fingerprint calculation and `rawData` for access to raw data.

Here is the example for Hardware Fingerprinter:

```kotlin

val hardwareFingerprinter = fingerprinter.hardwareFingerprinter()
val hardwareFingerprint  = fingerprinter.calculate()

val rawData = hardwareFingerprinter.rawData()

val manufacturerName = rawData.manufacturerName
val modelName = rawData.modelName
val cpuInfo = rawData.cpuInfo

...

```

#### Custom fingerprint

The library has a public method for custom fingerprint:

```kotlin

fun fingerprint(mask: Int): String

```

Mask is a bit mask from existing values:

```kotlin

object Type {
    @JvmField
    val HARDWARE = 1

    @JvmField
    val OS_BUILD = 1 shl 1

    @JvmField
    val INSTALLED_APPS = 1 shl 2

    @JvmField
    val DEVICE_STATE = 1 shl 3
}

```

#### Example: Custom fingerprint with Installed Applications

By default `Fingerprinter` calculates `fingerprint` using Hardware, Os Build and Device state fingerprints.

One particular fingerprint that adds a lot of entropy is an "installed apps fingerprint", it adds a lot of identification accuracy but can be unstable. 

To use all available fingerprinters for fingerprint calculation call the method as below.

```
fingerprinter.fingerprint(
                    Type.HARDWARE or
                    Type.OS_BUILD or
                    Type.INSTALLED_APPS or
                    Type.DEVICE_STATE
                )
```

Choose one that works for you, if the default doesn't.


#### Configuration

Nothing is perfect, and the current implementation of fingerprints is not an exception. It will be improving over time.

Due to the prevention of breaking changes and supporting backward compatibility the library has a versioning mechanism, which works by setting a version for each `BaseFingerprinter`.

It is implemented with a configuration class which is the following:

```kotlin
data class Configuration @JvmOverloads constructor(
    val hardwareFingerprintVersion: Int = HARDWARE_FINGERPRINTER_DEFAULT_VERSION,
    val osBuildFingerprintVersion: Int = OS_BUILD_FINGERPRINTER_DEFAULT_VERSION,
    val deviceStateFingerprintVersion: Int = DEVICE_STATE_FINGERPRINTER_DEFAULT_VERSION,
    val installedAppsFingerprintVersion: Int = INSTALLED_APPS_FINGERPRINTER_DEFAULT_VERSION,
    val hasherType: HasherType = HasherType.MurMur3
)

```

Also, there is an ability to add more hash-functions. The only supported and default at the moment hash-function is MurMur3 (64x128) - fast and light hash function. The list will be extended in the future.

#### Example: Backward compatibility of an old fingerprint

For example, let's look at a situation where a new version of hardwareFingerprint has appeared, but the default version has not changed, and you need to have both of them:

```kotlin

val defaultFingerprinter = FingerprinterFactory.getInitializedInstance(applicationContext)
val defaultFingerprint = defaultFingerprinter.fingerprint()

val newConfiguration = Configuration(hardwareFingerprintVersion = 2)
val newFingerprinter =
    FingerprinterFactory.getInitializedInstance(applicationContext, newConfiguration)
    
val newFingerprint = newFingerprinter.fingerprint()
```


## Playground App

Try all the library features in the [Playground App](https://github.com/fingerprintjs/fingerprint-android/releases/download/1.0.0/Playground-release-1.0.0.apk).

## Android API support
fingerprint-android supports API versions from 19 (Android 4.4) and higher.


## Contributing

Please respect the versioning of fingerprint. If the fingerprint has version in main branch - all changes are not welcome. Some people can rely on these fingerprints in their security solutions. Create a new version of a fingerprint instead. 

Use ```feature-*``` branches for contributing.

Code style - default for Kotlin.

