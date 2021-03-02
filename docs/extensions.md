**RxJava extensions**

Copy extensions in the project and use as shown below.

```kotlin

fun Fingerprinter.fingerprintObservable(
        stabilityLevel: StabilityLevel = StabilityLevel.OPTIMAL
) = Observable.create(ObservableOnSubscribe<FingerprintResult> { emitter ->
    this.getFingerprint(stabilityLevel) { fingerprintResult ->
        emitter.onNext(fingerprintResult)
        emitter.onComplete()
    }
})

fun Fingerprinter.deviceIdObservable() = Observable
	.create(ObservableOnSubscribe<DeviceIdResult> { emitter ->
    this.getDeviceId { deviceIdResult ->
        emitter.onNext(deviceIdResult)
        emitter.onComplete()
    }
})

```

Usage:

```kotlin

val fingerprinter = FingerprinterFactory
                .getInstance(this, Configuration(version = 2))

fingerprinter.fingerprintObservable()
        .subscribe { fingerprintResult ->
            val fingerprint = fingerprintResult.fingerprint
        }


fingerprinter
        .deviceIdObservable()
        .subscribe { deviceIdResult ->
            val deviceId = deviceIdResult.deviceId
        }


```

**Kotlin coroutines extensions**

```kotlin

suspend fun Fingerprinter.fingerprint(stabilityLevel: StabilityLevel = StabilityLevel.OPTIMAL): FingerprintResult {
    return suspendCancellableCoroutine { continuation ->
        getFingerprint(stabilityLevel) { fingerprintResult ->
            continuation.resume(fingerprintResult)
        }
    }
}

suspend fun Fingerprinter.deviceId(): DeviceIdResult {
    return suspendCancellableCoroutine { continuation ->
        getDeviceId { deviceIdResult ->
            continuation.resume(deviceIdResult)
        }
    }
}

```


Usage:

```kotlin

val fingerprinter = FingerprinterFactory
                .getInstance(this, Configuration(version = 2))

...

launch {
    val fingerprint = fingerprinter.fingerprint().fingerprint
    val deviceId = fingerprinter.deviceId().deviceId
}

```