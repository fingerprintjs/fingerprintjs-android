**Identifiers stability**

✅ – remains the same

🟡 – usually* remains the same

❌ – changes

| Event | Android ID | Media DRM ID | GSF ID | Hardware Fingerprint | OS Fingerprint | Installed apps fingerprint | Device state fingerprint |
| --- | --- | --- | --- | --- | --- | --- | --- |
| Remove all Google accounts from the device | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| Login back with the first Google account | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| Factory reset | ❌ | 🟡| ❌ | ✅ | ❌ | ❌ | ❌ |
| Repackaging the application | ❌ | ❌ | ✅ | ✅ | ✅ | ❌ | ✅ |
| System update | ✅ | ✅ | ✅ | 🟡 | ❌ | ❌ | ❌ |
| Different 'User' on the same device  | ❌ | ✅ | ❌ | ✅ | ✅ | ✅ | ❌ |
| Instant App compared to a regular app  | ✅ | ✅ | ❌ (not available) | ❌ | ❌ | ❌ (not available) | ❌ |

\* *With an exception for some device manufacturers (e.g. [Vivo](https://stackoverflow.com/questions/64509905/how-to-get-unique-id-in-android-q-that-must-be-same-while-uninstalling-and-inst) or [Samsung](https://github.com/fingerprintjs/fingerprintjs-android/issues/113))*
