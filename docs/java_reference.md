# Java reference

## Advanced usage

### Increasing the uniqueness of fingerprints

There is a probability that two different devices will have the same `fingerprint` value. There is also a probability that the same device will have different `fingerprint` values in different moments of time due to system upgrades or updated settings (although this should be infrequent).

A device fingerprint can be calculated using various signals.
These signals are grouped into several categories:

1. Hardware signals (e.g. CPU info, sensors list etc.)
2. OS build signals & attributes (the information about current ROM, its version etc.)
3. Device state information (the information about some settings of the device)
4. Installed apps information (unstable signal source as apps get reinstalled all the time).

By default we only use signals from sources #1, #2, and #3, because this combination provides the best balance between fingerprint uniqueness and  stability.

You can increase the uniqueness by adding the installed apps signal source, but this will decrease the stability of fingerprints.


Example of how to use all available signal providers for fingerprint calculation. 
This will improve the uniqueness of the fingerprint, but also it will reduce the stability i.e. the `fingerprint` will change more frequently.

```java

int mask = SignalProviderType.HARDWARE |
		   SignalProviderType.OS_BUILD |
		   SignalProviderType.DEVICE_STATE |
		   SignalProviderType.INSTALLED_APPS

fingerprinter.getFingerprint(mask, new Function1<FingerprintResult, Unit>() {
            @Override
            public Unit invoke(FingerprintResult fingerprintResult) {
            	String fingerprint = fingerprintResult.getFingerprint();
                return null;
            }
        });

``` 
 

### Raw data access

If you need an access to raw data from signal providers, you can get it as shown below:

```java

fingerprinter.getFingerprint(new Function1<FingerprintResult, Unit>() {
            @Override
            public Unit invoke(FingerprintResult fingerprintResult) {
                String fingerprint = fingerprintResult.getFingerprint();

                HardwareSignalProvider hardwareSignalProvider = fingerprintResult
                        .getSignalProvider(HardwareSignalProvider::class.java);

                String hardwareFingerprint = hardwareSignalProvider.fingerprint();
                Map<String, String> cpuInfo = hardwareSignalProvider.rawData.getProcCpuInfo();

                return null;
            }
        });

```

Also you can get fingerprint for every signal provider.

Available signal providers classes are:

1. HardwareSignalProvider
2. OsBuildSignalProvider
3. DeviceStateSignalProvider
4. InstalledAppsSignalProvider

### Change hash function


The library uses [MurMur3 hash](https://en.wikipedia.org/wiki/MurmurHash) (64x128) which is fast and optimal for most cases.

If this hash function does not work for you, you can change it to a different one.

To do it, implement your own hasher, and pass it to `Configuration` class as shown below:

``` java

Hasher hasher = new Hasher() {
            @NotNull
            @Override
            public String hash(@NotNull String s) {
                // Implement
            }
        };

Fingerprinter fingerprinter = FingerprinterFactory.getInstance(
	getApplicationContext(),
	new Configuration(1, hasher)
 );



```

 
### Backward compatibility

If you want to get a newer version of fingerprint, but also want to keep the old one for backward compatibility, you can get them both as shown below:


```kotlin

Fingerprinter oldFingerprinter = FingerprinterFactory
			.getInstance(getApplicationContext(), Configuration(1));

Fingerprinter newFingerprinter = FingerprinterFactory
			.getInstance(getApplicationContext(), Configuration(2));


oldFingerprinter.getFingerprint(mask, new Function1<FingerprintResult, Unit>() {
            @Override
            public Unit invoke(FingerprintResult fingerprintResult) {
            		String oldFingerprint = fingerprintResult.getFingerprint();
                	return null;
            }
        });


newFingerprinter.getFingerprint(mask, new Function1<FingerprintResult, Unit>() {
            @Override
            public Unit invoke(FingerprintResult fingerprintResult) {
            		String newFingerprint = fingerprintResult.getFingerprint();
                	return null;
            }
        });


```
