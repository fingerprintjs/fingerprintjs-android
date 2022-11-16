## Changes in major version 2

### Ditching global `Configuration` class
In versions 1.* of the library the configuration was passed to the factory method `FingerprinterFactory.getInstance(context, configuration)`.
If you wanted to get multiple fingerprints of different versions, (e.g. in order to migrate to the newer version of fingerprint), you had to call `getInstance` method twice, which is both ineffective and inconvenient.

In the major version 2, the version is passed to `getDeviceId(..)` and `getFingerprint(..)` methods.

### Ditching `SignalGroupProvider` class
In versions 1.* of the library if you wanted to retrieve any particular signal involved in fingerprinting, you had to do the following:
1. Call `Fingerprinter.getFingerprint(stabilityLevel, listener)` to retrieve `FingerprintResult`
2. Call  `FingerprintResult.getSignalProvider` to retrieve a signal provider that signal belongs to, e.g., `HardwareSignalGroupProvider`
3. Get `RawData` from that particular signal provider, e.g., `HardwareFingerprintRawData`
4. Get signal from that `RawData`, e.g., `HardwareFingerprintRawData.manufacturerName()`

To do the same thing in major version 2, you have to do the following:
1. Call `Fingerprinter.getFingerprintingSignalsProvider()`
2. Call `fingerprintingSignalsProvider.manufacturerNameSignal`

This approach requires less code and does not involve unnecessary fingerprint calculations.
Also, it allows to reduce the complexity of the code base by making signal's hierarchy *flat*, i.e. not dividing signals into different signal groups based on their nature.

### Introducing API for custom fingerprinting
The new method `Fingerprinter.getFingerprint(fingerprintingSignals, hasher)` allows you to create fingerprints based on any set of signals. In order to get signals first, use `FingerprintingSignalsProvider` class. See the example in the [API reference](https://github.com/fingerprintjs/fingerprintjs-android/blob/release/2.0.0/docs/api_reference.md#raw-signals-access-and-custom-fingerprinting)

### Changing behaviour of the factory method
Not only we have removed `configuration` parameter from the factory method, but also changed it's behaviour. This is why now we have `FingerprinterFactory.create(context)` method instead of `FingerprinterFactory.getInstance(context, configuration)` method.
Whereas the latter method always returned the same instance given the same `configuration`, the former always returns a new instance of `Fingerprinter`. This may be useful if you want to calculate the fingerprint from scratch multiple times. This cannot be possible with a reinstantiation of `Fingerprinter`, because fingerprinting signals inside it are cached internally.

### Caveats
Even though all the changes listed above are aimed towards making the API both more flexible and more convenient at the same time, there is one scenario of the API v1 usage that cannot be easily migrated to API v2.

If you intentionally operated with a whole *group* of signals using `SignalGroupProvider.fingerprint(..)` method or `RawData.signals()` method, then there is no alternative "one-liner" to do the same thing in API v2, because we decided to get rid of of signals grouping, as described in [this](#ditching-signalgroupprovider-class) section. Therefore, the only alternative when using API v2 is to manually get all the signals you want using `FingerprintingSignalsProvider` and then do with them whatever is needed, e.g. [calculate a custom fingerprint](https://github.com/fingerprintjs/fingerprintjs-android/blob/release/2.0.0/docs/api_reference.md#raw-signals-access-and-custom-fingerprinting)
