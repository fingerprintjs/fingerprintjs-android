**RxJava extensions**

Copy extensions in the project and use as shown below.

```kotlin

fun Fingerprinter.fingerprintObservable(
    version: IdentificationVersion,
    stabilityLevel: StabilityLevel = StabilityLevel.OPTIMAL,
    hasher: Hasher = MurMur3x64x128Hasher()
) = Observable.create(ObservableOnSubscribe<String> { emitter ->
    this.getFingerprint(version, stabilityLevel, hasher) { fingerprint->
        emitter.onNext(fingerprint)
        emitter.onComplete()
    }
})

fun Fingerprinter.deviceIdObservable(version: IdentificationVersion) = Observable
	.create(ObservableOnSubscribe<DeviceIdResult> { emitter ->
    this.getDeviceId(version) { deviceIdResult ->
        emitter.onNext(deviceIdResult)
        emitter.onComplete()
    }
})

```

Usage:

```kotlin

val fingerprinter = FingerprinterFactory.create(context)

fingerprinter
        .fingerprintObservable(version = IdentificationVersion.V_5)
        .subscribe { fingerprint ->
            // Your code
        }

fingerprinter
        .deviceIdObservable(version = IdentificationVersion.V_5)
        .subscribe { deviceIdResult ->
            val deviceId = deviceIdResult.deviceId
            // Your code
        }


```

**Kotlin coroutines extensions**

```kotlin

suspend fun Fingerprinter.fingerprint(
    version: IdentificationVersion,
    stabilityLevel: StabilityLevel = StabilityLevel.OPTIMAL,
    hasher: Hasher = MurMur3x64x128Hasher()
): String {
    return suspendCancellableCoroutine { continuation ->
        getFingerprint(version, stabilityLevel, hasher) { fingerprint->
            continuation.resume(fingerprint)
        }
    }
}

suspend fun Fingerprinter.deviceId(version: IdentificationVersion): DeviceIdResult {
    return suspendCancellableCoroutine { continuation ->
        getDeviceId(version) { deviceIdResult ->
            continuation.resume(deviceIdResult)
        }
    }
}

```


Usage:

```kotlin

val fingerprinter = FingerprinterFactory.create(context)

...

launch {
    val fingerprint = fingerprinter.fingerprint(IdentificationVersion.V_5)
    val deviceId = fingerprinter.deviceId(IdentificationVersion.V_5).deviceId
}

```