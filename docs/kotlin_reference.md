## Advanced usage

### Increasing the uniqueness of fingerprints

There is a probability that two different devices will have the same `fingerprint` value. There is also a probability that the same device will have different `fingerprint` values in different moments of time due to system upgrades or updated settings (although this should be infrequent).

By default, the library calculates a fingerprint with `StabilityLevel.OPTIMAL` as a `stabilityLevel` parameter, meaning optimal stability and uniqueness. But there are two more options you can use as well: `StabilityLevel.STABLE` and `StabilityLevel.UNIQUE`.

### Raw signals access and custom fingerprinting.

You can use `FingerprintingSignalsProvider` to access raw signals used for fingerprinting.
You can also calculate a fingerprint based only on the signals you want using `Fingerprinter.getFingerprint(fingerprintingSignals, hasher)` method:

```kotlin
// call this method from some worker thread
@WorkerThread
fun buildCustomFingerprint(fingerprinter: Fingerprinter): String {
    val signalsProvider = fingerprinter.getFingerprintingSignalsProvider()
    val neededSignals = listOf(
        signalsProvider.developmentSettingsEnabledSignal,
        signalsProvider.procCpuInfoV2Signal,
        signalsProvider.dateFormatSignal,
    )

    return fingerprinter.getFingerprint(
        fingerprintingSignals = neededSignals
    )
}
```

Note that this API is intentionally left synchronous, because it only provides the building blocks for you to build your fingerprint, leaving
you in a full control of this process.

### Change hash function

By default, the library uses [Murmur3 hash](https://en.wikipedia.org/wiki/MurmurHash) (64x128) which is fast and optimal for most cases.

If this hash function does not work for you, you can change it to a different one.

In order to do it, implement your own hasher, and pass it as a `hasher` parameter like shown below:

``` kotlin
fun someFunc(fingerprinter: Fingerprinter) {
    val hasher = object : Hasher {
        override fun hash(data: String): String {
            // Implement your own hashing logic, e.g. call SHA256 here
        }
    }

    fingerprinter.getFingerprint(
        version = IdentificationVersion.V_5,
        hasher = hasher
    ) { fingerprint ->
        // Do something with fingerprint
    }
}
```

### Backward compatibility

If you want to get a newer version of fingerprint, but also want to keep the old one for backward compatibility, you can get them both as shown below:

```kotlin
fun useMultipleVersions() {
    val fingerprinter = FingerprinterFactory.create(context)

    fingerprinter.getFingerprint(version = IdentificationVersion.V_4) { fingerprintV4 ->
        // Do something
    }

    fingerprinter.getFingerprint(version = IdentificationVersion.V_5) { fingerprintV5 ->
        // Do something
    }
}
```

### Incorporating with async programming frameworks

You can wrap our library API to use it with whatever asynchronous programming framework you prefer.
As a reference, [here](extensions.md) are some code snippets of incorporation our API with the popular frameworks .
