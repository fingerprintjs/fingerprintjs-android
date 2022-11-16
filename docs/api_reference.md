## API Reference

The following API reference corresponds to versions 2.0.0 and above. If you update from 1.* please follow the [migration guide](migration_to_v2.md).

### Public API

```kotlin
public class Fingerprinter internal constructor(
    ...
) {


    public fun getDeviceId(version: Version, listener: (DeviceIdResult) -> Unit) { ... }

    @JvmOverloads
    public fun getFingerprint(
        version: Version,
        stabilityLevel: StabilityLevel = StabilityLevel.OPTIMAL,
        hasher: Hasher = MurMur3x64x128Hasher(),
        listener: (String) -> (Unit),
    ) { ... }

    @WorkerThread
    @JvmOverloads
    public fun getFingerprint(
        fingerprintingSignals: List<FingerprintingSignal<*>>,
        hasher: Hasher = MurMur3x64x128Hasher(),
    ): String { ... }


    public fun getFingerprintingSignalsProvider(): FingerprintingSignalsProvider { ... }

    public enum class Version(
        internal val intValue: Int
    ) {
        V_1(intValue = 1),
        V_2(intValue = 2),
        V_3(intValue = 3),
        V_4(intValue = 4),
        V_5(intValue = 5);

        public companion object {
            public val latest: Version
                get() = values().last()
        }
    }
}

public data class DeviceIdResult(
    val deviceId: String,
    val gsfId: String,
    val androidId: String,
    val mediaDrmId: String,
)

```

### Increasing the uniqueness of fingerprints

There is a probability that two different devices will have the same `fingerprint` value. There is also a probability that the same device will have different `fingerprint` values in different moments of time due to system upgrades or updated settings (although this should be infrequent).

By default, the library calculates a fingerprint with `StabilityLevel.OPTIMAL` as a `stabilityLevel` parameter, meaning optimal stability and uniqueness. But there are two more options you can use as well: `StabilityLevel.STABLE` and `StabilityLevel.UNIQUE`.

### Raw signals access and custom fingerprinting.

You can use `FingerprintingSignalsProvider` to access raw signals used for fingerprinting.
You can also calculate a fingerprint based only on the signals you want using `Fingerprinter.getFingerprint(fingerprintingSignals, hasher)` method:

Kotlin:
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
Java:
```java
// call this method from some worker thread
@WorkerThread
String buildCustomFingerprint(Fingerprinter fingerprinter) {
    FingerprintingSignalsProvider signalsProvider = fingerprinter.getFingerprintingSignalsProvider();
    List<FingerprintingSignal<?>> neededSignals = new ArrayList<>();
    neededSignals.add(signalsProvider.getDevelopmentSettingsEnabledSignal());
    neededSignals.add(signalsProvider.getProcCpuInfoV2Signal());
    neededSignals.add(signalsProvider.getDateFormatSignal());

    return fingerprinter.getFingerprint(neededSignals);
}
```

Note that this API is intentionally left synchronous, because it only provides the building blocks for you to build your fingerprint, leaving
you in a full control of this process.

### Change hash function

By default, the library uses [Murmur3 hash](https://en.wikipedia.org/wiki/MurmurHash) (64x128) which is fast and optimal for most cases.

If this hash function does not work for you, you can change it to a different one.

In order to do it, implement your own hasher, and pass it as a `hasher` parameter like shown below:

Kotlin:
``` kotlin
fun someFunc(fingerprinter: Fingerprinter) {
    val hasher = object : Hasher {
        override fun hash(data: String): String {
            // Implement your own hashing logic, e.g. call SHA256 here
        }
    }

    fingerprinter.getFingerprint(
        version = Fingerprinter.Version.V_5,
        hasher = hasher
    ) { fingerprint ->
        // Do something with fingerprint
    }
}
```
Java:
```java
void someFunc(Fingerprinter fingerprinter) {
    Hasher hasher = new Hasher() {
        @NonNull
        @Override
        public String hash(@NonNull String data) {
            // Implement your own hashing logic, e.g. call SHA256 here
        }
    };

    fingerprinter.getFingerprint(Fingerprinter.Version.V_5, StabilityLevel.OPTIMAL, hasher, fingerprint -> {
        // Do something with fingerprint
        return null;
    });
}
```

### Backward compatibility

If you want to get a newer version of fingerprint, but also want to keep the old one for backward compatibility, you can get them both as shown below:

Kotlin:
```kotlin
fun useMultipleVersions() {
    val fingerprinter = FingerprinterFactory.create(context)

    fingerprinter.getFingerprint(version = Fingerprinter.Version.V_4) { fingerprintV4 ->
        // Do something
    }

    fingerprinter.getFingerprint(version = Fingerprinter.Version.V_5) { fingerprintV5 ->
        // Do something
    }
}
```
Java:
```java
void useMultipleVersions() {
    Fingerprinter fingerprinter = FingerprinterFactory.create(context);
    
    fingerprinter.getFingerprint(Fingerprinter.Version.V_4, fingerprintV4 -> {
        // Do something
        return null;
    });


    fingerprinter.getFingerprint(Fingerprinter.Version.V_5, fingerprintV5 -> {
        // Do something
        return null;
    });
}
```

### Incorporating with async programming frameworks

You can wrap our library API to use it with whatever asynchronous programming framework you prefer.
As a reference, [here](extensions.md) are some code snippets of incorporation our API with the popular frameworks .
