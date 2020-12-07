# Contributing to fingerprint-android

## Working with the code

To get started with a project, just open it using Android Studio after cloning.

### How to build

To build the library, run:

```bash
./gradlew fingerprint:assemble
```

The files will appear at `fingerprint/build/outputs/aar`.

The Playground application depends on libraries located in `fingerprint/build/outputs/aar` with the corresponding build-flavor. So before building the app make sure that the library has been built.

To build the Playground app, run:

```bash
./gradlew app:assemble
```

Also you can perform these actions with Android Studio.

### How to test

At the moment, library contains unit-tests and lint-checks. To perform the checks, run:

```bash
./gradlew fingerprint:lint
./gradlew fingerprint:testDebug
```

### Code style

The code style is the default for _Kotlin_.

Also, add 2 lines of indentation before and after import sections.

## Contributing

If you've found a bug, or you've extended the functionality of the library - simply create a PR.

### Adding new platform-signal

If you would like to add a new platform-signal follow the next steps:

1. Add a new method to an acceptable datasource (look the `datasources` package)
2. Wrap the method inside the `executeSafe` method - this will provide the crash-free property of the library.
3. Write a simple unit-test, where some exception occur while call the method. The expected result is no unhandled exceptions and returning some default value. 

Example for datasource with sensors information is below. The default value here is `emptyList()`.

```kotlin

class SensorDataSourceImpl(
    private val sensorManager: SensorManager
) : SensorDataSource {
    override fun sensors(): List<SensorData> {
        return executeSafe(
            {
                sensorManager.getSensorList(Sensor.TYPE_ALL).map {
                    SensorData(it.name, it.vendor)
                }
            }, emptyList()
        )
    }
}
```

Corresponding unit-test is following. `mock()` method will create dummy `SensorManager`, and that will produce NullPointerException. The expected result - is the emptiness of the returned list.

```kotlin

@Test
fun `SensorsDataSource crash free`() {
    val sensorsDataSource = SensorDataSourceImpl(
        mock()
    )
    assertEquals(0, sensorsDataSource.sensors().size)
}

```

### Adding new version of fingerprint

If you would like to create new version of fingerprint follow the next steps:

1. There is a `signal_providers` package, that contains classes responsible for aggregating datasources and calculating fingerprints. So add new version code for **every** signal provider, as shown below:

Before the update:

```kotlin
override fun fingerprint(): String {
    return when (version) {
        1 -> v1()
        else -> v1()
    }
}

private fun v1(): String {
...
}
```

After the update:

```kotlin
override fun fingerprint(): String {
    return when (version) {
        1 -> v1()
        2 -> v2()
        else -> v2()
    }
}

private fun v1(): String {
...
}

private fun v2(): String {
// Implement the new version of fingerprint here
}
```

2. Write unit-tests for every signal providers and the new version. See the examples in [test packages](../fingerprint/src/test/java/com/fingerprintjs/android/fingerprint/signal_providers)  . 