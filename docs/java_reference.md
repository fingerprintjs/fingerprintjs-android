# Java reference

## Advanced usage

### Increasing the uniqueness of fingerprints

There is a probability that two different devices will have the same fingerprint value. There is also a probability that the same device will have different fingerprint values in different moments of time due to system upgrades or updated settings (although this should be infrequent).

By default the library calculates a fingerprint with optimal stability and uniqueness. But also there are two more modes for fingerprints: Stable and Unique.

Use them as shown below:

```java


fingerprinter.getFingerprint(StabilityMode.STABLE, new Function1<FingerprintResult, Unit>() {
            @Override
            public Unit invoke(FingerprintResult fingerprintResult) {
            	String stableFingerprint = fingerprintResult.getFingerprint();
                return null;
            }
        });
	
fingerprinter.getFingerprint(StabilityMode.UNIQUE, new Function1<FingerprintResult, Unit>() {
            @Override
            public Unit invoke(FingerprintResult fingerprintResult) {
            	String uniqueFingerprint = fingerprintResult.getFingerprint();
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

                HardwareSignalGroupProvider hardwareSignalProvider = fingerprintResult
                        .getSignalProvider(HardwareSignalGroupProvider.class);

                String hardwareFingerprint = hardwareSignalProvider.fingerprint(StabilityLevel.STABLE);
                Map<String,String> cpuInfo = hardwareSignalProvider.rawData().procCpuInfo().getValue();

                return null;
            }
        });

```

Also you can get fingerprint for every signal provider.

Available signal providers classes are:

1. HardwareSignalGroupProvider
2. OsBuildSignalGroupProvider
3. DeviceStateSignalGroupProvider
4. InstalledAppsSignalGroupProvider

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
