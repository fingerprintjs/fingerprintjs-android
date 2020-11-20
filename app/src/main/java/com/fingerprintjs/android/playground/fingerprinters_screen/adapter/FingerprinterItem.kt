package com.fingerprintjs.android.playground.fingerprinters_screen.adapter


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class FingerprinterItem(
    val id: String,
    val title: String,
    val fingerprintValue: String,
    val description: List<FingerprintSectionDescription>
) : Parcelable

@Parcelize
data class FingerprintSectionDescription(
    val name: String,
    val fields: List<Pair<String, String>>
) : Parcelable