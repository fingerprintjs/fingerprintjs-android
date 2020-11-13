package com.fingerprintjs.android.playground.fingerprinters_screen


import android.app.Activity
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fingerprintjs.android.fingerprint.Type
import com.fingerprintjs.android.playground.R
import com.fingerprintjs.android.playground.fingerprinters_screen.adapter.FingerprintItemAdapter
import com.fingerprintjs.android.playground.fingerprinters_screen.adapter.FingerprinterItem
import java.util.LinkedList


interface PlaygroundView {
    fun setFingerprintItems(items: List<FingerprinterItem>)
    fun setCustomFingerprint(
        customFingerprintValue: String,
        enabledFingerprintTypes: List<Int>? = null
    )

    fun setOnCustomFingerprintChangedListener(listener: ((Int) -> (Unit))?)
}

class PlaygroundViewImpl(
    activity: Activity
) : PlaygroundView {
    private val container: RecyclerView = activity.findViewById(R.id.fingerprinters_container)
    private val viewManager = LinearLayoutManager(activity)
    private val dataset = LinkedList<FingerprinterItem>()
    private val adapter = FingerprintItemAdapter(dataset)

    private val customFingerprintValueText: TextView =
        activity.findViewById(R.id.custom_fingerprinter_value)
    private val hardwareFingerprintCheckbox: CheckBox =
        activity.findViewById(R.id.checkbox_hardware_fingerprint)
    private val osBuildFingerprintCheckbox: CheckBox =
        activity.findViewById(R.id.checkbox_osbuild_fingerprint)
    private val deviceStateFingerprintCheckbox: CheckBox =
        activity.findViewById(R.id.checkbox_device_state_fingerprint)
    private val installedAppsFingerprintCheckbox: CheckBox =
        activity.findViewById(R.id.checkbox_installed_applications_fingerprint)

    private var checkboxChangedListener: ((Int) -> (Unit))? = null


    init {
        container.layoutManager = viewManager
        container.adapter = adapter

        hardwareFingerprintCheckbox.setOnClickListener {
            checkboxChangedListener?.invoke(
                Type.HARDWARE
            )
        }

        osBuildFingerprintCheckbox.setOnClickListener {
            checkboxChangedListener?.invoke(
                Type.OS_BUILD
            )
        }

        deviceStateFingerprintCheckbox.setOnClickListener {
            checkboxChangedListener?.invoke(
                Type.DEVICE_STATE
            )
        }

        installedAppsFingerprintCheckbox.setOnClickListener {
            checkboxChangedListener?.invoke(
                Type.INSTALLED_APPS
            )
        }
    }

    override fun setFingerprintItems(items: List<FingerprinterItem>) {
        dataset.clear()
        items.forEach {
            dataset.add(it)
        }
        adapter.notifyDataSetChanged()
    }

    override fun setCustomFingerprint(
        customFingerprintValue: String,
        enabledFingerprintTypes: List<Int>?
    ) {
        customFingerprintValueText.text = customFingerprintValue
        enabledFingerprintTypes ?: return

        hardwareFingerprintCheckbox.isChecked = false
        osBuildFingerprintCheckbox.isChecked = false
        deviceStateFingerprintCheckbox.isChecked = false
        installedAppsFingerprintCheckbox.isChecked = false

        enabledFingerprintTypes.forEach {
            when (it) {
                Type.HARDWARE -> hardwareFingerprintCheckbox.isChecked = true
                Type.OS_BUILD -> osBuildFingerprintCheckbox.isChecked = true
                Type.DEVICE_STATE -> deviceStateFingerprintCheckbox.isChecked = true
                Type.INSTALLED_APPS -> installedAppsFingerprintCheckbox.isChecked = true
            }
        }

    }

    override fun setOnCustomFingerprintChangedListener(listener: ((Int) -> (Unit))?) {
        checkboxChangedListener = listener
    }

}