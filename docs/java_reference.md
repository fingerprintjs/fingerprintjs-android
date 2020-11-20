# Java reference

## Advanced usage

### Change stablility/uniquiness ratio

`fingerprint` **is not a strict ID**, so there is a probability that two different devices will have the same fingerprint. Also, there is a probability, that the same device will have a different fingerprint in different moments due to system update, settings changing, etc.

A device fingerprint can be calculated using various platform signals.
These signals are grouped into several categories:

1. Hardware signals...
2. OS build signals & attributes ..
3. Device state information ..
4. Installed apps information .. (unstable signal source as apps get reinstalled all the time).


The library by default uses 1,2 and 3, and this gives the optimal stablility/uniquiness ratio.

But there is an ability to customize the ratio. You can choose the set of signal providers with bit mask. 

Here is the example, how to use all available signal providers for fingerprint calculation. This will improve the uniqueness of the fingerprint, but also it will reduce the stability. It will change more frequently.

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
                Map<String, String> cpuInfo = hardwareSignalProvider.rawData.getCpuInfo();

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

Hash function can be changed. By default the library uses MurMur hash (64x128) which is fast and optimal for most of the cases.

If it doesn't work for you, implement your own hasher, and pass it to `Configuration` class.

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

If you want to get a newer version of fingerprint, but also get an older one for backward compatibility, you can get them as shown below:

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