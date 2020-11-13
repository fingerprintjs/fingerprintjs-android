<p align="center">
  <a href="https://fingerprintjs.com">
    <img src="resources/logo.svg" alt="FingerprintJS" width="300px" />
  </a>
</p>

[![](https://jitpack.io/v/fingerprintjs/fingerprint-android.svg)](https://jitpack.io/#fingerprintjs/fingerprint-android)
# fingerprint android

Lightweight library for device identification and fingerprinting.

Fully written in Kotlin. 100% Crash-free. 

Creates a user identifier from all available platform signals.

The identifier is fully stateless and will remain the same after reinstalling or clearing application data.


## Quick start

### Install as Gradle dependency


```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }	
	}
}
```

### Add the dependency to your project

This library depends on `kotlin-stdlib`. If your application is written in Java, add `kotlin-stdlib` dependency first (it's lightweight and has excellent backward and forward compatibility).

```gradle
dependencies {
	implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
	implementation 'com.github.fingerprintjs:fingerprint-android:1.0.0'
}
```

## API Reference

### Initialization & usage

Initialize the library and get `fingerprint` and `deviceId`. 
Consider not to mix deviceId and fingerprint in case the deviceId will not be accessible due to privacy policy changes.

In Kotlin
```kotlin

// Initialization
val fingerprintAndroidAgent =
            FingerprintAndroidAgentFactory.getInitializedInstance(applicationContext)

// Simple usage
val fingerprint = fingerprintAndroidAgent.getFingerprint()
val deviceId = fingerprintAndroidAgent.deviceId()

```

In Java
```java

// Initialization
FingerprintAndroidAgent fingerprintAndroidAgent =
                FingerprintAndroidAgentFactory.getInitializedInstance(getApplicationContext());
                
// Simple usage
String fingerprint = fingerprintAndroidAgent.getFingerprint(
	FingerprintAndroidAgent.HARDWARE | 
	FingerprintAndroidAgent.OS_BUILD |
	FingerprintAndroidAgent.DEVICE_STATE
);

String deviceId = fingerprintAndroidAgent.deviceId();

```
#### Advanced usage

Agent has a configuration class. Use it when your preferred version of fingerprints or hash type are distinct from default.

```kotlin
data class FingerprintAndroidConfiguration(
    val hardwareFingerprintVersion: Int,
    val osBuildFingerprintVersion: Int,
    val deviceStateFingerprintVersion: Int,
    val installedAppsFingerprintVersion: Int,
    val hasherType: HasherType
)
```

Add your configuration during the initializaion:

```kotlin
val fingerprintAndroidAgent =
    FingerprintAndroidAgentFactory.getInitializedInstance(applicationContext, configuration)
```

Also you can set up fingerprint calculation process. 
By default it depends on three fingerprints:

1. Hardware fingerprint
2. Os Build fingerprint
3. Device state fingerprint

One particular fingerprint that adds a lot of entropy is an "installed apps fingerprint", it adds a lot of identification accuracy but can be unstable. 

To use all available fingerprinters for fingerprint calculation call the method as below.

```
fingerprintAgent.getFingerprint(
                    FingerprintType.HARDWARE or
                    FingerprintType.OS_BUILD or
                    FingerprintType.INSTALLED_APPS or
                    FingerprintType.DEVICE_STATE
                )
```

Choose one that works for you!

#### 3. Raw data access

To get separate fingerprints you can directly call fingerprinter methods.
Here is the example for hardware fingerprinter:

```kotlin

val hardwareFingerprinter = fingerprintAgent.hardwareFingerprinter()
val hardwareFingerprint  = hardwareFingerprinter.calculate()

val rawData = hardwareFingerprinter.rawData()

val manufacturerName = rawData.manufacturerName
val modelName = rawData.modelName
val cpuInfo = rawData.cpuInfo

...

```


## Playground App

Try all the library features in the [Playground App](https://github.com/fingerprintjs/fingerprint-android/releases/download/1.0.0/Playground-release-1.0.0.apk).

## Android API support
fingerprint-android supports API versions from 19 (Android 4.4) and higher.


## Contributing

Please respect the versioning of fingerprint. If the fingerprint has version in main branch - all changes are not welcome. Some people can rely on these fingerprints in their security solutions. Create a new version of a fingerprint instead. 

Use ```feature-*``` branches for contributing.

Code style - default for Kotlin.

