## Advanced usage

Reference for Kotlin is provided below. [Java reference](docs/java_reference.md).

The full public API of the library is following:

```kotlin

interface Fingerprinter {
  fun getDeviceId(listener: (DeviceIdResult) -> (Unit))
  fun getFingerprint(listener: (FingerprintResult) -> (Unit))
  fun getFingerprint(stabilityLevel: StabilityLevel, listener: (FingerprintResult) -> (Unit))
}

interface FingerprintResult {
  val fingerprint: String
  fun <T> getSignalProvider(clazz: Class<T>): T?
}

data class DeviceIdResult(
  val deviceId: String,
  val gsfId: String?,
  val androidId: String,
  val mediaDrmId: String?
)

```

If you are using RxJava or Kotlin Coroutines - use the [extensions](docs/extensions.md).

### Increasing the uniqueness of fingerprints

There is a probability that two different devices will have the same `fingerprint` value. There is also a probability that the same device will have different `fingerprint` values in different moments of time due to system upgrades or updated settings (although this should be infrequent).

By default the library calculates a fingerprint with optimal stability and uniqueness. But also there are two more modes for fingerprints: Stable and Unique.

Use them as shown below:


```kotlin


fingerprinter.getFingerprint(StabilityMode.STABLE) { fingerprintResult ->
  val stableFingerprint = fingerprintResult.fingerprint
}

fingerprinter.getFingerprint(StabilityMode.OPTIMAL) { fingerprintResult ->
  val optimalFingerprint = fingerprintResult.fingerprint
}

fingerprinter.getFingerprint(StabilityMode.UNIQUE) { fingerprintResult ->
  val uniqueFingerprint = fingerprintResult.fingerprint
}

```

### Raw data access

If you need access to raw data from signal providers, you can get it as shown below:

```kotlin

fingerprinter.getFingerprint { fingerprintResult ->

  val hardwareSignalProvider = fingerprintResult
  			.getSignalProvider(HardwareSignalGroupProvider::class.java)

  val hardwareFingerprint = hardwareSignalProvider.fingerprint()

  val cpuInfo = hardwareSignalProvider.rawData().procCpuInfo()
}

```

### Change hash function

The library uses [Murmur3 hash](https://en.wikipedia.org/wiki/MurmurHash) (64x128) which is fast and optimal for most cases.

If this hash function does not work for you, you can change it to a different one.

To do it, implement your own hasher, and pass it to `Configuration` class as shown below:

``` kotlin

val hasher = object : Hasher {
  override fun hash(data: String): String {
    // Implement your own hashing logic, e.g. call SHA256 here
  }
}

val fingerprinter = FingerprinterFactory.getInstance(
  applicationContext,
  Configuration(version = 1, hasher = hasher)

)

```

### Backward compatibility

If you want to get a newer version of fingerprint, but also want to keep the old one for backward compatibility, you can get them both as shown below:

```kotlin

val v1Fingerprinter = FingerprinterFactory
		.getInstance(applicationContext, Configuration(version = 1))

val v2Fingerprinter = FingerprinterFactory
		.getInstance(applicationContext, Configuration(version = 2))


v1Fingerprinter.getFingerprint { fingerprintResult ->
  val v1Fingerprint = fingerprintResult.fingerprint
}

v2Fingerprinter.getFingerprint { fingerprintResult ->
  val v2Fingerprint = fingerprintResult.fingerprint
}

```