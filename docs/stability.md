**Identifiers stability**

✅ – remains the same

❌ – changes

| Event | Android ID | Media DRM ID | GSF ID | Hardware Fingerprint | OS Fingerprint | Installed apps fingerprint | Device state fingerprint |
| --- | --- | --- | --- | --- | --- | --- | --- |
| Remove all Google accounts from the device | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| Login back with the first Google account | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| Factory reset | ❌ | ❌ | ❌ | ✅ | ❌ | ❌ | ❌ |
| Repackaging the application | ❌ | ❌ | ✅ | ✅ | ✅ | ❌ | ✅ |
| System update | ✅ | ✅ | ✅ | ✅ | ❌ | ❌ | ❌ |
| Different 'User' on the same device  | ❌ | ✅ | ❌ | ✅ | ✅ | ✅ | ❌ |
| Instant App compared to a regular app  | ✅ | ✅ | ❌ (not available) | ❌ | ❌ | ❌ (not available) | ❌ |
