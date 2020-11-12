<p align="center">
  <a href="https://fingerprintjs.com">
    <img src="resources/logo.svg" alt="FingerprintJS" width="300px" />
  </a>
</p>

[![](https://jitpack.io/v/fingerprintjs/fingerprint-android.svg)](https://jitpack.io/#fingerprintjs/fingerprint-android)
# fingerprint-android

Simple lightweight library for device-identifying and fingerprinting, fully written in Kotlin. Crash-free. 

Makes an application user identifier from available identifiers and device fingerprints.
Unlike cookies and local storage, these identifiers remain the same after reinstalling or clear application data.

Enjoy!


## Quick start

### Install as gradle dependency

1. Add repository to your project

```gradle
allprojects {
	repositories{
			...
			maven { url 'https://jitpack.io' }	
		}
}
```

2. Add the dependency to the project. Also the library depends on kotlin-stdlib library So if your application is written in java - please add dependency for kotlin-stdlib. It's lightweight and has great forward and backward compatibility.

```gradle
dependencies {
	implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
	implementation 'com.github.fingerprintjs:fingerprint-android:v1.0.0'
}
```

3. The fingerprint-android methods is accessible inside of your project.


## Open-source version reference


### API
#### 1. Simple Usage

Simply initialize the library, and get fingerprint and deviceId. 
Consider not to mix deviceId and fingerprint in case the deviceId will not be accessible due to privacy policy changes.

```kotlin

// Initialization
val fingerprintAndroidAgent =
            FingerprintAndroidAgentFactory.getInitializedInstance(applicationContext)

// Simple usage
val fingerprint = fingerprintAndroidAgent.getFingerprint()
val deviceId = fingerprintAndroidAgent.deviceId()

```

#### 2. Advanced

Agent has configuration class. Use it, when your preferred version of fingerprints of hash type distinct from default.

```kotlin
data class FingerprintAndroidConfiguration(
    val hardwareFingerprintVersion: Int,
    val osBuildFingerprintVersion: Int,
    val deviceStateFingerprintVersion: Int,
    val installedAppsFingerprintVersion: Int,
    val hasherType: HasherType
)
```

Add your configuration while initializaion.

```kotlin
val fingerprintAndroidAgent =
            FingerprintAndroidAgentFactory.getInitializedInstance(applicationContext, configuration)
```

Also you can set up fingerprint calculation process. 
By default it depends on three fingerprints:

1. Hardware fingerprint
2. Os Build fingerprint
3. Device state fingerprint

There is another one fingerprint, that adds a lot of entropy, but can be unstable - Installed applications fingerprint. 

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
Example for hardware fingerprinter:

```kotlin

val hardwareFingerprinter = fingerprintAgent.hardwareFingerprinter()
val hardwareFingerprint  = hardwareFingerprinter.calculate()

val rawData = hardwareFingerprinter.rawData()

val manufacturerName = rawData.manufacturerName
val modelName = rawData.modelName
val cpuInfo = rawData.cpuInfo

...

```



## Android API support
fingerprint-android supports API versions from 19 (Android 4.4) and higher.



## Contributing

Please respect the versioning of fingerprint. If the fingerprint has version in main branch - all changes are not welcome. Some people can rely on these fingerprints in their security solutions. Create a new version of a fingerprint instead. 

Use ```feature-*``` branches for contributing.

Code style - default for Kotlin.

