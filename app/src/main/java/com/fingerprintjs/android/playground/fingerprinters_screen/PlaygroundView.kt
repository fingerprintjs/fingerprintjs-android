package com.fingerprintjs.android.playground.fingerprinters_screen


import android.app.Activity
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fingerprintjs.android.fingerprint.signal_providers.SignalGroupProviderType
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

    fun setFingerprinterVersion(version: Int)

    fun setOnCustomFingerprintChangedListener(listener: ((Int) -> (Unit))?)
}

class PlaygroundViewImpl(
    private val activity: Activity
) : PlaygroundView {
    private val container: RecyclerView = activity.findViewById(R.id.fingerprinters_container)
    private val viewManager = LinearLayoutManager(activity)
    private val dataset = LinkedList<FingerprinterItem>()
    private val adapter = FingerprintItemAdapter(dataset)

    private val customFingerprintHeadingText: TextView =
        activity.findViewById(R.id.custom_fingerprinter_heading)
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
                SignalGroupProviderType.HARDWARE
            )
        }

        osBuildFingerprintCheckbox.setOnClickListener {
            checkboxChangedListener?.invoke(
                SignalGroupProviderType.OS_BUILD
            )
        }

        deviceStateFingerprintCheckbox.setOnClickListener {
            checkboxChangedListener?.invoke(
                SignalGroupProviderType.DEVICE_STATE
            )
        }

        installedAppsFingerprintCheckbox.setOnClickListener {
            checkboxChangedListener?.invoke(
                SignalGroupProviderType.INSTALLED_APPS
            )
        }
    }

    override fun setFingerprinterVersion(version: Int) {
        customFingerprintHeadingText.text =
            activity.resources.getString(R.string.custom_fingerprint_heading, version.toString())
    }


    override fun setFingerprintItems(items: List<FingerprinterItem>) {
        activity.runOnUiThread {
            dataset.clear()
            items.forEach {
                dataset.add(it)
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun setCustomFingerprint(
        customFingerprintValue: String,
        enabledFingerprintTypes: List<Int>?
    ) {
        activity.runOnUiThread {
            customFingerprintValueText.text = customFingerprintValue
            if (enabledFingerprintTypes != null) {

                hardwareFingerprintCheckbox.isChecked = false
                osBuildFingerprintCheckbox.isChecked = false
                deviceStateFingerprintCheckbox.isChecked = false
                installedAppsFingerprintCheckbox.isChecked = false

                enabledFingerprintTypes.forEach {
                    when (it) {
                        SignalGroupProviderType.HARDWARE -> hardwareFingerprintCheckbox.isChecked = true
                        SignalGroupProviderType.OS_BUILD -> osBuildFingerprintCheckbox.isChecked = true
                        SignalGroupProviderType.DEVICE_STATE -> deviceStateFingerprintCheckbox.isChecked =
                            true
                        SignalGroupProviderType.INSTALLED_APPS -> installedAppsFingerprintCheckbox.isChecked =
                            true
                    }
                }
            }
        }
    }

    override fun setOnCustomFingerprintChangedListener(listener: ((Int) -> (Unit))?) {
        checkboxChangedListener = listener
    }

}