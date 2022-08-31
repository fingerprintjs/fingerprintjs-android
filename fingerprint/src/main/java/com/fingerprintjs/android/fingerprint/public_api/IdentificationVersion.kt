package com.fingerprintjs.android.fingerprint.public_api

import android.annotation.SuppressLint
import androidx.annotation.Discouraged

public enum class IdentificationVersion(
    internal val intValue: Int
) {
    V_1(intValue = 1),
    V_2(intValue = 2),
    V_3(intValue = 3),
    V_4(intValue = 4),
    V_5(intValue = 5),

    @Discouraged(
        message = "Use this value with a great caution. Since it will change over time " +
                "with the library updates, using it as a parameter to the library API may lead " +
                "to unintended change of the results of this API. "
    )
    LATEST(intValue = V_5.intValue),
}

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
public annotation class IdentificationVersionRange(
    val from: IdentificationVersion,
//    @SuppressLint("DiscouragedApi") val to: IdentificationVersion = IdentificationVersion.LATEST,
    val to: IdentificationVersion = IdentificationVersion.LATEST,
)
