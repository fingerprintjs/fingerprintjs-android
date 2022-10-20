package com.fingerprintjs.android.fingerprint

import androidx.annotation.Discouraged

/**
 * This class represents the version of the logic provided by [Fingerprinter] API.
 * Whenever we implement new signals (completely new or just more stable variants of existing)
 * for device ID or fingerprint, the version is incremented.
 * Please keep in mind that changing [IdentificationVersion] leads to changing device id
 * and/or fingerprint returned by [Fingerprinter] API.
 */
public enum class IdentificationVersion(
    internal val intValue: Int
) {
    V_1(intValue = 1),
    V_2(intValue = 2),
    V_3(intValue = 3),
    V_4(intValue = 4),
    V_5(intValue = 5);

    public companion object {
        @get:Discouraged(
            message = "Use this value with a great caution. Since it will change over time " +
                    "with the library updates, using it as a parameter to the library API may lead " +
                    "to unintended change of the results of this API."
        )
        public val latest: IdentificationVersion
            get() = values().last()

        internal val fingerprintingFlattenedSignalsFirstVersion: IdentificationVersion
            get() = V_5

        internal val fingerprintingGroupedSignalsLastVersion: IdentificationVersion
            get() = V_4
    }
}
